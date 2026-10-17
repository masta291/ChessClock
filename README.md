# ♟️ ChessClock

A chess clock for Android, written in **Kotlin** with **Jetpack Compose**.
Place the phone between two players: each one taps their half of the screen to end their move and pass the turn to the opponent.

<!-- Replace with your own screenshots (screenshots/ folder) -->
<!-- ![Screenshot](screenshots/main.png) -->

## Table of contents

- [Features](#features)
- [Usage](#usage)
- [Built with](#built-with)
- [Getting started](#getting-started)
- [Project structure](#project-structure)
- [What I learned](#what-i-learned)
- [Roadmap](#roadmap)
- [License](#license)

## Features

- **Two player zones**: the screen is split in two, and the top player's zone is rotated 180° so players can sit face to face.
- **Fischer increment**: a configurable number of seconds is added after each move.
- **Active player highlight**: the zone whose turn it is changes color.
- **Move counter** for each player.
- **Live time adjustment**: `-30s`, `-15s`, `+15s` and `+30s` buttons on each zone.
- **Pause, resume and reset** from the control bar.
- **Game over screen**: the player who runs out of time sees "YOU LOSE" on a red background.
- **Settings**: starting time (1, 3, 5 or 10 minutes) and increment (0, 2, 5 or 10 seconds).
- **Saved settings**: your choices are kept after the app is closed.

## Usage

1. Open the app: the clock starts paused.
2. Choose the time control in **Settings** (gear icon).
3. Press **▶ Play** to start the game: Player 1's time starts counting down.
4. At the end of their move, each player **taps their own zone** to pass the turn.
5. Use **⏸ Pause** to stop the clock and **🔄 Replay** to start over.

| Button | Action |
|---|---|
| ▶ | Start / resume |
| ⏸ | Pause |
| 🔄 | Reset times and move counters |
| ⚙️ | Open settings |

## Built with

- [Kotlin](https://kotlinlang.org/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose) and Material 3
- Kotlin coroutines (`LaunchedEffect`, `delay`) for the countdown
- `SharedPreferences` to persist settings
- Gradle (Kotlin DSL)

## Getting started

### Prerequisites

- A recent version of [Android Studio](https://developer.android.com/studio)
- An Android device or emulator (the minimum Android version is defined in `app/build.gradle.kts`)

### Run the project

```bash
git clone https://github.com/masta291/ChessClock.git
cd ChessClock
```

1. Open the folder in Android Studio.
2. Wait for the Gradle sync to finish.
3. Select a device or emulator, then click **Run ▶**.

## Project structure

```
ChessClock/
├── app/
│   └── src/main/
│       ├── java/com/example/chessclock/
│       │   ├── MainActivity.kt      # main screen and UI components
│       │   └── ui/theme/            # colors, typography, theme
│       └── res/                     # icons and resources
├── gradle/
└── build.gradle.kts
```

Main components in `MainActivity.kt`:

| Component | Role |
|---|---|
| `ChessClockScreen` | Holds the state (times, moves, active player, pause) and runs the countdown |
| `PlayerZone` | Displays one player's zone (time, moves, quick-adjust buttons) |
| `ControleButton` | Icon button used in the control bar |
| `TimeChip` | Small rounded button to adjust the time |
| `OptionButton` | Selectable button used in the settings dialog |
| `formatTime` | Converts seconds into `mm:ss` |

## What I learned

- Building a layout with `Row`, `Column` and `Box`
- Managing state with `remember` and `mutableStateOf`
- State hoisting and passing functions as parameters
- Running a countdown with `LaunchedEffect` and coroutines
- Deriving values from state instead of storing them
- Saving preferences with `SharedPreferences`

## Roadmap

- [ ] Sound on each move and when time runs out
- [ ] Editable player names
- [ ] Keep the screen on during a game
- [ ] Preserve state when the screen is rotated
- [ ] More time controls (Bronstein, simple delay, per-move time)
- [ ] Interface translations

## License

This project is released under the MIT License. Add a `LICENSE` file at the root of the repository
(GitHub offers a template: **Add file > Create new file > name it `LICENSE`**).

## Author

**masta291**: [github.com/masta291](https://github.com/masta291)
