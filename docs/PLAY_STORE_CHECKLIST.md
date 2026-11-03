# FinTrack Google Play Publishing Checklist

## Current Project Status

- Application ID: `com.fintrack.android`. Treat this package name as permanent after publishing.
- Current target SDK is 35. New app submissions and updates must target API 36 or higher from August 31, 2026. Upgrade the Android Gradle Plugin/SDK and validate behavior changes before submission.
- Release builds currently fall back to the debug signing key when `keystore.properties` is absent. Do not upload that build to Play.
- AdMob defaults in this project are Google's test app/unit IDs. They do not generate revenue and must be replaced with your registered AdMob IDs before production.
- The app now requests daily reference exchange rates and can display ads after consent. Disclose these network/SDK behaviors in the privacy policy and Play Console Data safety and Ads declarations.

## Before Upload

1. Register a Google Play developer account and create the app in Play Console. Verify current account identity/device and testing requirements there; personal accounts may have a mandatory closed-testing period before production access.
2. Set `compileSdk` and `targetSdk` to at least 36 with a compatible Android Gradle Plugin and installed SDK platform. Run the full test suite and test on API 36 devices.
3. Generate and securely back up a production signing key. Configure it through an untracked `keystore.properties` file, verify the signing certificate, and use Play App Signing when creating the app.
4. Replace the test AdMob app and banner IDs using Gradle properties `ADMOB_APP_ID` and `ADMOB_BANNER_ID`. Verify consent forms, test-device behavior, ad placement, and privacy options; never click live ads during testing.
5. Publish a privacy policy at a stable public URL that explains local financial storage, exchange-rate requests, advertising/consent SDK data, retention, and user choices.
6. Complete Play Console Data safety, Ads, content rating, target-audience, app-access, and financial-features declarations accurately. Provide reviewer access instructions if any app features are gated.
7. Prepare the store listing: app name, short/full descriptions, support email, privacy-policy URL, 512x512 icon, feature graphic, phone screenshots, and localized listing text.
8. Build and upload a signed Android App Bundle (AAB), not the debug-signed APK. Increase `versionCode` for every submitted update.
9. Run Play Console pre-review checks and an internal/closed test track, validate install/update/data preservation on multiple Android versions, then use a staged production rollout.

## Build Commands

```powershell
.\gradlew.bat testDebugUnitTest
.\gradlew.bat bundleRelease
```

If the Android Lint tool cannot parse the installed JDK version, use the repository's supported JDK 17 toolchain and resolve lint before submitting. Do not bypass lint as a production release check.

## Official References

- [Create and set up a Play app](https://support.google.com/googleplay/android-developer/answer/9859152)
- [Target API level requirements](https://support.google.com/googleplay/android-developer/answer/11926878)
- [Prepare your app for review](https://support.google.com/googleplay/android-developer/answer/9859455)
- [Google Mobile Ads quick start](https://developers.google.com/admob/android/quick-start)
- [Google UMP consent SDK](https://developers.google.com/admob/android/quick-start#request_consent)