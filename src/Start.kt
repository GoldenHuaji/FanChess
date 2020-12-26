package goldenhuaji.me.fanchess

import goldenhuaji.me.fanchess.GLog.loge
import kotlin.random.Random

fun main(args: Array<String>) {
    try {
        ChessMain()
    } catch (t: Throwable) {
        loge(t)
    }
}

class ChessMain {
    private val sChessMap = arrayOfNulls<ChessItem>(32)

    init {
        // 此变量用于设置棋子颜色
        val chessColorMap = mutableListOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2)
        val blackChessTypeMap = mutableListOf(ChessItem.CHESS_TYPE_JL, ChessItem.CHESS_TYPE_UI, ChessItem.CHESS_TYPE_UI,
                ChessItem.CHESS_TYPE_XL, ChessItem.CHESS_TYPE_XL, ChessItem.CHESS_TYPE_JU, ChessItem.CHESS_TYPE_JU, ChessItem.CHESS_TYPE_MA, ChessItem.CHESS_TYPE_MA,
                ChessItem.CHESS_TYPE_PC, ChessItem.CHESS_TYPE_PC, 7, 7, 7, 7, 7)
        val redChessTypeMap = mutableListOf(ChessItem.CHESS_TYPE_JL, ChessItem.CHESS_TYPE_UI, ChessItem.CHESS_TYPE_UI,
                ChessItem.CHESS_TYPE_XL, ChessItem.CHESS_TYPE_XL, ChessItem.CHESS_TYPE_JU, ChessItem.CHESS_TYPE_JU, ChessItem.CHESS_TYPE_MA, ChessItem.CHESS_TYPE_MA,
                ChessItem.CHESS_TYPE_PC, ChessItem.CHESS_TYPE_PC, 7, 7, 7, 7, 7)
        val random = Random

        // 随机颜色
        for ((i, ele) in sChessMap.withIndex()) {
            sChessMap[i] = ChessItem()
            if (sChessMap[i] == null) {
                val e: Throwable = NullPointerException("I don't know why ChessItem is still null.")
                GLog.loge(e)
            } else {
                val index = random.nextInt(0, chessColorMap.size)
                sChessMap[i]!!.chessColor = chessColorMap[index]
                // 删除已经选过的颜色
                chessColorMap.removeAt(index)
            }
        }

        // 随机类型
        for ((i, ele) in sChessMap.withIndex()) {
            if (ele == null) {
                val e: Throwable = NullPointerException("I don't know why ChessItem is still null.")
                GLog.loge(e)
            } else {
                when (ele.chessColor) {
                    1 -> {
                        val index = random.nextInt(0, blackChessTypeMap.size)
                        ele.chessType = blackChessTypeMap[index]
                        // 删除已经选过的棋子
                        blackChessTypeMap.removeAt(index)
                    }
                    2 -> {
                        val index = random.nextInt(0, redChessTypeMap.size)
                        ele.chessType = redChessTypeMap[index]
                        // 删除已经选过的棋子
                        redChessTypeMap.removeAt(index)
                    }
                }
                // 做初始化
                ele.doInit()
            }
        }
        // 显示窗口
        Window(sChessMap)
    }
}