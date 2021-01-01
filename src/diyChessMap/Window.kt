package goldenhuaji.me.fanchess.diyChessMap

import goldenhuaji.me.fanchess.ChessItem
import goldenhuaji.me.fanchess.GLog
import goldenhuaji.me.fanchess.Utils
import goldenhuaji.me.fanchess.ui.MyVFlowLayout
import java.awt.BorderLayout
import java.awt.event.WindowEvent
import java.io.File
import java.io.FileNotFoundException
import javax.swing.*

class Window {
    var frmIpa: JFrame? = null

    var fileToSaveAt: File? = null

    init {
        // 窗口框架
        frmIpa = JFrame()
        frmIpa!!.title = "翻翻棋 - 自定义棋局"
        frmIpa!!.setBounds(600, 300, 1250, 600)
        frmIpa!!.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frmIpa!!.iconImage = ImageIcon(javaClass.classLoader.getResource("军师.png")).image
        val chessPan = ChessRunner(Array(32) {
            ChessItem()
        })
        val leftPanel = JPanel().apply {
            add(JButton("保存").also {
                it.isFocusPainted = false
                it.addActionListener {
                    if (fileToSaveAt == null) {
                        val file = chessPan.save(frmIpa!!) ?: return@addActionListener
                        fileToSaveAt = file
                    } else {
                        chessPan.save(fileToSaveAt!!)
                    }
                }
            })
            add(JButton("另存为").also {
                it.isFocusPainted = false
                it.addActionListener {
                    val file = chessPan.save(frmIpa!!) ?: return@addActionListener
                    fileToSaveAt = file
                }
            })

            val btnChangeTurn = JButton(if (chessPan.turn == 1) "红方先手中... " else "黑方先手中... ").also { btn ->
                btn.addActionListener {
                    btn.text = chessPan.changeTurn()
                }
            }

            add(JButton("从文件加载").also {
                it.isFocusPainted = false
                it.addActionListener {
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
                            GLog.loge(FileNotFoundException("无法读取文件。请检查是否拥有权限。"))
                            return@addActionListener
                        }
                        chessPan.load(file)
                        fileToSaveAt = file
                        btnChangeTurn.text = if (chessPan.turn == 1) "红方先手中... " else "黑方先手中... "
                    } catch (t: Throwable) {
                        GLog.loge(t)
                    }
                }
            })
            add(btnChangeTurn)
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
        }
        leftPanel.layout = MyVFlowLayout()
        frmIpa!!.add(leftPanel, BorderLayout.WEST)
        // 面板1
        val panel = JPanel()
        panel.add(chessPan)
        frmIpa!!.contentPane.add(panel, BorderLayout.EAST)
        frmIpa!!.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
        //这个最好放在最后，否则会出现视图问题。
        frmIpa!!.isVisible = true
    }
}