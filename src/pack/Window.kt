package goldenhuaji.me.fanchess.pack

import goldenhuaji.me.fanchess.GLog
import goldenhuaji.me.fanchess.Utils
import goldenhuaji.me.fanchess.settings.ConfigManager
import goldenhuaji.me.fanchess.ui.MyVFlowLayout
import java.io.File
import java.io.FileNotFoundException
import java.io.FileWriter
import javax.swing.*

class Window : JFrame() {
    var mgr: ConfigManager

    init {
        iconImage = ImageIcon(javaClass.classLoader.getResource("军师.png")).image
        title = "资源包"
        setBounds(600, 300, 150, 300)
        defaultCloseOperation = DISPOSE_ON_CLOSE
        mgr = ConfigManager.get()

        val panel = JPanel()
        panel.layout = MyVFlowLayout()

        panel.add(JButton("选择资源包").apply {
            addActionListener {
                val jfc = JFileChooser()
                jfc.fileFilter = Utils.PackFileFilter()
                if (jfc.showOpenDialog(this@Window) == JFileChooser.CANCEL_OPTION) {
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
                mgr.put(ConfigManager.ENABLED_PACK_PATH, file.absolutePath)
                Window()
                dispose()
            }
        })
        panel.add(JButton("改为默认").apply {
            addActionListener {
                mgr.remove(ConfigManager.ENABLED_PACK_PATH)
                Window()
                dispose()
            }
        })
        panel.add(JButton("如何制作？").apply {
            addActionListener {
                JOptionPane.showMessageDialog(null,
                """文件目录如下，并且打成 Zip 压缩包:
                    | 黑车.png
                    | 黑将.png
                    | 黑马.png
                    | 黑炮.png
                    | 黑士.png
                    | 黑象.png
                    | 黑卒.png
                    | 红车.png
                    | 红马.png
                    | 红帅.png
                    | 红炮.png
                    | 红士.png
                    | 红相.png
                    | 红兵.png
                    | 军师.png
                    | 空棋.png
                    | 棋盘.png
                """.trimMargin())
            }
        })
        panel.add(JLabel("<html>当前资源包：${if (ConfigManager.get().getString(ConfigManager.ENABLED_PACK_PATH) == null) "默认" else ConfigManager.get().getString(ConfigManager.ENABLED_PACK_PATH)}</html>"))
        this.add(panel)
        this.isVisible = true
    }
}