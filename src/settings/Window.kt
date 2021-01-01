package goldenhuaji.me.fanchess.settings

import goldenhuaji.me.fanchess.GLog.loge
import goldenhuaji.me.fanchess.ui.MyVFlowLayout
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.io.File
import java.lang.NullPointerException
import java.lang.StringBuilder
import javax.swing.*

class Window : JFrame() {
    var mgr: ConfigManager? = null
    private val updateLog = mapOf(
        "1.0 Beta" to "+ 修复 悔棋\n+ 修复 炮能吃己方棋子\n+ 增加 存盘和复盘系统\n+ 增加 设置\n+ 增加 自动保存\n+ 增加 自定义棋局\n+ 改变 军师模式",
        "1.0 Alpha" to "+ 修复 炮能够跨过两个棋子到达空白处\n+ 添加 资源包",
        "1.0 Omega" to "+ 修复 4个有关悔棋的BUG\n+ 添加 刷新按钮\n+ 美化 排版",
        "1.1 Beta" to "+ 优化 界面\n+ 优化 资源包"
    )
    private val versionArray = arrayOf("1.1 Beta", "1.0 Omega", "1.0 Alpha", "1.0 Beta")

    init {
        title = "翻翻棋 - 设置"
        iconImage = ImageIcon(javaClass.classLoader.getResource("军师.png")).image
        setBounds(600, 300, 150, 300)
        defaultCloseOperation = DISPOSE_ON_CLOSE
        mgr = ConfigManager.get()
        if (mgr == null) {
            loge(NullPointerException("FUCK YOU"))
        }
        val panel = JPanel()
        panel.layout = MyVFlowLayout()

        panel.add(JButton("军师模式：${if (mgr!!.getBooleanNonNull(ConfigManager.SHIT_MODE)) "启用" else "禁用"}").apply {
            addActionListener {
                mgr!!.changeBoolean(ConfigManager.SHIT_MODE)
                text = "军师模式：${if (mgr!!.getBooleanNonNull(ConfigManager.SHIT_MODE)) "启用" else "禁用"}"
            }
            isFocusPainted = false
        })
        panel.add(JButton("资源包").apply {
            addActionListener {
                goldenhuaji.me.fanchess.pack.Window()
                dispose()
            }
            isFocusPainted = false
        })
        panel.add(JButton("更新日志").apply {
            isFocusPainted = false
            addActionListener {
//                JOptionPane.showMessageDialog(null, updateLog.last())
                if (JOptionPane.showOptionDialog(
                        null,
                        updateLog[versionArray.first()],
                        "更新日志",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        arrayOf("确定", "查看其它版本更新日志"),
                        "确定"
                    ) == JOptionPane.NO_OPTION
                ) {
                    JOptionPane.showMessageDialog(null, JPanel(BorderLayout()).also {
                        it.add(JScrollPane(JPanel(MyVFlowLayout()).apply {
                            for (i in versionArray.indices) {
                                val sb = StringBuilder()
                                sb.append("<html><body>")
                                sb.append(versionArray[i])
                                sb.append("<br>")
                                sb.append(updateLog[versionArray[i]]!!.replace("\n", "<br>"))
                                sb.append("<br><br>")
                                sb.append("</body></html>")
                                add(JLabel(sb.toString()))
                            }
                        }), BorderLayout.CENTER)
                    })
                }
            }
        })
        panel.add(JButton("清空临时文件").apply {
            addActionListener {
                File(System.getProperty("java.io.tmpdir") + "FanChess\\Tmp\\").deleteRecursively()
            }
            isFocusPainted = false
        })
        add(panel)
        isVisible = true
    }
}