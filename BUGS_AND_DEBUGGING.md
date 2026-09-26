# BUGS_AND_DEBUGGING.md — CLI Task Manager

This documents real bugs hit while building this project, how they were noticed, and how they were fixed.

---

### Bug 1: Typo in method call (`geStatus` instead of `getStatus`)
**Symptom:** Menu option "6" (Complete Task) appeared to do nothing when selected. Later, option "2" (List Tasks) also seemed broken.
**Root cause:** `Main.java` called `task.geStatus()` (missing the "t") in two places. This is not a valid method name, so the file failed to compile entirely.
**Why it was confusing:** Because the file didn't compile, `javac` never produced an updated `.class` file — so running `java Main` kept executing an *old* compiled version from before the new menu options were added. The symptom ("option 6 does nothing") looked like a logic bug, but the real problem was that the new code had never actually been compiled at all.
**Fix:** Corrected both occurrences to `getStatus()`, then recompiled and confirmed zero errors before testing again.
**Lesson:** A compile error anywhere in the file blocks *all* code in that file from updating — always confirm a clean compile (no errors at all) before assuming a specific feature is broken.

---

### Bug 2: `.gitignore` not actually ignoring `.class` files
**Symptom:** Running `git add .` staged `.class` files even though `.gitignore` was supposed to exclude them.
**Root cause:** The content pasted into `.gitignore` accidentally included explanatory text alongside the patterns, all mashed onto one line (e.g. `*.class # IDE clutter .vscode/ ...`), instead of one clean pattern per line. Git couldn't parse it as valid ignore rules.
**Fix:** Rewrote `.gitignore` with exactly one pattern per line, re-ran `git add .`, and confirmed via `git status` that only source files were staged.
**Lesson:** Copy-pasting code and its accompanying explanation into the same file is an easy mistake — always view the file's actual raw content before trusting it did what was intended.

---

### Bug 3: Compiled `.class` files got committed anyway, later blocking a merge
**Symptom:** `git merge` refused to proceed with the error "untracked working tree files would be overwritten by merge."
**Root cause:** `.class` files had been committed to git *before* `.gitignore` was fixed. Since git was already tracking them, fixing `.gitignore` afterward didn't retroactively untrack them — `.gitignore` only affects files git doesn't already know about.
**Fix:** Ran `git rm --cached src/*.class` to stop tracking them (without deleting them from disk), committed that removal, then deleted the leftover local `.class` files before retrying the merge.
**Lesson:** `.gitignore` must be set up correctly *before* the first `git add`; fixing it later requires an explicit `git rm --cached` step for anything already tracked.

---

### Bug 4: `updateTask` call had the wrong number of arguments after adding `priority`
**Symptom:** `cannot find symbol` / "actual and formal argument lists differ in length" — expected 4 arguments, found 3.
**Root cause:** After changing `TaskManager.updateTask`'s signature to accept a new `Priority` parameter, one existing call site in `Main.java` (`updateTaskFlow`) still called it with only the old 3 arguments — it hadn't been updated to match.
**Fix:** Located the outdated call and updated it to pass all four arguments, matching the new method signature.
**Lesson:** Changing a method's signature requires finding and updating *every* place that calls it — the compiler's error message names the exact file/line where the mismatch is, which is the fastest way to find every affected call site.

---

### Bug 5: Confusing "stale editor tab" vs. actual file on disk
**Symptom:** Errors referencing code (e.g. `Priority`) that shouldn't have existed on the current git branch, even though `git status` reported a clean working tree.
**Root cause:** VS Code had an open, unsaved editor tab showing older or different content than what was actually saved to disk / tracked by git. Trusting what the editor displayed, rather than the file on disk, led to chasing a bug that didn't really exist in the committed code.
**Fix:** Verified the real file contents directly via the terminal (`findstr` on Windows) rather than trusting the editor view, and closed/reopened stale tabs to resync them with disk.
**Lesson:** When git and the editor seem to disagree, the terminal reading the actual file content is the source of truth — not what's currently displayed in an editor tab.
