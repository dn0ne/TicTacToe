import kotlin.math.abs

fun main() {
    val grid = createGrid()
    printGrid(grid)

    var gameState = checkGameState(grid)
    var xTurn = true
    while (gameState == "Game not finished") {
        makeMove(
            if (xTurn) 'X' else 'O',
            grid
        )
        xTurn = !xTurn

        printGrid(grid)
        gameState = checkGameState(grid)
    }
    println(gameState)
}

fun createGrid(): List<MutableList<Char>> {
    val grid = mutableListOf<MutableList<Char>>()
    var idx = 0
    for (i in 0..2) {
        grid.add(mutableListOf())
        for (j in 0..2) {
            grid[i].add(' ')
        }
    }
    return grid
}

fun printGrid(grid: List<MutableList<Char>>) {
    println("---------")
    for (row in grid) {
        print("| ")
        for (ch in row) {
            print("$ch ")
        }
        println("|")
    }
    println("---------")
}

fun makeMove(symbol: Char, grid: List<MutableList<Char>>) {
    while (true) {
        var (y, x) = readln().trimIndent().split(Regex("\\s+")).map { it.toIntOrNull() }
        if (x == null || y == null) {
            println("You should enter numbers!")
            continue
        }

        x--
        y--

        if (x !in 0..2 || y !in 0..2) {
            println("Coordinates should be from 1 to 3!")
            continue
        }

        if(grid[y][x] != ' ') {
            println("This cell is occupied! Choose another one!")
            continue
        }

        grid[y][x] = symbol
        break
    }
}

fun countRows(symbol: Char, grid: List<MutableList<Char>>): Int {
    var rowsCount = 0

    var hRow = 0
    var vRow = 0
    var mainDiagRow = 0
    var sideDiagRow = 0
    for (i in grid.indices) {
        for (j in grid[i].indices) {
            if (grid[i][j] == symbol) {
                hRow++
            } else {
                hRow = 0
            }

            if (grid[j][i] == symbol) {
                vRow++
            } else {
                vRow = 0
            }

            if (i == j) {
                if (grid[i][i] == symbol) {
                    mainDiagRow++
                } else {
                    mainDiagRow = 0
                }

                if (grid[i][grid.lastIndex - i] == symbol) {
                    sideDiagRow++
                } else {
                    sideDiagRow = 0
                }
            }
        }
        rowsCount += hRow / 3 + vRow / 3 + mainDiagRow / 3 + sideDiagRow / 3
    }

    return rowsCount
}

fun checkGameState(grid: List<MutableList<Char>>): String {
    val xCount = grid.sumOf { list -> list.count { ch -> ch == 'X' } }
    val oCount = grid.sumOf { list -> list.count { ch -> ch == 'O' } }
    val emptyCount = grid.sumOf { list -> list.count { ch -> ch == ' ' } }

    val xRows = countRows('X', grid)
    val oRows = countRows('O', grid)

    return when {
        abs(xCount - oCount) > 1 || (xRows > 0 && oRows > 0) || xRows > 1 || oRows > 1 -> {
            println("xCount: $xCount, oCount: $oCount, xRows: $xRows, oRows: $oRows")
            "Impossible"
        }
        xRows == 0 && oRows == 0 -> {
            if (emptyCount == 0) "Draw"
            else "Game not finished"
        }
        xRows == 1 -> "X wins"
        oRows == 1 -> "O wins"
        else -> "Something went wrong :("
    }
}