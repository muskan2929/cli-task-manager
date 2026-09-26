# LEARNINGS.md — CLI Task Manager

## What I learned about design
Before this project I would have just started writing code. Working through requirements first — writing a requirement map, deciding how tasks should be stored, and choosing enums over strings for status and priority — showed me that most "bugs" in a real system come from decisions made (or skipped) before any code is written. For example, choosing a `Map<Integer, Task>` instead of a `List<Task>` wasn't about which was "correct" in some abstract sense — it came from noticing that almost every operation in the requirements was "find a task by its ID," and picking the data structure that matched that pattern.

I also learned the difference between a field that's just private for the sake of it, and a field that's private *because* an invariant depends on it — `id` and `createdAt` being `final` with no setters wasn't a style choice, it was making an incorrect state impossible to represent, rather than just "not allowed."

## What I learned about debugging
This was the hardest part of the whole project, and also where I learned the most. My biggest recurring problem wasn't understanding Java syntax — it was **not trusting what was actually on disk versus what I thought I'd changed.** Multiple times I edited a file in VS Code, believed I'd fixed something, and then got confused when the same error kept appearing — because the file hadn't actually been saved, or I was looking at the wrong file, or a stale `.class` file was still around from an earlier compile.

The habit that actually fixed this: instead of guessing, checking the literal file contents on disk (`findstr` on Windows) before assuming an edit had taken effect, and reading the compiler's exact error message and line number instead of skimming it. A compile error like `cannot find symbol` almost always means exactly what it says — something (a class, a method) genuinely doesn't exist where the compiler is looking, not that something is vaguely "broken."

I also learned that **one typo can look like several unrelated errors.** A single misspelled method name (`geStatus` instead of `getStatus`) caused a cascade of confusing symptoms elsewhere (a menu option "not working") — because the whole file failed to compile, so I was unknowingly still running old, previously-compiled code. Once I understood that "does it actually compile, with zero errors, right now" is the first thing to check — before touching feature logic — a lot of confusion disappeared.

## What I learned about Java specifically
- `private final` fields enforce immutability at compile time, not just by convention.
- Enums are reference types (can be `null`), unlike primitives like `int`.
- Method/constructor overloading lets you offer a simpler version of something without duplicating logic, by having the simple version call the full version internally.
- Exceptions (`throw`/`try`/`catch`) are a deliberate way to say "this input is invalid, stop here" — and letting an exception propagate up (rather than catching it too early) can actually simplify code, since methods that depend on each other (`updateTask` calling `getTask`) get error handling "for free."

## What I'd do differently next time
Compile after every single small change, not after several changes at once — it makes it much faster to know exactly which edit introduced a problem. And double-check a file is actually saved before assuming an edit "didn't work."
