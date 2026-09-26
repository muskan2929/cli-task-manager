# DESIGN.md — CLI Task Manager

## 1. Problem, in my own words
This is a command-line program that manages a list of tasks entirely in memory (nothing is saved to disk). A user can create a task, list all tasks, look one up by ID, update its title/description/priority, mark it complete, delete it, search for tasks by keyword, or exit the program. Invalid input — a bad ID, a blank title, non-numeric input — must be handled gracefully, without ever crashing the app.

## 2. Requirement map

| Operation | Input | Output | Failure case |
|---|---|---|---|
| Create | title, description, priority (optional) | new Task with assigned ID | blank/empty title |
| List | none | all tasks | empty list — shown as a message, not an error |
| Get | ID | task details | ID doesn't exist |
| Update | ID, new title, new description (optional), new priority (optional) | updated task | ID doesn't exist; blank title |
| Delete | ID | confirmation | ID doesn't exist |
| Complete | ID | status set to COMPLETED | ID doesn't exist |
| Search | keyword | matching tasks (case-insensitive, partial match) | zero matches — shown as a message, not an error |

## 3. Design decisions and why

**Task ID generation — simple incrementing counter (starting at 1), not UUID.**
For an in-memory, single-run program, a counter is simplest, fully human-readable, and easy to debug. A UUID would only be justified if IDs needed to be unguessable or survive across restarts — neither applies here.

**Status and Priority — Java enums, not Strings.**
An enum (`Status`: PENDING/COMPLETED; `Priority`: LOW/MEDIUM/HIGH) makes invalid values impossible at compile time. A `String` field would allow typos (`"compelte"`) or inconsistent casing (`"High"` vs `"HIGH"`) that the compiler can't catch. This tradeoff was made deliberately after comparing both options.

**Storage — `Map<Integer, Task>`, not `List<Task>`.**
Nearly every operation (get, update, delete, complete) is "find the task by ID." A `Map` turns that into a direct O(1) lookup (`map.get(id)`); a `List` would require scanning every element. Since ID lookup is the dominant access pattern, `Map` is the better fit.

**Encapsulation — private fields, selective getters/setters.**
`id` and `createdAt` are `private final` with only getters — no way to reassign them after creation, which protects the "unique, unchanging ID" and "immutable creation timestamp" guarantees at the compiler level, not just by convention. `title`, `description`, `status`, and `priority` are `private` with both getters and setters, since Update and Complete legitimately need to change them.

**Error handling — exceptions, not null returns.**
`getTask(id)` throws `IllegalArgumentException` if the ID doesn't exist, rather than returning `null`. This makes every other method that depends on `getTask` (update, delete, complete) automatically safe — they either get a real `Task` or the exception stops execution immediately. The alternative (returning `null`) would require a null-check at every call site, which is easy to forget.

**Validation — centralized in one method (`validateTitle`), reused everywhere.**
Both `createTask` and `updateTask` call the same `validateTitle` helper, rather than duplicating the blank-title check in two places. Duplicated validation logic is a common source of bugs (fix it in one place, forget the other).

**Update semantics — `null` means "don't change this field."**
`updateTask` accepts `description` and `priority` as nullable parameters. If the caller passes `null`, the existing value is preserved. This lets a user update just the title without being forced to re-type (or accidentally wipe) the description or priority. The CLI layer converts an empty typed line (`""`) into `null` before calling into `TaskManager`, since `Scanner` never actually returns `null` for a line the user typed nothing into.

**Completing an already-completed task — allowed silently, not an error.**
Re-marking a completed task as complete doesn't corrupt any data, so it's treated as a harmless no-op rather than an exception. This avoids punishing a user for an accidental double-action.

**Default priority — MEDIUM, via constructor/method overloading.**
`Task` and `TaskManager.createTask` both have two overloads: one accepting an explicit `Priority`, and a simpler one that omits it and defaults to `MEDIUM`. This avoids duplicating logic between the two versions (the simple version just calls the full version internally).

## 4. Edge cases considered
1. Blank or whitespace-only title on create or update → rejected with a clear error, task not created/changed.
2. Nonexistent task ID passed to get/update/delete/complete → caught and reported, app keeps running.
3. Non-numeric input where an ID is expected (e.g. typing "abc") → caught separately from "ID not found," with its own message.
4. Empty task list when running List → shown as "No tasks yet." rather than blank output or an error.
5. Zero search results → shown as "No matching tasks found." rather than an error.
6. Invalid priority value typed by the user (e.g. "urgent") → `Priority.valueOf` throws automatically, caught and reported.
7. Updating a task while leaving description/priority blank → existing values preserved, not wiped.
8. Completing an already-completed task → succeeds silently, no error.

## 5. What I would change with more time
- Persist tasks to a file so data survives between runs.
- Add due dates and sort/filter by priority or due date.
- Replace the numbered-menu CLI with a proper command syntax (e.g. `create "title" "desc"`).
