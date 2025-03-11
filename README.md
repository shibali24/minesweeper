Minesweeper 🏴‍☠️💣
A classic Minesweeper game built using Jetpack Compose in Kotlin!

📌 Overview
This project is a fully functional Minesweeper game, featuring a 5x5 grid with 3 randomly placed mines. Players must clear the board by revealing safe cells and marking mines with flags. The game ends if a mine is clicked or if all mines are correctly flagged.

🎮 Features & Gameplay
🟢 Basic Gameplay
5x5 grid with 3 mines (randomly placed).
Game starts automatically when launched.
Clicking on a cell prompts an action choice:
Try a Field → Reveals the cell.
Place a Flag → Marks a potential mine.
🔢 Number Display (Mine Detection)
If a revealed cell is safe, it displays a number showing how many mines are nearby.
If there are no nearby mines, the cell remains blank.
💀 Game Over Conditions
If a player clicks a mine, it briefly shows the bomb before displaying the Game Over screen.
If a flag is placed incorrectly, the game ends.
🏆 Winning the Game
If all mines are correctly flagged, the game displays a “You Win” screen.
🔄 Reset & Replay
A "Reset Board" button allows players to start over at any time.
