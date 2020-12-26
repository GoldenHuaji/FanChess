package goldenhuaji.me.fanchess.settings

import goldenhuaji.me.fanchess.GLog.loge
import java.io.File
import java.lang.NullPointerException
import javax.swing.*

class Window : JFrame(){
    var mgr: ConfigManager? = null
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
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        panel.add(JButton("军师模式：${if (mgr!!.getBooleanNonNull(ConfigManager.SHIT_MODE)) "启用" else "禁用"}").apply {
            addActionListener {
                mgr!!.changeBoolean(ConfigManager.SHIT_MODE)
                text = "军师模式：${if (mgr!!.getBooleanNonNull(ConfigManager.SHIT_MODE)) "启用" else "禁用"}"
            }
        })
        panel.add(JButton("资源包").apply {
            addActionListener {
                TODO("资源包")
            }
        })
        panel.add(JButton("更新日志").apply {
            addActionListener {
                JOptionPane.showMessageDialog(null, "+ 修复 悔棋\n+ 修复 炮能吃己方棋子\n+ 增加 存盘和复盘系统\n+ 增加 设置\n+ 增加 自动保存\n+ 增加 自定义棋局\n" +
                        "+ 改变 军师模式")
            }
        })

        add(panel)

        isVisible = true
    }
}