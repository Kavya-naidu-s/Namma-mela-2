# Namma-Mela Android App

This folder is now a Kotlin Android project for the Namma-Mela drama booking app.

## What is included

- Kotlin + Jetpack Compose Android app
- Customer flow:
  - Language and role selection
  - Phone OTP-style login mock
  - Tonight's shows list
  - Play detail page
  - Cast page
  - Seat selection
  - Booking confirmation
- Manager flow:
  - Email login mock
  - Saved manager email and login history
  - Dashboard metrics
  - Show list management surface
  - Add, edit, and delete shows
  - Manager login locked to `kavyanaidu2005@gmail.com`
  - Add/edit show form with duration, persona/about text, required poster image picker, lead actor name, and lead actor photo picker
- Customer flow:
  - OTP-gated phone login mock
  - Local phone-based booking history, so the same number sees previous data again
  - Plays tab
  - My Bookings tab
  - Fan Wall tab
  - Profile with previous booking count
- Booking flow:
  - Seat confirmation dialog before purchase
  - Booked seats become unavailable after confirmation
- Updated visual theme:
  - Fresh teal, coral, and gold color system
  - Updated launcher background color
  - Refreshed manager dashboard with booked/available seat counts and previous bookings
- Local mock data and booking business rules
- Unit tests for seat booking totals and seat selection limits

## Open in Android Studio

1. Open Android Studio.
2. Choose **Open**.
3. Select this folder:
   `C:\Users\kavya\OneDrive\Documents\Custom Office Templates\Desktop\namma mele projectsss`
4. Let Gradle sync.
5. Run the `app` configuration on an emulator or Android phone.

## Build from terminal

On this machine, the project was verified with:

```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-21'
& 'C:\Users\kavya\.gradle\wrapper\dists\gradle-9.2.1-bin\2t0n5ozlw9xmuyvbp7dnzaxug\gradle-9.2.1\bin\gradle.bat' testDebugUnitTest
& 'C:\Users\kavya\.gradle\wrapper\dists\gradle-9.2.1-bin\2t0n5ozlw9xmuyvbp7dnzaxug\gradle-9.2.1\bin\gradle.bat' assembleDebug
```

The generated debug APK is:

`app\build\outputs\apk\debug\app-debug.apk`

## If Android Studio says "application could not be installed"

The app builds and installs correctly on the emulator. If installation fails on the connected Moto E40 with `Requested internal only, but not enough space`, the phone storage is full. On the tested phone, `/data` was at 100% usage with only about 468 MB available.

Fix:

1. Free at least 1.5 GB internal storage on the phone.
2. Uninstall any older Namma-Mela test build if present.
3. In Android Studio, choose only one target device. The emulator installed successfully.
4. Run the app again.

## Next production steps

- Replace `NammaMelaRepository` mock data with Retrofit/Firebase repositories.
- Persist language and session values using DataStore.
- Add Firebase Phone Auth for real OTP.
- Add manager API authentication.
- Add Room caching for offline tickets and show data.
- Connect the selected poster URI to Firebase Storage or your backend upload API.
