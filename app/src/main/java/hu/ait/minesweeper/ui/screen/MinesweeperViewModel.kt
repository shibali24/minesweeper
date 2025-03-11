package hu.ait.minesweeper.ui.screen

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlin.random.Random

data class Cell(
    val x: Int,
    val y: Int,
    var isMine: Boolean = false,
    var isRevealed: Boolean = false,
    var isFlagged: Boolean = false,
    var nearbyMines: Int = 0
)

class MinesweeperViewModel : ViewModel() {

    val gridSize = 5
    val mineCount = 3

    var grid = mutableStateListOf<MutableList<Cell>>()
    var gameOver = mutableStateOf(false)
    var gameWon = mutableStateOf(false)

    init {
        resetGame()
    }

    fun resetGame() {
        grid.clear()
        gameOver.value = false
        gameWon.value = false

        val newGrid = MutableList(gridSize) { y ->
            MutableList(gridSize) { x -> Cell(x, y) }
        }
        placeMines(newGrid)
        grid.addAll(newGrid)
    }

    private fun placeMines(grid: MutableList<MutableList<Cell>>) {
        val minePositions = mutableSetOf<Pair<Int, Int>>()
        while (minePositions.size < mineCount) {
            val x = Random.nextInt(gridSize)
            val y = Random.nextInt(gridSize)
            if (!grid[y][x].isMine) {
                grid[y][x].isMine = true
                minePositions.add(Pair(x, y))
            }
        }
        calculateNumbers(grid)
    }

    private fun calculateNumbers(grid: MutableList<MutableList<Cell>>) {
        val directions = listOf(-1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1)
        for (row in grid) {
            for (cell in row) {
                if (cell.isMine) continue
                cell.nearbyMines = directions.count { (dx, dy) ->
                    val nx = cell.x + dx
                    val ny = cell.y + dy
                    nx in 0 until gridSize && ny in 0 until gridSize && grid[ny][nx].isMine
                }
            }
        }
    }

    fun revealCell(x: Int, y: Int) {
        if (grid[y][x].isRevealed || grid[y][x].isFlagged || gameOver.value) return

        grid[y][x] = grid[y][x].copy(isRevealed = true)

        if (grid[y][x].isMine) {
            // Show bomb image for 1 second before game over
            CoroutineScope(Dispatchers.Main).launch {
                delay(1000) // 1 second delay
                gameOver.value = true
            }
        } else {
            checkWinCondition()
        }
    }

    fun toggleFlag(x: Int, y: Int) {
        if (grid[y][x].isRevealed || gameOver.value) return

        grid[y][x] = grid[y][x].copy(isFlagged = !grid[y][x].isFlagged)

        if (!grid[y][x].isMine && grid[y][x].isFlagged) {
            gameOver.value = true
        } else {
            checkWinCondition()
        }
    }

    private fun checkWinCondition() {
        val correctlyFlagged = grid.flatten().count { it.isMine && it.isFlagged }
        if (correctlyFlagged == mineCount) {
            gameWon.value = true
        }
    }
}
