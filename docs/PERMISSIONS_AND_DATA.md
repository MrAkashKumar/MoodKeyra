# Permissions and data audit

## MoodKeyra manifest result

MoodKeyra declares no ordinary `uses-permission` entry. Its input-method service is protected with:

```xml
android:permission="android.permission.BIND_INPUT_METHOD"
```

This is not a permission MoodKeyra requests from the user. It is a signature-level system requirement that means only Android can bind to the keyboard service. Android's input-method documentation states that the system will not use an IME that does not protect its service with `BIND_INPUT_METHOD`.

## Permissions deliberately absent

MoodKeyra does not declare:

- `INTERNET` or network-state access;
- microphone or audio recording;
- contacts, calendar, phone, SMS, or call logs;
- camera, photos, videos, files, or broad storage;
- approximate, precise, or background location;
- notifications, advertising ID, Bluetooth, or nearby devices;
- installed-app inventory or `QUERY_ALL_PACKAGES`;
- accessibility service, overlay, device admin, VPN, or usage access.

Therefore the current app does not display Android runtime-permission prompts. Enabling any third-party keyboard still triggers Android's standard system warning; that warning is controlled by Android and cannot or should not be bypassed.

## Why MoodKeyra should not copy Gboard permissions

The June 2026 Gboard Play listing describes many features beyond MoodKeyra's scope, including voice typing, online-assisted features, and diagnostics. Its Data safety section says the app may collect audio, app activity, crash logs, diagnostics, and other performance data while sharing no data with third parties.

MoodKeyra is deliberately narrower: manual mood selection, ordinary key input, and local appearance only. Those features need no microphone, Internet connection, analytics, crash-reporting SDK, or sensitive runtime permission. Adding access merely because another keyboard uses it would conflict with Google Play's minimum-scope policy, which requires sensitive access to be necessary for promoted core functionality.

## Data behavior

- Typed content is committed directly to the active field and is never persisted.
- The service never calls APIs to read text before or after the cursor.
- Password fields force the Normal theme.
- Only the manually selected mood and one-message preference are stored locally.
- Preferences are excluded from cloud backup and device transfer.
- No SDK transmits data because there is no networking permission or third-party runtime SDK.

## Play Console declaration

For the source currently documented, Data safety can state that no user data is collected or shared only after the final signed AAB is re-audited. Google requires the form and a public privacy-policy URL even for apps that collect nothing.

Official references:

- [Android InputMethod API and required service protection](https://developer.android.com/reference/android/view/inputmethod/InputMethod)
- [Gboard Play listing](https://play.google.com/store/apps/details?id=com.google.android.inputmethod.latin)
- [Gboard Data safety details](https://play.google.com/store/apps/datasafety?id=com.google.android.inputmethod.latin)
- [Google Play permissions and sensitive APIs policy](https://support.google.com/googleplay/android-developer/answer/16558241)
- [Google Play Data safety instructions](https://support.google.com/googleplay/android-developer/answer/10787469)
