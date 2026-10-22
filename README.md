# Credit Card Expense Tracker

An Android app, written in Java, that tracks spending on one credit card shared by several people. It records who spent what and who paid back what, and works out what each person owes before the bank due date on the 9th of each month.

Mobile Application Development (CS02310) lab project, Section W, The University of Lahore, Fall 2026.

## Group members

| Name | SAP ID |
| --- | --- |
| Danyal Amin | 70150522 |
| Fakiha Nadeem | 70150132 |
| Hafsa Rahman | 70150078 |
| Mohammad Hassan Nour | 70151880 |

## Project status

| Milestone | Tag | Contents |
| --- | --- | --- |
| Task 1: UI and layouts | `task-1` | Every screen built as XML layouts, with menus, strings, colours (light and dark), dimensions, icons, Activity shells and manifest entries |
| Task 2 | planned | Navigation between screens, sample data, list adapters, input validation, Room database |

## Required tools

- Android Studio (a version that supports Android Gradle Plugin 9.4.1)
- Android SDK Platform 37 (compile and target SDK 37, minimum SDK 24)
- An emulator or Android device running Android 7.0 (API 24) or newer. We test on the Medium Phone emulator, API 37.2
- JDK: Gradle provisions the JDK 25 daemon toolchain automatically on first sync, so no manual JDK setup is needed

## Setup and run

1. Clone the repository:
   `git clone https://github.com/70150522-Danyal/Credit-Card-Expense-Tracker.git`
2. In Android Studio choose **File > Open** and select the cloned folder.
3. Let Gradle sync finish. Android Studio creates `local.properties` with your SDK path automatically.
4. If prompted, install **Android SDK Platform 37** from the SDK Manager.
5. Create or start an emulator in **Device Manager**.
6. Select the `app` run configuration and press **Run**.

To check out exactly what was submitted for Task 1, run `git checkout task-1`.

## Database configuration

No database setup is needed. The app is designed to run fully offline on one device using Room (SQLite), stored in the app's private storage, so there is no server, account or connection string. Room is already added as a dependency; the database itself is created in code from Task 2 onwards.

## Project structure

| Path | Contents |
| --- | --- |
| `app/src/main/java/com/example/creditcardexpensetracker/` | `MainActivity`, which opens the Setup screen |
| `app/src/main/java/com/example/creditcardexpensetracker/ui/` | Activity classes for each screen |
| `app/src/main/res/layout/` | Screen, list row, dialog and bottom sheet layouts |
| `app/src/main/res/menu/` | Toolbar and item menus |
| `app/src/main/res/values/`, `values-night/` | Strings, colours, dimensions and themes for light and dark mode |
| `app/src/main/res/drawable/` | Vector icons and shape backgrounds |

## Known limitations (Task 1)

- Activities only call `setContentView()`, so buttons have no actions yet and the app stays on the Setup screen. The other screens can be viewed in Android Studio's Layout Editor, which shows them with sample data.
- Lists have no adapters or data yet, and the member chips on the member tab only have design-time names.
- Empty-state messages and input error areas are in the layouts but are not triggered yet; there is no input validation.
- The status bar icons are hard to read on the light app background (edge-to-edge drawing without light status bar icons).
