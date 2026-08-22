# UI Test Plan

This file records console UI test cases for `LenZaBot`.

Focus:
- invalid input handling
- command parsing regressions
- a small number of happy-path checks around task creation and status changes

Each test case below includes:
- an aim
- the commands to enter
- the expected output for each command
- a JSON block used by `.opencode/skills/test-ui/run-ui-tests.ps1`

## TC-INVALID-TODO-EMPTY

Aim: Reject a `todo` command with no description.

Commands:
```text
todo
bye
```

Expected output:
```text
Oops: the description for `todo` cannot be empty.
Bye! See ya later.
```

```json
{
  "id": "TC-INVALID-TODO-EMPTY",
  "aim": "Reject a `todo` command with no description.",
  "commands": [
    "todo",
    "bye"
  ],
  "expectedOutputs": [
    ["Oops: the description for `todo` cannot be empty."],
    ["Bye! See ya later."]
  ]
}
```

## TC-INVALID-UNKNOWN-COMMAND

Aim: Reject an unknown command instead of treating it as a task.

Commands:
```text
blah
bye
```

Expected output:
```text
Oops: I dont understand what you mean by "blah".
Bye! See ya later.
```

```json
{
  "id": "TC-INVALID-UNKNOWN-COMMAND",
  "aim": "Reject an unknown command instead of treating it as a task.",
  "commands": [
    "blah",
    "bye"
  ],
  "expectedOutputs": [
    ["Oops: I dont understand what you mean by \"blah\"."],
    ["Bye! See ya later."]
  ]
}
```

## TC-INVALID-DEADLINE-MISSING-BY

Aim: Reject a `deadline` command that is missing `/by`.

Commands:
```text
deadline return book
bye
```

Expected output:
```text
Oops: use `deadline <description> /by <time>`.
Bye! See ya later.
```

```json
{
  "id": "TC-INVALID-DEADLINE-MISSING-BY",
  "aim": "Reject a `deadline` command that is missing `/by`.",
  "commands": [
    "deadline return book",
    "bye"
  ],
  "expectedOutputs": [
    ["Oops: use `deadline <description> /by <time>`."],
    ["Bye! See ya later."]
  ]
}
```

## TC-INVALID-EVENT-MISSING-TO

Aim: Reject an `event` command that does not include `/to`.

Commands:
```text
event project meeting /from Mon 2pm
bye
```

Expected output:
```text
Oops: use `event <description> /from <start> /to <end>`.
Bye! See ya later.
```

```json
{
  "id": "TC-INVALID-EVENT-MISSING-TO",
  "aim": "Reject an `event` command that does not include `/to`.",
  "commands": [
    "event project meeting /from Mon 2pm",
    "bye"
  ],
  "expectedOutputs": [
    ["Oops: use `event <description> /from <start> /to <end>`."],
    ["Bye! See ya later."]
  ]
}
```

## TC-INVALID-MARK-NONNUMERIC

Aim: Reject `mark` when the task number is not numeric.

Commands:
```text
mark x
bye
```

Expected output:
```text
Oops: `mark` needs a valid task number.
Bye! See ya later.
```

```json
{
  "id": "TC-INVALID-MARK-NONNUMERIC",
  "aim": "Reject `mark` when the task number is not numeric.",
  "commands": [
    "mark x",
    "bye"
  ],
  "expectedOutputs": [
    ["Oops: `mark` needs a valid task number."],
    ["Bye! See ya later."]
  ]
}
```

## TC-INVALID-MARK-EMPTY-LIST

Aim: Reject `mark` when there are no tasks in the list.

Commands:
```text
mark 1
bye
```

Expected output:
```text
Oops: there are no tasks in the list yet.
Bye! See ya later.
```

```json
{
  "id": "TC-INVALID-MARK-EMPTY-LIST",
  "aim": "Reject `mark` when there are no tasks in the list.",
  "commands": [
    "mark 1",
    "bye"
  ],
  "expectedOutputs": [
    ["Oops: there are no tasks in the list yet."],
    ["Bye! See ya later."]
  ]
}
```

## TC-VALID-BASIC-FLOW

Aim: Confirm a basic happy path across `todo`, `deadline`, `event`, `list`, `mark`, and `unmark`.

Commands:
```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
mark 2
unmark 2
bye
```

Expected output:
```text
Added task: [T][ ] borrow book
Added task: [D][ ] return book (by: Sunday)
Added task: [E][ ] project meeting (from: Mon 2pm to: 4pm)
1. [T][ ] borrow book
2. [D][ ] return book (by: Sunday)
3. [E][ ] project meeting (from: Mon 2pm to: 4pm)
Good job, marked the following task as completed: [D][X] return book (by: Sunday)
Ok, marked the following task as incomplete: [D][ ] return book (by: Sunday)
Bye! See ya later.
```

```json
{
  "id": "TC-VALID-BASIC-FLOW",
  "aim": "Confirm a basic happy path across `todo`, `deadline`, `event`, `list`, `mark`, and `unmark`.",
  "commands": [
    "todo borrow book",
    "deadline return book /by Sunday",
    "event project meeting /from Mon 2pm /to 4pm",
    "list",
    "mark 2",
    "unmark 2",
    "bye"
  ],
  "expectedOutputs": [
    ["Added task: [T][ ] borrow book"],
    ["Added task: [D][ ] return book (by: Sunday)"],
    ["Added task: [E][ ] project meeting (from: Mon 2pm to: 4pm)"],
    [
      "1. [T][ ] borrow book",
      "2. [D][ ] return book (by: Sunday)",
      "3. [E][ ] project meeting (from: Mon 2pm to: 4pm)"
    ],
    ["Good job, marked the following task as completed: [D][X] return book (by: Sunday)"],
    ["Ok, marked the following task as incomplete: [D][ ] return book (by: Sunday)"],
    ["Bye! See ya later."]
  ]
}
```
