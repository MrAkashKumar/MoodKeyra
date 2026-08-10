# MoodKeyra architecture

## Design goals

MoodKeyra prioritizes privacy, predictable keyboard behavior, a small binary, and code that can be reviewed without hidden data flows. It uses Android platform Views rather than a large UI framework and has no third-party runtime dependencies.

## Components

### `MainActivity`

The launcher and IME settings activity. It explains offline behavior and displays only the next setup action.

### `KeyboardSetup`

A reusable setup controller that detects whether MoodKeyra is enabled and selected, opens Android's official input-method settings, and displays the system keyboard picker. It cannot bypass Android's required user approval.

### `MoodInputMethodService`

The Android IME boundary. It:

- creates the keyboard view;
- detects numeric and password editor types;
- commits only the key selected by the user through `InputConnection`;
- invokes the target editor's action for Send, Search, Done, and similar actions;
- exposes a standard path to the next keyboard;
- never reads surrounding text, clipboard data, contacts, messages, or conversation identity.

### `MoodKeyboardView`

A reusable custom View responsible only for measuring, drawing, hit testing, and emitting semantic `KeyboardKey` or `Mood` selections. It does not have access to `InputConnection` or persistence.

### `MoodPreferences`

A narrow wrapper over private `SharedPreferences`. It stores only:

- selected mood enum name;
- whether the mood should reset after one message.

Typed text is never passed to this class. Backup and device transfer exclude these preferences.

### `Mood` and `KeyboardKey`

Small domain types that keep visual theme definitions and keyboard actions separate from Android service logic.

## Data flow

```text
Touch → MoodKeyboardView → KeyboardKey → MoodInputMethodService → InputConnection → active text field
                         ↘ Mood → MoodPreferences
```

There is no network, database, logging, analytics, suggestion engine, spell checker, clipboard reader, or message-history flow.

## Package convention

The package root is `com.moodkeyra.keyboard`. Packages are lowercase and mirror directories. Classes and files use UpperCamelCase; functions and properties use lower camel case, following Kotlin conventions.

## Safe extension rules

Future contributors should not add typed-text logging, sentiment inference, network processing, unrestricted storage, advertising SDKs, or behavioral analytics. Any new SDK or permission requires a new privacy and Play policy audit before release.
