# Build and release guide

## Required tools

- Android Studio compatible with Android Gradle Plugin 8.13.2
- JDK 17
- Android SDK Platform 36
- Current Android SDK Build-Tools and Platform-Tools

In Android Studio, use **Settings → Android SDK** to install the SDK components and **Settings → Build Tools → Gradle** to select JDK 17.

## Command-line validation

Run from the MoodKeyra project folder:

```bash
./gradlew clean
./gradlew :app:lintDebug
./gradlew :app:buildMoodKeyraApk
```

The normal Gradle debug artifact is generated first, then the project task copies it to `app/build/outputs/apk/MoodKeyra.apk`.

## Device installation

Enable Developer options and USB debugging on the phone, connect it, authorize the computer, and run:

```bash
adb devices
adb install -r app/build/outputs/apk/MoodKeyra.apk
```

Open MoodKeyra and follow its guided setup. Test normal, password, number, email, URL, multiline, Search, Done, and Send fields. Test portrait and landscape and verify the keyboard switch control from every layout.

## Signed Play bundle

Use Android Studio's **Generate Signed Bundle / APK** flow and select Android App Bundle. Store the upload keystore outside the repository. Use a unique password, maintain encrypted backups, and restrict access.

Before every release:

1. Increment `versionCode`; update `versionName` when appropriate.
2. Run `./gradlew :app:lintRelease :app:testReleaseUnitTest :app:bundleRelease`.
3. Inspect the merged release manifest and verify there is no `INTERNET` or unexpected permission.
4. Test the release build on supported Android versions and screen sizes.
5. Re-audit Data safety, privacy policy, dependencies, SDKs, permissions, and current Play policies.
6. Upload the signed AAB to a testing track before production.

The expected bundle is `app/build/outputs/bundle/release/app-release.aab`.

## Signing warning

Never commit `.jks`, `.keystore`, passwords, Play service-account credentials, or `local.properties`. Losing the upload key can delay releases; losing access to the Play developer account or Play App Signing process may prevent updates.
