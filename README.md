# CLI Task Manager

A command-line task manager built in Java. Tasks are stored in memory only (nothing persists after the program exits) — this is a learning project focused on clean design, validation, and error handling rather than persistence.

## Features
- Create a task (title, description, optional priority — defaults to MEDIUM)
- List all tasks
- Get a single task by ID
- Update a task's title, description, and/or priority
- Delete a task
- Mark a task complete
- Search tasks by keyword (case-insensitive, partial match on title)
- Exit cleanly

## Requirements
- A JDK (Java 17 or later recommended). Check with:
  ```
  java -version
  javac -version
  ```

## Project structure
```
.
├── DESIGN.md
├── ARCHITECTURE.md
├── LEARNINGS.md
├── BUGS_AND_DEBUGGING.md
├── README.md
└── src/
    ├── Main.java
    ├── TaskManager.java
    ├── Task.java
    ├── Status.java
    └── Priority.java
```

## How to build
From the `src` folder:
```
javac Status.java Priority.java Task.java TaskManager.java Main.java
```
No output means it compiled successfully.

## How to run
Still inside `src`:
```
java Main
```

## Usage
On launch you'll see a numbered menu:
```
--- Task Manager ---
1. Create Task
2. List Tasks
3. Get Task
4. Update Task
5. Delete Task
6. Complete Task
7. Search Tasks
8. Exit
Choose an option:
```
Type the number of the action you want and press Enter, then follow the prompts. Invalid input (a non-numeric ID, a nonexistent ID, a blank title, an unrecognized priority) is reported with a message — the program will not crash, and will return you to the menu.

## Design notes
See `DESIGN.md` for the reasoning behind key decisions (ID generation, enums vs. strings, storage structure, validation strategy) and `ARCHITECTURE.md` for how the classes fit together.
