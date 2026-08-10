# MoodKeyra — Private Offline Mood Keyboard

MoodKeyra is a lightweight Android keyboard that lets a person manually choose a visual mood while typing. It works offline and never reads, analyzes, rewrites, records, or uploads typed text.

> MoodKeyra is an independent product. It is not affiliated with Google or Gboard and must not use Gboard branding, icons, screenshots, or store-listing language.

## Documentation

Every project document is available directly from this README:

| Document | Purpose |
|---|---|
| [Architecture](docs/ARCHITECTURE.md) | Packages, responsibilities, data flow, and safe extension rules |
| [Build and release](docs/BUILD_AND_RELEASE.md) | Android Studio setup, APK installation, signed AAB, and release commands |
| [Permissions and data](docs/PERMISSIONS_AND_DATA.md) | Exact manifest permission audit and Gboard comparison |
| [Google Play checklist](docs/PLAY_STORE_CHECKLIST.md) | Binary, privacy, listing, testing, and reviewer requirements |
| [Privacy policy template](docs/PRIVACY_POLICY.md) | Public privacy-policy starting point that must be completed before release |
| [Store listing draft](docs/STORE_LISTING_DRAFT.md) | Accurate Play Store title, descriptions, reviewer notes, and screenshot plan |

## Current MVP

- Android `InputMethodService` with English QWERTY and symbol/number layouts.
- Normal, Happy, Excited, Calm, Sad, Angry, Thoughtful, and Romantic keyboard themes.
- Shift, backspace, editor action/enter, space, symbol toggle, and keyboard switcher.
- Password-field protection: mood display is disabled and typed content is never retained.
- Offline companion screen for privacy information and keyboard setup.
- State-aware one-button onboarding: MoodKeyra shows only the next required Android setup action, detects completion, and advances to the keyboard picker automatically after the user returns.
- No Internet, contacts, SMS, call log, microphone, camera, location, storage, notification, advertising ID, package visibility, or accessibility-service permission.
- No accounts, cloud service, ads, analytics, tracking SDK, database, or network dependency.

Third-party apps control their own message bubbles. MoodKeyra can theme its keyboard, but it cannot change the fonts, colors, or animations of messages displayed by WhatsApp, Messages, Telegram, Instagram, or email. Text sent to those apps remains ordinary text.

## Project identity

| Item | Value |
|---|---|
| Product name | MoodKeyra |
| Keyboard label | MoodKeyra Keyboard |
| Gradle project | `MoodKeyra` |
| Application ID | `com.moodkeyra.keyboard` |
| Minimum Android | Android 8.0 / API 26 |
| Compile and target API | Android 16 / API 36 |
| Version | `1.0.0` (`versionCode 1`) |

The application ID is the permanent Play Store identity. Confirm that you own and want `com.moodkeyra.keyboard` before the first production upload; it cannot be changed for updates to the same listing.

## Source structure

```text
MoodKeyra/
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── kotlin/com/moodkeyra/keyboard/
│       │   ├── MainActivity.kt
│       │   ├── data/MoodPreferences.kt
│       │   ├── ime/KeyboardKey.kt
│       │   ├── ime/MoodInputMethodService.kt
│       │   ├── ime/MoodKeyboardView.kt
│       │   └── model/Mood.kt
│       └── res/
├── docs/
│   ├── ARCHITECTURE.md
│   ├── BUILD_AND_RELEASE.md
│   ├── PLAY_STORE_CHECKLIST.md
│   └── PRIVACY_POLICY.md
├── gradle/wrapper/
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
└── README.md
```

The architecture intentionally stays small:

- `model` contains immutable mood definitions.
- `data` persists only the manually chosen mood and duration setting.
- `ime` renders the keyboard and commits individual key presses through Android's `InputConnection`.
- `MainActivity` contains offline setup and privacy guidance.

See [Architecture](docs/ARCHITECTURE.md) for responsibility and privacy boundaries.

## Build a debug APK

Install Android Studio, JDK 17, Android SDK Platform 36, Android SDK Build-Tools, and Platform-Tools. Open this folder—not its parent—in Android Studio.

From this folder:

```bash
./gradlew clean :app:lintDebug :app:buildMoodKeyraApk
```

Output:

```text
app/build/outputs/apk/MoodKeyra.apk
```

Install on a USB-connected device with USB debugging enabled:

```bash
adb install -r app/build/outputs/apk/MoodKeyra.apk
```

Open MoodKeyra, enable it in Android's keyboard settings, return to MoodKeyra, and select it from the keyboard picker. Android shows a standard warning when any third-party keyboard is enabled; MoodKeyra's offline design and absence of Internet permission are deliberate safeguards.

## Build for Google Play

Google Play requires an Android App Bundle. In Android Studio choose **Build → Generate Signed Bundle / APK → Android App Bundle**, create or select a private upload key, choose `release`, and generate the bundle.

Expected output:

```text
app/build/outputs/bundle/release/app-release.aab
```

Never commit a keystore or its passwords. Keep encrypted backups. Increment `versionCode` for every Play Store update.

See [Build and release](docs/BUILD_AND_RELEASE.md) and the [Play Store checklist](docs/PLAY_STORE_CHECKLIST.md) before distribution.

## Installed phone icon

The project includes an original adaptive launcher icon: a blue rounded-square surface with a white keyboard grid and a warm mood accent. Android uses it for the launcher, recent-apps screen, system app settings, and Play-installed application entry. Both standard and round icon variants are declared in the manifest.

## Privacy and Google Play

As of August 2026, new mobile apps and updates submitted after 31 August 2026 must target Android 16/API 36. This project does. Every published app must still complete Play Console Data safety and provide a public privacy-policy URL even when it collects no data.

The included [privacy-policy template](docs/PRIVACY_POLICY.md) accurately describes this source version. Before publishing it, replace the owner/contact placeholders, host it at a stable public URL, and re-audit the final AAB and every future dependency. Google—not this repository—makes the final policy and approval decision.

MoodKeyra intentionally requests less access than Gboard. Gboard's Play listing currently reports optional/broader data practices associated with features such as voice input and performance diagnostics. MoodKeyra does not implement those features, so copying those permissions would violate minimum-scope design. See [Permissions and data](docs/PERMISSIONS_AND_DATA.md).

Official references:

- [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- [Android input method guide](https://developer.android.com/develop/ui/views/touch-and-input/creating-input-method)
- [Google Play target API requirements](https://support.google.com/googleplay/android-developer/answer/11926878)
- [Google Play Data safety guidance](https://support.google.com/googleplay/android-developer/answer/10787469)
- [Google Play Developer Program Policies](https://play.google.com/about/developer-content-policy/)

## Status

The Gradle project configuration and XML resources are validated. A full build requires Android SDK Platform 36 on the build computer. Store approval cannot be guaranteed because Google reviews the final binary, declarations, listing, privacy policy, developer account, and policies in force on the submission date.
