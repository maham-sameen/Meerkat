# Meerkat — Calendly Clone for Android

Meerkat is an Android scheduling app that mirrors the core Calendly experience: publish your real Google Calendar availability, let invitees pick a slot, and let the app handle the Google Meet event and timezone-aware email invitations.

## 🎬 Demo

**Full walkthrough on YouTube → https://youtu.be/Mtl5rLWHrAw?si=GT481nCQay3qhmQP

## ✨ Features

1. **Authentication** — Email/password sign-in via Firebase Auth, and Google sign-in via OAuth 2.0.
2. **One-to-one and one-to-many meetings** — Pick one or many contacts as invitees.
3. **Live availability from Google Calendar** — Free time slots are computed from the host's Google Calendar; busy times are filtered out automatically.
4. **Full Google Calendar sync** — Creating, updating, or deleting a meeting in Meerkat creates, updates, or deletes the corresponding event in Google Calendar (with an auto-generated Google Meet link).
5. **Timezone-aware email invitations** — Every invitee receives an email showing the meeting time converted to **their** time zone.

### Example

Say the host (Pakistan time, `mahamsameenhere@gmail.com`) schedules a 30-minute meeting at **10:00 AM on 17 Dec** with two invitees:

| Invitee | Email | Time zone | Time shown in email |
|---|---|---|---|
| Naeem | `naeemnaeema46@gmail.com` | Asia/Aden | Tue 16 Dec 2025 · 8:00 – 8:30 AM (Arabian Time) |
| Freak Witch | `freakwitch049@gmail.com` | America/Rosario | Tue 16 Dec 2025 · 2:00 – 2:30 AM (Argentina Time) |

Edit the meeting (e.g. remove an invitee) and the Google Calendar event + email invitations update accordingly.

## 🧱 Tech stack

- **Language:** Kotlin (JDK 17)
- **UI:** Jetpack Compose + Material 3
- **Architecture:** Clean architecture per feature module (`data` / `domain` / `presentation`), MVVM with `StateFlow`
- **DI:** Hilt (+ Hilt Worker)
- **Local storage:** Room, DataStore
- **Networking:** Retrofit + Gson (Google Calendar API)
- **Auth:** Firebase Auth, Google Identity Services, Credentials Manager
- **Cloud:** Firebase Auth, Firestore, Cloud Functions, Cloud Messaging, **Trigger Email from Firestore** extension
- **Background work:** WorkManager (email dispatch queue with retry)
- **Async:** Coroutines + Flow

## 🏗️ Project structure

```
com.example.meerkat
├── core/                  shared building blocks (calendar, time-zone, presentation helpers)
├── di/                    Hilt modules
├── feature_auth/          email + Google sign-in
├── feature_contacts/      contact CRUD
├── feature_event/         home screen + bottom-nav graph
├── feature_meetings/      booking flow, meeting list, meeting details
├── feature_notification/  in-app notifications
├── feature_setting/       profile / sign-out / preferences
└── ui/                    Material 3 theme
```

## 🚀 Run locally

1. Clone the repo:
   ```bash
   git clone https://github.com/maham-sameen/Meerkat.git
   cd Meerkat
   ```
2. Create your own Firebase project, add an Android app with package `com.example.meerkat`, and replace `app/google-services.json` with your own.
3. In the Firebase console, enable **Authentication (Email/Password + Google)**, **Firestore**, and (optional) the **Trigger Email from Firestore** extension for invitation emails.
4. Enable **Google Calendar API** in the linked GCP project and add the SHA-1 of your debug signing certificate to the Android OAuth client.
5. Open in Android Studio (Hedgehog+), let Gradle sync, and run.

## 👤 Author

**Maham Sameen** — `mahamsameenhere@gmail.com`

If this project helped you, please ⭐ the repo.
