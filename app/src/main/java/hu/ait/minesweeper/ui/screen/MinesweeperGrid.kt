package hu.ait.minesweeper.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.Image
import androidx.compose.material3.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import hu.ait.minesweeper.R
import androidx.compose.ui.Modifier

@Composable
fun MinesweeperGrid(viewModel: MinesweeperViewModel = viewModel()) {
    var selectedCell by remember { mutableStateOf<Cell?>(null) }

    if (viewModel.gameOver.value || viewModel.gameWon.value) {
        GameOverScreen(viewModel)
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Shibali's Minesweeper 💣",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Column {
                for (row in viewModel.grid) {
                    Row {
                        for (cell in row) {
                            MinesweeperCell(cell) {
                                selectedCell = cell
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = { viewModel.resetGame() }) {
                Text("Reset Board")
            }
        }

        selectedCell?.let { cell ->
            ActionPopup(cell, viewModel) { selectedCell = null }
        }
    }
}

@Composable
fun MinesweeperCell(cell: Cell, onCellClick: () -> Unit) {
    val imageRes = when {
        cell.isFlagged -> R.drawable.flag
        cell.isRevealed && cell.isMine -> R.drawable.bomb // Shows bomb before game over
        cell.isRevealed && cell.nearbyMines > 0 -> // Display numbers
            when (cell.nearbyMines) {
                1 -> R.drawable.one
                2 -> R.drawable.two
                3 -> R.drawable.three
                4 -> R.drawable.four
                else -> R.drawable.selectedblock // Default if 5+ (unlikely in 5x5 grid)
            }
        cell.isRevealed -> R.drawable.selectedblock // No mine, no number
        else -> R.drawable.unselectedblock // Default closed block
    }

    Box(
        modifier = Modifier
            .size(50.dp)
            .border(2.dp, Color.Black, RoundedCornerShape(4.dp))
            .background(Color.Gray)
            .clickable { onCellClick() }
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Cell",
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun ActionPopup(cell: Cell, viewModel: MinesweeperViewModel, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Choose Action") },
        text = { Text("Do you want to try this field or place a flag?") },
        confirmButton = {
            Button(onClick = {
                viewModel.revealCell(cell.x, cell.y)
                onDismiss()
            }) {
                Text("Try Field")
            }
        },
        dismissButton = {
            Button(onClick = {
                viewModel.toggleFlag(cell.x, cell.y)
                onDismiss()
            }) {
                Text("Place Flag")
            }
        }
    )
}

@Composable
fun GameOverScreen(viewModel: MinesweeperViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (viewModel.gameWon.value) "🎉 You Win!" else "💀 Game Over!",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = if (viewModel.gameWon.value) Color.Green else Color.Red
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { viewModel.resetGame() }) {
            Text("Play Again")
        }
    }
}

