package goldenhuaji.me.fanchess

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import goldenhuaji.me.fanchess.GLog.logd
import goldenhuaji.me.fanchess.GLog.loge
import goldenhuaji.me.fanchess.Utils.getChessMapFileContent
import goldenhuaji.me.fanchess.Utils.getChessMapInfo
import goldenhuaji.me.fanchess.Utils.jsonToChessMap
import goldenhuaji.me.fanchess.diyChessMap.Window
import goldenhuaji.me.fanchess.settings.ConfigManager
import goldenhuaji.me.fanchess.ui.MyVFlowLayout
import java.awt.*
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import java.io.File
import java.io.FileNotFoundException
import java.io.FileWriter
import javax.swing.*
import kotlin.system.exitProcess


@SuppressWarnings("unchecked")
class Window(chessMap: Array<ChessItem?>, turn: Int? = null) {
    var frmIpa: JFrame? = null

    init {
        // 窗口框架
        frmIpa = JFrame()
//        frmIpa!!.isResizable = false
        frmIpa!!.title = "翻翻棋 - By GoldenHuaji"
        frmIpa!!.setBounds(600, 300, 1250, 600)
        frmIpa!!.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frmIpa!!.iconImage = ImageIcon(javaClass.classLoader.getResource("军师.png")).image
        // 面板1
        val panel = JPanel()
        frmIpa!!.contentPane.add(panel, BorderLayout.WEST)
        val btnNewWar = JButton("开始新的对战")
        btnNewWar.addActionListener {
            val tmp = JOptionPane.showOptionDialog(
                btnNewWar,
                "即将放弃本场对战并开启新的对战？",
                "提示",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                arrayOf("确定", "取消"),
                "取消"
            )
            if (tmp != JOptionPane.YES_OPTION) {
                return@addActionListener
            }
            try {
                (object : Thread() {
                    override fun run() {
                        super.run()
                        val file = File("${System.getenv("APPDATA")}\\FanChess\\backup.json")
                        if (file.exists()) {
                            file.delete()
                        }
                        frmIpa!!.dispose()
                    }
                }).start()
                ChessMain()
            } catch (e: Throwable) {
                loge(e)
            }
        }

        val tv = JLabel("现在应该红方行走")
        val tv2 = JLabel("")

        val chessPan = ChessRunner(frmIpa!!, chessMap, tv2, tv)
        val btnShowAllChessItem = JButton("翻开所有棋子")
        btnShowAllChessItem.addActionListener {
            try {
//                chessPan.addToBackTree()
                chessPan.chessMap.forEach {
                    it!!.shown = true
                }
            } catch (e: Throwable) {
                loge(e)
            }
        }

        panel.add(btnNewWar)
        panel.add(btnShowAllChessItem)
        panel.layout = MyVFlowLayout()
        val jPanel = JPanel()
        if (turn != null) {
            if (chessPan.getTurn() != turn) {
                chessPan.changeTurn()
            }
        }
        jPanel.add(chessPan)
        val btnUndo = JButton("悔棋")
        btnUndo.addActionListener {
            try {
                chessPan.undo()
            } catch (t: Throwable) {
                loge(t)
            }
        }
        jPanel.add(chessPan)
        val btnSave = JButton("保存对战")
        btnSave.addActionListener {
            try {
                chessPan.save(frmIpa!!)
            } catch (t: Throwable) {
                loge(t)
            }
        }
        val btnLoad = JButton("加载对战")
        btnLoad.addActionListener {
            try {
                val jfc = JFileChooser()
                jfc.fileFilter = Utils.MyFileFilter()
                if (jfc.showOpenDialog(frmIpa) == JFileChooser.CANCEL_OPTION) {
                    return@addActionListener
                }
                val file: File = jfc.selectedFile
                if (!file.exists()) {
                    file.createNewFile()
                }
                if (!file.canRead()) {
                    loge(FileNotFoundException("无法读取文件。请检查是否拥有权限。"))
                    return@addActionListener
                }
                chessPan.load(file)
            } catch (t: Throwable) {
                loge(t)
            }
        }
        val btnEdit = JButton("自定义布局")
        btnEdit.addActionListener {
            try {
                Window()
            } catch (t: Throwable) {
                loge(t)
            }
        }
        val btnSettings = JButton("设置")
        btnSettings.addActionListener {
            try {
                goldenhuaji.me.fanchess.settings.Window()
            } catch (t: Throwable) {
                loge(t)
            }
        }
        val btnRefresh = JButton("刷新")
        btnRefresh.addActionListener {
            try {
                chessPan.refresh()
            } catch (t: Throwable) {
                loge(t)
            }
        }
        panel.add(btnUndo)
        panel.add(btnSave)
        panel.add(btnLoad)
        panel.add(btnEdit)
        panel.add(btnRefresh)
        panel.add(btnSettings)
        panel.add(tv)
        panel.add(tv2)
        frmIpa!!.contentPane.add(jPanel, BorderLayout.CENTER)
        //这个最好放在最后，否则会出现视图问题。
        frmIpa!!.isVisible = true

        frmIpa!!.addWindowListener(object : WindowAdapter() {
            override fun windowOpened(e: WindowEvent?) {
                logd("窗口打开")
                val file = File("${System.getenv("APPDATA")}\\FanChess\\backup.json")
                if (!file.exists()) {
                    return
                }
                if (JOptionPane.showOptionDialog(
                        null,
                        "检测到您有未完成的战斗，是否继续？",
                        "消息",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        arrayOf("是", "否"),
                        "是"
                    ) == JOptionPane.YES_OPTION
                ) {
                    chessPan.load(file)
                }
            }

            override fun windowClosing(e: WindowEvent?) {
                try {
                    logd("窗口关闭")
                    // 保存设置
                    ConfigManager.get().save()
                    val array = Array(32) {
                        chessPan.chessMap[it]!!.shown
                    }
                    // 如果全部都没有翻开
                    if (array.contentEquals(Array(32) { false })) {
                        return
                    }
                    val file = File("${System.getenv("APPDATA")}\\FanChess\\backup.json")
                    logd(file.absolutePath)
                    if (!file.exists()) {
                        val dir: File
                        if (!(File("${System.getenv("APPDATA")}\\FanChess")).apply {
                                logd(this.absolutePath)
                                dir = this
                            }.exists()) {
                            dir.mkdir()
                        }
                        file.createNewFile()
                    }
                    val fileWriter = FileWriter(file)
                    fileWriter.write(getChessMapFileContent(chessPan.chessMap, chessPan.getTurn()!!))
                    fileWriter.flush()
                    fileWriter.close()
                } catch (t: Throwable) {
                    loge(t)
                    exitProcess(-1)
                }
            }
        })
    }
}