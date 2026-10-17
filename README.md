# ♟️ ChessClock

Une pendule d'échecs pour Android, écrite en **Kotlin** avec **Jetpack Compose**.
Posez le téléphone entre deux joueurs : chacun tape sa moitié d'écran pour terminer son coup et passer la main à l'adversaire.

<!-- Remplacez par vos captures d'écran (dossier screenshots/) -->
<!-- ![Capture d'écran](screenshots/main.png) -->

## Sommaire

- [Fonctionnalités](#fonctionnalités)
- [Utilisation](#utilisation)
- [Technologies](#technologies)
- [Installation](#installation)
- [Structure du projet](#structure-du-projet)
- [Ce que ce projet m'a appris](#ce-que-ce-projet-ma-appris)
- [Pistes d'amélioration](#pistes-damélioration)
- [Licence](#licence)

## Fonctionnalités

- **Deux zones de jeu** : l'écran est coupé en deux, la zone du joueur du haut est retournée à 180° pour jouer face à face.
- **Incrément de type Fischer** : un nombre de secondes est ajouté après chaque coup.
- **Joueur actif mis en valeur** : la zone dont le tour est en cours change de couleur.
- **Compteur de coups** pour chaque joueur.
- **Ajustement du temps en direct** : boutons `-30s`, `-15s`, `+15s`, `+30s` sur chaque zone.
- **Pause, reprise et remise à zéro** depuis la barre de contrôle.
- **Fin de partie** : le joueur qui n'a plus de temps voit s'afficher « YOU LOSE » sur fond rouge.
- **Réglages** : durée de départ (1, 3, 5 ou 10 minutes) et incrément (0, 2, 5 ou 10 secondes).
- **Réglages sauvegardés** : ils sont conservés après la fermeture de l'application.

## Utilisation

1. Ouvrez l'application : l'horloge est en pause.
2. Choisissez la cadence dans les **réglages** (icône engrenage).
3. Appuyez sur **▶ Play** pour lancer la partie : le temps du joueur 1 commence à s'écouler.
4. À la fin de son coup, chaque joueur **tape sa propre zone** pour passer la main.
5. Utilisez **⏸ Pause** pour arrêter l'horloge et **🔄 Replay** pour recommencer.

| Bouton | Action |
|---|---|
| ▶ | Démarrer / reprendre |
| ⏸ | Mettre en pause |
| 🔄 | Remettre les temps et les coups à zéro |
| ⚙️ | Ouvrir les réglages |

## Technologies

- [Kotlin](https://kotlinlang.org/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose) et Material 3
- Coroutines Kotlin (`LaunchedEffect`, `delay`) pour le décompte
- `SharedPreferences` pour la sauvegarde des réglages
- Gradle (Kotlin DSL)

## Installation

### Prérequis

- [Android Studio](https://developer.android.com/studio) récent
- Un appareil Android ou un émulateur (la version minimale d'Android est indiquée dans `app/build.gradle.kts`)

### Lancer le projet

```bash
git clone https://github.com/masta291/ChessClock.git
cd ChessClock
```

1. Ouvrez le dossier dans Android Studio.
2. Attendez la fin de la synchronisation Gradle.
3. Sélectionnez un appareil ou un émulateur, puis cliquez sur **Run ▶**.

## Structure du projet

```
ChessClock/
├── app/
│   └── src/main/
│       ├── java/com/example/chessclock/
│       │   ├── MainActivity.kt      # écran principal et composants
│       │   └── ui/theme/            # couleurs, typographie, thème
│       └── res/                     # icônes et ressources
├── gradle/
└── build.gradle.kts
```

Composants principaux de `MainActivity.kt` :

| Composant | Rôle |
|---|---|
| `ChessClockScreen` | Gère l'état (temps, coups, joueur actif, pause) et le décompte |
| `PlayerZone` | Affiche la zone d'un joueur (temps, coups, boutons de réglage rapide) |
| `ControleButton` | Bouton d'icône de la barre de contrôle |
| `TimeChip` | Petit bouton arrondi pour ajuster le temps |
| `OptionButton` | Bouton de choix dans le dialogue des réglages |
| `formatTime` | Convertit des secondes en `mm:ss` |

## Ce que ce projet m'a appris

- Construire une interface avec `Row`, `Column` et `Box`
- Gérer l'état avec `remember` et `mutableStateOf`
- Remonter l'état (*state hoisting*) et passer des fonctions en paramètre
- Lancer un décompte avec `LaunchedEffect` et les coroutines
- Calculer des valeurs à partir de l'état plutôt que de les stocker
- Sauvegarder des préférences avec `SharedPreferences`

## Pistes d'amélioration

- [ ] Bip sonore à chaque coup et à la fin du temps
- [ ] Noms des joueurs modifiables
- [ ] Garder l'écran allumé pendant une partie
- [ ] Conserver l'état lors de la rotation de l'écran
- [ ] Autres modes de cadence (Bronstein, délai simple, temps par coup)
- [ ] Traduction de l'interface

## Licence

Ce projet est distribué sous licence MIT. Ajoutez un fichier `LICENSE` à la racine du dépôt
(GitHub propose un modèle : **Add file > Create new file > nom `LICENSE`**).

## Auteur

**masta291** : [github.com/masta291](https://github.com/masta291)
