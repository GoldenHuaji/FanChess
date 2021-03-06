package goldenhuaji.me.fanchess

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import goldenhuaji.me.fanchess.GLog.logd
import goldenhuaji.me.fanchess.GLog.loge
import goldenhuaji.me.fanchess.Utils.getChessMapFileContent
import goldenhuaji.me.fanchess.Utils.getChessMapInfo
import goldenhuaji.me.fanchess.Utils.jsonToChessMap
import goldenhuaji.me.fanchess.pack.ResLoader
import goldenhuaji.me.fanchess.settings.ConfigManager
import java.awt.Color
import java.awt.Image
import java.awt.Graphics
import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.util.*
import javax.swing.*
import kotlin.collections.HashMap

import java.io.File
import java.io.FileNotFoundException
import java.io.FileWriter
import java.lang.NullPointerException
import javax.swing.JFileChooser
import javax.swing.border.Border

class ChessRunner(
    var chessMap: Array<ChessItem?>,
    private val tv: JLabel,
    private val tvShowTurn: JLabel,
) : JPanel(GridLayout(4, 8)) {

    private var bg: Image? = null
    private var checkedChessItemIndex: Int? = null
    private var turn: Int? = ChessItem.CHESS_COLOR_RED
        set(value) {
            field = value
            if (turn == 1) {
                tvShowTurn.text = "现在应该红方行走"
            } else {
                tvShowTurn.text = "现在应该黑方行走"
            }
        }
    private val chessMapUndoTree: MutableList<HashMap<String, Any>> = mutableListOf()
    private var chessMapBackup: Array<ChessItem?> = chessMap.clone()

    private val noBorder: Border = BorderFactory.createLineBorder(Color(0f, 0f, 0f, 0f), 5)
    private val redBorder: Border = BorderFactory.createLineBorder(Color.RED, 5)
    private val blueBorder: Border = BorderFactory.createLineBorder(Color.BLUE, 5)

    init {
        bg = ImageIcon(ResLoader.loadRes("棋盘.png")).image
        doInit()
    }

    fun getTurn(): Int? {
        return turn
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        //下面这行是为了背景图片可以跟随窗口自行调整大小，可以自己设置成固定大小
        g.drawImage(bg, 0, 0, width, height, this)
    }

    private fun doInit() {
        this.removeAll()
        for ((i, ele) in chessMap.withIndex()) {
            this.add(ele!!)
            val action = addActionListener@{ _: ActionEvent ->
                try {
                    if (chessMap.first() == null) {
                        loge(NullPointerException("I don't know what happened.But something's wrong."),
                            "你是怎么做到把游戏搞崩溃的？务必把你是怎么做到的告诉开发者。")
                    }
                    // 黑方红方交替走棋
                    if (ele.shown && ele.chessColor != turn && checkedChessItemIndex == null) {
                        return@addActionListener
                    }

                    // 没有显示时调用
                    if (!ele.shown) {
                        if (checkedChessItemIndex == null) {
                            // 如果选中了棋子
                            if (checkedChessItemIndex != null) {
                                checkedChessItemIndex = null
                                chessMap.forEachIndexed { i, _ ->
                                    i.changeBorder(ChessItem.BORDER_TYPE_NONE)
                                }
                                return@addActionListener
                            }
                            checkedChessItemIndex = null
                            addToBackTree()
                            ele.shown = true
                            i.changeBorder(ChessItem.BORDER_TYPE_EAT)
                            changeTurn()
                            return@addActionListener
                        }
                        if (chessMap[checkedChessItemIndex!!]!!.chessType != ChessItem.CHESS_TYPE_PC) {
                            // 如果选中了棋子
                            if (checkedChessItemIndex != null) {
                                checkedChessItemIndex = null
                                chessMap.forEachIndexed { i, _ ->
                                    i.changeBorder(ChessItem.BORDER_TYPE_NONE)
                                }
                                return@addActionListener
                            }
                            checkedChessItemIndex = null
                            addToBackTree()
                            ele.shown = true
                            i.changeBorder(ChessItem.BORDER_TYPE_EAT)
                            changeTurn()
                            return@addActionListener
                        }
                    }

                    // 曾经没有选中棋子时调用
                    if (checkedChessItemIndex == null) {
                        if (chessMap[i]!!.chessColor == 0) {
                            return@addActionListener
                        }
                        checkedChessItemIndex = i
                        i.changeBorder(ChessItem.BORDER_TYPE_CHECK)
                        return@addActionListener
                    }

                    // 连输点击两个相同棋子时调用
                    if (chessMap[i]!! == chessMap[checkedChessItemIndex!!]) {
                        checkedChessItemIndex = null
                        i.changeBorder(ChessItem.BORDER_TYPE_NONE)
                        return@addActionListener
                    }

                    // 对于炮的特殊处理
                    if (chessMap[checkedChessItemIndex!!]!!.chessType == ChessItem.CHESS_TYPE_PC) {
                        // 选中的棋子时同一方的
                        if (ele.chessColor == chessMap[checkedChessItemIndex!!]!!.chessColor && ele.shown) {
                            return@addActionListener
                        }
                        // 在同一行时
                        if (i / 8 == checkedChessItemIndex!! / 8) {
                            // 普通移动
                            if (i + 1 == checkedChessItemIndex || i - 1 == checkedChessItemIndex) {
                                if (ele.chessColor == 0) {
                                    eat(chessMap[checkedChessItemIndex!!]!!, ele)
                                    return@addActionListener
                                } else {
                                    return@addActionListener
                                }
                            }
                            if (ele.chessColor == 0) {
                                return@addActionListener
                            }
                            val found: MutableList<ChessItem?> = mutableListOf()
                            for (j in checkedChessItemIndex!!..i) {
                                if (chessMap[j]!!.chessColor != 0) {
                                    found.add(chessMap[j])
                                }
                            }
                            if (found.size == 0) {
                                for (j in i..checkedChessItemIndex!!) {
                                    if (chessMap[j]!!.chessColor != 0) {
                                        found.add(chessMap[j])
                                    }
                                }
                            }
                            if (found.size != 3) {
                                return@addActionListener
                            }
                            if (!ele.shown) {
                                tv.text = "${
                                    when (ele.chessColor) {
                                        0 -> "空白"
                                        1 -> {
                                            when (ele.chessType) {
                                                1 -> "红帅"
                                                2 -> "红士"
                                                3 -> "红相"
                                                4 -> "红车"
                                                5 -> "红马"
                                                6 -> "红炮"
                                                7 -> "红兵"
                                                else -> "军师"
                                            }
                                        }
                                        2 -> {
                                            when (ele.chessType) {
                                                1 -> "黑将"
                                                2 -> "黑士"
                                                3 -> "黑象"
                                                4 -> "黑车"
                                                5 -> "黑马"
                                                6 -> "黑炮"
                                                7 -> "黑卒"
                                                else -> "军师"
                                            }
                                        }
                                        else -> "军师"
                                    }
                                }被炮吃掉了。"
                            }
                            eat(chessMap[checkedChessItemIndex!!]!!, ele)
                        } else if (i % 8 == checkedChessItemIndex!! % 8) {
                            if (i + 8 == checkedChessItemIndex || i - 8 == checkedChessItemIndex) {
                                // 普通移动
                                if (ele.chessColor == 0) {
                                    eat(chessMap[checkedChessItemIndex!!]!!, ele)
                                    return@addActionListener
                                } else {
                                    return@addActionListener
                                }
                            }
                            if (ele.chessColor == 0) {
                                return@addActionListener
                            }
                            val found: MutableList<ChessItem?> = mutableListOf()
                            val index: MutableList<Int> = mutableListOf()
                            for (j in checkedChessItemIndex!! / 8..i / 8) {
                                index.add(j * 8 + checkedChessItemIndex!! % 8)
                            }
                            if (found.size == 0) {
                                for (j in i / 8..checkedChessItemIndex!! / 8) {
                                    index.add(j * 8 + checkedChessItemIndex!! % 8)
                                }
                            }
                            for (j in index) {
                                if (chessMap[j]!!.chessColor != 0) {
                                    found.add(chessMap[j])
                                }
                            }
                            if (found.size != 3) {
                                return@addActionListener
                            }
                            if (!ele.shown) {
                                tv.text = "${
                                    when (ele.chessColor) {
                                        0 -> "空白"
                                        1 -> {
                                            when (ele.chessType) {
                                                1 -> "红帅"
                                                2 -> "红士"
                                                3 -> "红相"
                                                4 -> "红车"
                                                5 -> "红马"
                                                6 -> "红炮"
                                                7 -> "红兵"
                                                else -> "军师"
                                            }
                                        }
                                        2 -> {
                                            when (ele.chessType) {
                                                1 -> "黑将"
                                                2 -> "黑士"
                                                3 -> "黑象"
                                                4 -> "黑车"
                                                5 -> "黑马"
                                                6 -> "黑炮"
                                                7 -> "黑卒"
                                                else -> "军师"
                                            }
                                        }
                                        else -> "军师"
                                    }
                                }被炮吃掉了。"
                            }
                            eat(chessMap[checkedChessItemIndex!!]!!, ele)
                        }
                        return@addActionListener
                    }

                    // 选中的棋子时同一方的
                    if (ele.chessColor == chessMap[checkedChessItemIndex!!]!!.chessColor) {
                        return@addActionListener
                    }

                    // 兵卒吃将帅
                    if (ele.chessType == ChessItem.CHESS_TYPE_JL && chessMap[checkedChessItemIndex!!]!!.chessType == ChessItem.CHESS_TYPE_BK) {
                        eat(chessMap[checkedChessItemIndex!!]!!, ele)
                        return@addActionListener
                    } else if (ele.chessType == ChessItem.CHESS_TYPE_BK && chessMap[checkedChessItemIndex!!]!!.chessType == ChessItem.CHESS_TYPE_JL) {
                        return@addActionListener
                    }

                    // 选中棋子无法吃掉
                    if (ele.chessType < chessMap[checkedChessItemIndex!!]!!.chessType) {
                        return@addActionListener
                    }

                    // 在同一行时
                    if (i / 8 == checkedChessItemIndex!! / 8) {
                        // 相邻时
                        if (i + 1 == checkedChessItemIndex || i - 1 == checkedChessItemIndex) {
                            eat(chessMap[checkedChessItemIndex!!]!!, ele)
                            return@addActionListener
                        }

                        // 在同一列时
                    } else if (i % 8 == checkedChessItemIndex!! % 8) {
                        // 相邻时
                        if (i + 8 == checkedChessItemIndex || i - 8 == checkedChessItemIndex) {
                            eat(chessMap[checkedChessItemIndex!!]!!, ele)
                            return@addActionListener
                        }

                        // 点击一个不能吃到的棋子时调用
                    } else {
                        checkedChessItemIndex!!.changeBorder(ChessItem.BORDER_TYPE_NONE)
                        checkedChessItemIndex = null
                        return@addActionListener
                    }
                } catch (t: Throwable) {
                    loge(t)
                }
            }
            ele.addActionListener {
                action.invoke(it)
                checkWin(chessMap)
            }
        }
        refresh()
    }

    @Suppress("UNCHECKED_CAST")
    fun undo() {
        try {
            val tmp = jsonToChessMap(chessMapUndoTree[chessMapUndoTree.lastIndex]["chessMap"]!! as Array<String>)
            chessMap = tmp.clone()
            turn = chessMapUndoTree[chessMapUndoTree.lastIndex]["turn"]!! as Int
            chessMapUndoTree.removeAt(chessMapUndoTree.lastIndex)
        } catch (e: IndexOutOfBoundsException) {
            loge(e, "您是否没有走棋？")
            return
        }
        doInit()
        refresh()
    }

    fun refresh() {
        chessMap.forEach {
            it!!.shit = ConfigManager.get().getBooleanNonNull(ConfigManager.SHIT_MODE)
        }
        this.validate()
        this.repaint()
    }

    /**
     * @param eater 发的吃食的子
     * @param eaten 被吃的子
     *
     */
    private fun eat(eater: ChessItem, eaten: ChessItem) {
        addToBackTree()
        changeTurn()
        // 清除蓝色边框
        chessMap.forEachIndexed { i, j ->
            when (j!!.border) {
                blueBorder -> {
                    i.changeBorder(ChessItem.BORDER_TYPE_NONE)
                }
            }
        }
        eaten.chessType = eater.chessType
        eaten.chessColor = eater.chessColor
        eaten.shown = eater.shown
        eater.chessColor = 0
        eater.chessType = Int.MAX_VALUE
        eater.border = blueBorder
        eaten.border = blueBorder
        checkedChessItemIndex = null
    }

    /**
     * 更改边框
     */
    private fun changeBorder(itemIndex: Int, type: Int) {
        if (itemIndex > 32 - 1 || itemIndex < 1 - 1) {
            return
        }
        when (type) {
            ChessItem.BORDER_TYPE_EAT -> {
                // 清除蓝色边框
                chessMap.forEachIndexed { i, j ->
                    when (j!!.border) {
                        blueBorder -> {
                            i.changeBorder(ChessItem.BORDER_TYPE_NONE)
                        }
                    }
                }
                chessMap[itemIndex]!!.border = blueBorder
            }
            ChessItem.BORDER_TYPE_CHECK -> chessMap[itemIndex]!!.border = redBorder
            ChessItem.BORDER_TYPE_NONE -> chessMap[itemIndex]!!.border = noBorder
        }
    }

    @JvmName("changeBorder1")
    private fun Int.changeBorder(type: Int) {
        changeBorder(this, type)
    }

    private fun checkWin(chessMap: Array<ChessItem?>) {
        isRedWin(chessMap)
        isBlackWin(chessMap)
    }

    private fun isRedWin(chessMap: Array<ChessItem?>) {
        for (j in chessMap) {
            if (j!!.chessColor == 0) {
                continue
            }
            if (j.chessColor != 1) {
                return
            }
        }
        JOptionPane.showOptionDialog(
            this,
            "恭喜！红方取得胜利！",
            "完成",
            JOptionPane.OK_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            arrayOf("确定"),
            "确定"
        )
    }

    private fun isBlackWin(chessMap: Array<ChessItem?>) {
        for (j in chessMap) {
            if (j!!.chessColor == 0) {
                continue
            }
            if (j.chessColor != 2) {
                return
            }
        }
        JOptionPane.showOptionDialog(
            this,
            "恭喜！黑方取得胜利！",
            "完成",
            JOptionPane.OK_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            arrayOf("确定"),
            "确定"
        )
    }

    fun changeTurn() {
        turn = if (turn == 1) 2 else 1
        chessMapBackup = chessMap.clone()
    }

    fun addToBackTree() {
        val map = hashMapOf<String, Any>().also {
            it["turn"] = turn!!
            it["chessMap"] = getChessMapInfo(chessMap)
        }
        if (chessMapUndoTree.size != 0 && map == chessMapUndoTree.last()) {
            return
        }
        chessMapUndoTree.add(map)
    }

    fun save(frmIpa: JFrame) {
        try {
            val jfc = JFileChooser()
            jfc.fileFilter = Utils.MyFileFilter()
            if (jfc.showSaveDialog(frmIpa) == JFileChooser.CANCEL_OPTION) {
                return
            }
            val file: File = jfc.selectedFile
            if (!file.exists()) {
                file.createNewFile()
            }
            if (!file.canWrite()) {
                loge(FileNotFoundException("无法写入文件。请检查是否拥有权限。"))
                return
            }
            if (file.length() != 0L) {
                val tmp = JOptionPane.showOptionDialog(
                    null,
                    "文件已存在，是否覆盖它？",
                    "提示",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    arrayOf("确定", "取消"),
                    "取消"
                )
                if (tmp != JOptionPane.YES_OPTION) {
                    return
                }
            }
            val fileWriter = FileWriter(file)
            fileWriter.write(getChessMapFileContent(chessMap, turn!!))
            fileWriter.flush()
            fileWriter.close()
            tv.text = "保存成功"
        } catch (t: Throwable) {
            loge(t)
        }
    }

    @Suppress("UNUSED")
    fun save(file: File) {
        try {
            if (!file.exists()) {
                file.createNewFile()
            }
            if (!file.canWrite()) {
                loge(FileNotFoundException("无法写入文件。请检查是否拥有权限。"))
                return
            }
            val fileWriter = FileWriter(file)
            fileWriter.write(getChessMapFileContent(chessMap, turn ?: 1))
            fileWriter.flush()
            fileWriter.close()
        } catch (t: Throwable) {
            loge(t)
        }
    }

    fun load(file: File, hint: Boolean = true) {
        val str = file.readText()
        val obj = JSON.parseObject(str)
        chessMap = jsonToChessMap((obj["chessMap"] as JSONArray).toArray(arrayOf("")))
        turn = obj["turn"] as Int
        logd("MAIN-Last chess shown is ${chessMap.last()!!.shown}")
        doInit()
        if (hint) tv.text = "加载成功"
    }
}