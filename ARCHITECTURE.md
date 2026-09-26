# ARCHITECTURE.md — CLI Task Manager

## Overview
The application is a single-process, in-memory CLI program with four classes, split by responsibility:

```
Main.java          → CLI layer: prints menu, reads user input, calls TaskManager, formats output
TaskManager.java    → Business logic: owns the task collection, enforces rules, validates input
Task.java           → Data model: represents one task and its fields
Status.java         → Enum: PENDING / COMPLETED
Priority.java       → Enum: LOW / MEDIUM / HIGH
```

## Layering and responsibility

**Main (CLI layer)**
- Owns the menu loop and `Scanner` for reading keyboard input.
- Never touches the `tasks` collection directly — always goes through `TaskManager`.
- Converts raw user input (strings) into typed values (`int` IDs via `Integer.parseInt`, `Priority` via `Priority.valueOf`) before calling into `TaskManager`.
- Wraps every call to `TaskManager` in try/catch, translating exceptions into user-friendly messages. This is the only layer that prints to the console.

**TaskManager (business logic layer)**
- Holds the single source of truth: a `Map<Integer, Task>` plus a `nextId` counter.
- Exposes one method per capability (createTask, getTask, updateTask, deleteTask, completeTask, listTasks, searchTasks).
- Enforces the rules that must always hold, regardless of who's calling: titles can't be blank, an ID must exist before it can be fetched/updated/deleted/completed.
- Never talks to the console (no `System.out` here) and never reads user input directly — this keeps business rules testable independently of the CLI.

**Task (data model)**
- A plain class representing one task's state: id, title, description, status, priority, createdAt.
- Protects its own invariants: `id` and `createdAt` are `final` and have no setters, so nothing outside the class can ever change them after construction.

**Status / Priority (enums)**
- Fixed, closed sets of valid values. Used instead of `String` so invalid states are impossible at compile time rather than merely "supposed not to happen."

## Data flow example (Complete Task)
1. `Main` reads the menu choice `"6"`, calls `completeTaskFlow`.
2. `completeTaskFlow` reads the ID as a `String`, parses it to `int` (catching `NumberFormatException` if it's not numeric).
3. It calls `manager.completeTask(id)`.
4. `TaskManager.completeTask` calls `getTask(id)` — throws `IllegalArgumentException` if the ID isn't in the map, which unwinds straight back to `Main`'s catch block.
5. If found, `TaskManager` calls `task.setStatus(Status.COMPLETED)` and returns the task.
6. `Main` prints a confirmation message using the returned `Task`'s `getId()`.

This flow — CLI parses/validates format, TaskManager enforces business rules, Task holds state — repeats for every other operation.

## Why this structure
Separating CLI from business logic means the core task-management rules aren't tied to a console interface — the `TaskManager` class could be reused behind a web API or GUI without any changes, since it has no dependency on `System.out` or `Scanner`. It also makes debugging easier: a bug in "how a task is stored/validated" is always in `TaskManager` or `Task`, never in `Main`, and a bug in "what the user sees" is always in `Main`.
