# FinTrack - Generating a Release Signed APK

## 1. Prerequisites
- Android Studio Ladybug / Meerkat or AGP 8.7+ installed
- JDK 17 configured (`JAVA_HOME`); the current Android Lint toolchain cannot parse JDK 25 version strings
- Android SDK Platform 35 installed

---

## 2. Generating a Release Keystore
If you do not have an existing signing keystore, create one using `keytool` from your JDK's `bin` directory:

```bash
keytool -genkey -v -keystore fintrack-release-key.jks -alias fintrack_key -keyalg RSA -keysize 2048 -validity 10000
```
Follow the interactive prompts:
- Enter a secure keystore password.
- Provide developer/organization details (Name, Organization, City, Country Code).
- Note down your `keystore password`, `key alias`, and `key password`.

---

## 3. Configure the Release Keystore
Do **NOT** commit the keystore or passwords into version control. Create a `keystore.properties` file in the project root (it is excluded in `.gitignore`):

```properties
# keystore.properties
storeFile=../fintrack-release-key.jks
storePassword=YOUR_SECURE_STORE_PASSWORD
keyAlias=fintrack_key
keyPassword=YOUR_SECURE_KEY_PASSWORD
```

The Gradle build reads this file automatically. If it is absent, release builds use the debug key for local device testing; that APK must **not** be uploaded to Google Play. A release keystore is required for Play Store distribution, and the same key must be retained for future updates.

---

## 4. Building the Signed APK / AAB

### Method A: Via Command Line (Gradle Wrapper)
Run the following command from the project root:

**On Windows (PowerShell / Command Prompt):**
```powershell
.\gradlew.bat assembleRelease
```
Or to build an Android App Bundle (AAB) for Google Play:
```powershell
.\gradlew.bat bundleRelease
```

**On macOS / Linux:**
```bash
./gradlew assembleRelease
# Or for App Bundle:
./gradlew bundleRelease
```

### Method B: Via Android Studio IDE
1. Open the project in **Android Studio**.
2. Select **Build** > **Generate Signed Bundle / APK...** from the top menu.
3. Choose **APK** or **Android App Bundle** and click **Next**.
4. Choose your `fintrack-release-key.jks`, enter your credentials, and select **release** destination.
5. Check **V1 (Jar Signature)** and **V2 (Full APK Signature)**.
6. Click **Finish**.

---

## 5. Locating the Output
- **Signed APK Location**:
  `app/build/outputs/apk/release/app-release.apk`
- **Signed App Bundle Location**:
  `app/build/outputs/bundle/release/app-release.aab`

