package goldenhuaji.me.fanchess

import goldenhuaji.me.fanchess.GLog.logd
import goldenhuaji.me.fanchess.pack.ResLoader
import goldenhuaji.me.fanchess.settings.ConfigManager
import java.awt.Color
import java.awt.Insets
import javax.swing.BorderFactory
import javax.swing.ImageIcon
import javax.swing.JButton

class ChessItem : JButton() {
    var chessType = CHESS_TYPE_DEAD
        set(value) {
            field = value
            doInit()
        }
    var chessColor = CHESS_COLOR_DEAD
        set(value) {
            if (value == 0) {
                chessType = 114514
                shown = true
            }
            field = value
            doInit()
        }

    var icon: ImageIcon? = null

    var shown = false
        set(value) {
            field = value
            doInit()
        }

    var shit = ConfigManager.get().getBooleanNonNull(ConfigManager.SHIT_MODE)
        set(value) {
            field = value
            doInit()
        }

    init {
        isOpaque = true
        isContentAreaFilled = false
        border = BorderFactory.createLineBorder(Color(0f, 0f, 0f, 0f), 5)
        isFocusPainted = false
        margin = Insets(0, 0, 0, 0)
    }

    fun doInit() {
        try {
            ResLoader.loadRes("空棋.png")
        } catch (t: NoSuchFileException) {
            ConfigManager.get().remove(ConfigManager.ENABLED_PACK_PATH)
            ConfigManager.get().save()
        }
        // 更改图标
        if (!shown) {
            icon = ImageIcon(ResLoader.loadRes("空棋.png"))
        } else {
            if (shit) {
                icon = if (chessColor == 0) ImageIcon(ResLoader.loadRes("空白.png")) else ImageIcon(ResLoader.loadRes("军师.png"))
            } else {
                icon = when (chessColor) {
                    0 -> ImageIcon(ResLoader.loadRes("空白.png"))
                    1 -> {
                        when (chessType) {
                            1 -> ImageIcon(ResLoader.loadRes("红帅.png"))
                            2 -> ImageIcon(ResLoader.loadRes("红士.png"))
                            3 -> ImageIcon(ResLoader.loadRes("红相.png"))
                            4 -> ImageIcon(ResLoader.loadRes("红车.png"))
                            5 -> ImageIcon(ResLoader.loadRes("红马.png"))
                            6 -> ImageIcon(ResLoader.loadRes("红炮.png"))
                            7 -> ImageIcon(ResLoader.loadRes("红兵.png"))
                            else -> ImageIcon(ResLoader.loadRes("军师.png"))
                        }
                    }
                    2 -> {
                        when (chessType) {
                            1 -> ImageIcon(ResLoader.loadRes("黑将.png"))
                            2 -> ImageIcon(ResLoader.loadRes("黑士.png"))
                            3 -> ImageIcon(ResLoader.loadRes("黑象.png"))
                            4 -> ImageIcon(ResLoader.loadRes("黑车.png"))
                            5 -> ImageIcon(ResLoader.loadRes("黑马.png"))
                            6 -> ImageIcon(ResLoader.loadRes("黑炮.png"))
                            7 -> ImageIcon(ResLoader.loadRes("黑卒.png"))
                            else -> ImageIcon(ResLoader.loadRes("军师.png"))
                        }
                    }
                    else -> ImageIcon(ResLoader.loadRes("军师.png"))
                }
            }
        }
        this.setIcon(icon)
    }

    companion object {
        const val CHESS_TYPE_DEAD = 0
        const val CHESS_TYPE_JL = 1 // 将帅
        const val CHESS_TYPE_UI = 2 // 士
        const val CHESS_TYPE_XL = 3 // 象
        const val CHESS_TYPE_JU = 4 // 车
        const val CHESS_TYPE_MA = 5 // 吗
        const val CHESS_TYPE_PC = 6 // 炮
        const val CHESS_TYPE_BK = 7 // 兵卒

        const val CHESS_COLOR_DEAD = 0
        const val CHESS_COLOR_RED = 1 // 红方
        const val CHESS_COLOR_BLACK = 2 // 黑方
    }
}