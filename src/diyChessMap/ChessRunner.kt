package goldenhuaji.me.fanchess.diyChessMap

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import goldenhuaji.me.fanchess.ChessItem
import goldenhuaji.me.fanchess.GLog
import goldenhuaji.me.fanchess.GLog.logd
import goldenhuaji.me.fanchess.Utils
import goldenhuaji.me.fanchess.pack.ResLoader
import java.awt.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileWriter
import javax.swing.*

class ChessRunner(
    var chessMap: Array<ChessItem?>
) : JPanel(GridLayout(4, 8)) {
    private var bg: Image? = null
    var turn: Int? = 1

    init {
        bg = ImageIcon(ResLoader.loadRes("棋盘.png")).image
        doInit()
    }

    private fun doInit() {
        this.removeAll()
        for ((i, ele) in chessMap.withIndex()) {
            this.add(ele)
            if (ele!!.chessColor == 0) {
                ele.shown = true
            }
            ele.addActionListener btnClick@{
                JOptionPane.showOptionDialog(
                    null,
                    JPanel(BorderLayout()).also {
                        it.add(JLabel("选择棋子"), BorderLayout.NORTH)
                        it.add(JPanel(GridLayout(4, 8)).apply {
                            add(JLabel("红方："))
                            add(JButton("士").apply {
                                addActionListener {
                                    if (isFull(1, ChessItem.CHESS_TYPE_UI)) {
                                        JOptionPane.showOptionDialog(
                                            null,
                                            "错误：该类型数量已达到上限。",
                                            "错误",
                                            JOptionPane.OK_OPTION,
                                            JOptionPane.ERROR_MESSAGE,
                                            null,
                                            arrayOf("关闭"),
                                            "关闭"
                                        )
                                        return@addActionListener
                                    }
                                    ele.chessColor = 1
                                    ele.chessType = ChessItem.CHESS_TYPE_UI
                                }
                            })
                            add(JButton("相").apply {
                                addActionListener {
                                    if (isFull(1, ChessItem.CHESS_TYPE_XL)) {
                                        JOptionPane.showOptionDialog(
                                            null,
                                            "错误：该类型数量已达到上限。",
                                            "错误",
                                            JOptionPane.OK_OPTION,
                                            JOptionPane.ERROR_MESSAGE,
                                            null,
                                            arrayOf("关闭"),
                                            "关闭"
                                        )
                                        return@addActionListener
                                    }
                                    ele.chessColor = 1
                                    ele.chessType = ChessItem.CHESS_TYPE_XL
                                }
                            })
                            add(JButton("车").apply {
                                addActionListener {
                                    if (isFull(1, ChessItem.CHESS_TYPE_JU)) {
                                        JOptionPane.showOptionDialog(
                                            null,
                                            "错误：该类型数量已达到上限。",
                                            "错误",
                                            JOptionPane.OK_OPTION,
                                            JOptionPane.ERROR_MESSAGE,
                                            null,
                                            arrayOf("关闭"),
                                            "关闭"
                                        )
                                        return@addActionListener
                                    }
                                    ele.chessColor = 1
                                    ele.chessType = ChessItem.CHESS_TYPE_JU
                                }
                            })
                            add(JButton("马").apply {
                                addActionListener {
                                    if (isFull(1, ChessItem.CHESS_TYPE_MA)) {
                                        JOptionPane.showOptionDialog(
                                            null,
                                            "错误：该类型数量已达到上限。",
                                            "错误",
                                            JOptionPane.OK_OPTION,
                                            JOptionPane.ERROR_MESSAGE,
                                            null,
                                            arrayOf("关闭"),
                                            "关闭"
                                        )
                                        return@addActionListener
                                    }
                                    ele.chessColor = 1
                                    ele.chessType = ChessItem.CHESS_TYPE_MA
                                }
                            })
                            add(JButton("炮").apply {
                                addActionListener {
                                    if (isFull(1, ChessItem.CHESS_TYPE_PC)) {
                                        JOptionPane.showOptionDialog(
                                            null,
                                            "错误：该类型数量已达到上限。",
                                            "错误",
                                            JOptionPane.OK_OPTION,
                                            JOptionPane.ERROR_MESSAGE,
                                            null,
                                            arrayOf("关闭"),
                                            "关闭"
                                        )
                                        return@addActionListener
                                    }
                                    ele.chessColor = 1
                                    ele.chessType = ChessItem.CHESS_TYPE_PC
                                }
                            })
                            add(JButton("帅").apply {
                                addActionListener {
                                    if (isFull(1, ChessItem.CHESS_TYPE_JL)) {
                                        JOptionPane.showOptionDialog(
                                            null,
                                            "错误：该类型数量已达到上限。",
                                            "错误",
                                            JOptionPane.OK_OPTION,
                                            JOptionPane.ERROR_MESSAGE,
                                            null,
                                            arrayOf("关闭"),
                                            "关闭"
                                        )
                                        return@addActionListener
                                    }
                                    ele.chessColor = 1
                                    ele.chessType = ChessItem.CHESS_TYPE_JL
                                }
                            })
                            add(JButton("兵").apply {
                                addActionListener {
                                    if (isFull(1, ChessItem.CHESS_TYPE_BK)) {
                                        JOptionPane.showOptionDialog(
                                            null,
                                            "错误：该类型数量已达到上限。",
                                            "错误",
                                            JOptionPane.OK_OPTION,
                                            JOptionPane.ERROR_MESSAGE,
                                            null,
                                            arrayOf("关闭"),
                                            "关闭"
                                        )
                                        return@addActionListener
                                    }
                                    ele.chessColor = 1
                                    ele.chessType = ChessItem.CHESS_TYPE_BK
                                }
                            })
                            add(JLabel("黑方："))
                            add(JButton("士").apply {
                                addActionListener {
                                    if (isFull(2, ChessItem.CHESS_TYPE_UI)) {
                                        JOptionPane.showOptionDialog(
                                            null,
                                            "错误：该类型数量已达到上限。",
                                            "错误",
                                            JOptionPane.OK_OPTION,
                                            JOptionPane.ERROR_MESSAGE,
                                            null,
                                            arrayOf("关闭"),
                                            "关闭"
                                        )
                                        return@addActionListener
                                    }
                                    ele.chessColor = 2
                                    ele.chessType = ChessItem.CHESS_TYPE_UI
                                }
                            })
                            add(JButton("象").apply {
                                addActionListener {
                                    if (isFull(2, ChessItem.CHESS_TYPE_XL)) {
                                        JOptionPane.showOptionDialog(
                                            null,
                                            "错误：该类型数量已达到上限。",
                                            "错误",
                                            JOptionPane.OK_OPTION,
                                            JOptionPane.ERROR_MESSAGE,
                                            null,
                                            arrayOf("关闭"),
                                            "关闭"
                                        )
                                        return@addActionListener
                                    }
                                    ele.chessColor = 2
                                    ele.chessType = ChessItem.CHESS_TYPE_XL
                                }
                            })
                            add(JButton("车").apply {
                                addActionListener {
                                    if (isFull(2, ChessItem.CHESS_TYPE_JU)) {
                                        JOptionPane.showOptionDialog(
                                            null,
                                            "错误：该类型数量已达到上限。",
                                            "错误",
                                            JOptionPane.OK_OPTION,
                                            JOptionPane.ERROR_MESSAGE,
                                            null,
                                            arrayOf("关闭"),
                                            "关闭"
                                        )
                                        return@addActionListener
                                    }
                                    ele.chessColor = 2
                                    ele.chessType = ChessItem.CHESS_TYPE_JU
                                }
                            })
                            add(JButton("马").apply {
                                addActionListener {
                                    if (isFull(2, ChessItem.CHESS_TYPE_MA)) {
                                        JOptionPane.showOptionDialog(
                                            null,
                                            "错误：该类型数量已达到上限。",
                                            "错误",
                                            JOptionPane.OK_OPTION,
                                            JOptionPane.ERROR_MESSAGE,
                                            null,
                                            arrayOf("关闭"),
                                            "关闭"
                                        )
                                        return@addActionListener
                                    }
                                    ele.chessColor = 2
                                    ele.chessType = ChessItem.CHESS_TYPE_MA
                                }
                            })
                            add(JButton("炮").apply {
                                addActionListener {
                                    if (isFull(2, ChessItem.CHESS_TYPE_PC)) {
                                        JOptionPane.showOptionDialog(
                                            null,
                                            "错误：该类型数量已达到上限。",
                                            "错误",
                                            JOptionPane.OK_OPTION,
                                            JOptionPane.ERROR_MESSAGE,
                                            null,
                                            arrayOf("关闭"),
                                            "关闭"
                                        )
                                        return@addActionListener
                                    }
                                    ele.chessColor = 2
                                    ele.chessType = ChessItem.CHESS_TYPE_PC
                                }
                            })
                            add(JButton("将").apply {
                                addActionListener {
                                    if (isFull(2, ChessItem.CHESS_TYPE_JL)) {
                                        JOptionPane.showOptionDialog(
                                            null,
                                            "错误：该类型数量已达到上限。",
                                            "错误",
                                            JOptionPane.OK_OPTION,
                                            JOptionPane.ERROR_MESSAGE,
                                            null,
                                            arrayOf("关闭"),
                                            "关闭"
                                        )
                                        return@addActionListener
                                    }
                                    ele.chessColor = 2
                                    ele.chessType = ChessItem.CHESS_TYPE_JL
                                }
                            })
                            add(JButton("卒").apply {
                                addActionListener {
                                    if (isFull(2, ChessItem.CHESS_TYPE_BK)) {
                                        JOptionPane.showOptionDialog(
                                            null,
                                            "错误：该类型数量已达到上限。",
                                            "错误",
                                            JOptionPane.OK_OPTION,
                                            JOptionPane.ERROR_MESSAGE,
                                            null,
                                            arrayOf("关闭"),
                                            "关闭"
                                        )
                                        return@addActionListener
                                    }
                                    ele.chessColor = 2
                                    ele.chessType = ChessItem.CHESS_TYPE_BK
                                }
                            })
                            add(JLabel("特殊操作："))
                            add(JButton("删除").apply {
                                addActionListener {
                                    ele.chessColor = 0
                                    ele.chessType = Int.MAX_VALUE
                                }
                            })
                            add(JButton("设为未翻开").apply {
                                addActionListener {
                                    if (ele.chessColor == 0 || ele.chessType == Int.MAX_VALUE) {
                                        JOptionPane.showOptionDialog(
                                            null,
                                            "错误：不存在的棋子。",
                                            "错误",
                                            JOptionPane.OK_OPTION,
                                            JOptionPane.ERROR_MESSAGE,
                                            null,
                                            arrayOf("关闭"),
                                            "关闭"
                                        )
                                        return@addActionListener
                                    }
                                    ele.shown = false
                                }
                            })
                            add(JLabel(""))
                            add(JLabel(""))
                            add(JLabel(""))
                            add(JLabel(""))
                            add(JLabel(""))
                            add(JLabel("选中的棋子："))
                            add(JLabel(when (ele.chessColor) {
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
                            }))
                            add(JLabel(""))
                            add(JLabel(""))
                            add(JLabel(""))
                            add(JLabel(""))
                            add(JLabel(""))
                            add(JLabel(""))
                        }, BorderLayout.SOUTH)
                    },
                    "选择",
                    JOptionPane.OK_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    arrayOf("关闭"),
                    "关闭"
                )
            }
        }
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        //下面这行是为了背景图片可以跟随窗口自行调整大小，可以自己设置成固定大小
        g.drawImage(bg, 0, 0, width, height, this)
    }

    /**
     * @return 选中的文件
     */
    fun save(frmIpa: JFrame): File? {
        try {
            val jfc = JFileChooser()
            jfc.fileFilter = Utils.MyFileFilter()
            if (jfc.showSaveDialog(frmIpa) == JFileChooser.CANCEL_OPTION) {
                return null
            }
            val file: File = jfc.selectedFile
            if (!file.exists()) {
                file.createNewFile()
            }
            if (!file.canWrite()) {
                GLog.loge(FileNotFoundException("无法写入文件。请检查是否拥有权限。"))
                return null
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
                    return null
                }
            }
            val fileWriter = FileWriter(file)
            fileWriter.write(Utils.getChessMapFileContent(chessMap, turn ?: 1))
            fileWriter.flush()
            fileWriter.close()
            JOptionPane.showMessageDialog(null, "保存成功")
            return file
        } catch (t: Throwable) {
            GLog.loge(t)
            return null
        }
    }

    fun save(file: File) {
        try {
            if (!file.exists()) {
                file.createNewFile()
            }
            if (!file.canWrite()) {
                GLog.loge(FileNotFoundException("无法写入文件。请检查是否拥有权限。"))
                return
            }
            val fileWriter = FileWriter(file)
            fileWriter.write(Utils.getChessMapFileContent(chessMap, turn ?: 1))
            fileWriter.flush()
            fileWriter.close()
            JOptionPane.showMessageDialog(null, "保存成功")
        } catch (t: Throwable) {
            GLog.loge(t)
        }
    }

    fun load(file: File) {
        val str = file.readText()
        val obj = JSON.parseObject(str)
        chessMap = Utils.jsonToChessMap((obj["chessMap"] as JSONArray).toArray(arrayOf("")))
        turn = obj["turn"] as Int
        logd("DIY-Last chess shown is ${chessMap.last()!!.shown}")
        doInit()
        object:Thread(){
            override fun run() {
                super.run()
                JOptionPane.showMessageDialog(null, "加载成功")
            }
        }
    }

    fun changeTurn(): String {
        turn = if (turn == 1) 2 else 1
        return if (turn == 1) "红方先手中... " else "黑方先手中... "
    }

    private fun isFull(color: Int, type: Int): Boolean {
        val max = when (type) {
            ChessItem.CHESS_TYPE_BK -> 5
            ChessItem.CHESS_TYPE_JL -> 1
            else -> 2
        }
        var found = 0
        chessMap.forEach {
            if (it!!.chessColor == color && it.chessType == type) {
                found++
            }
        }
        if (found >= max) {
            return true
        }
        return false
    }
}