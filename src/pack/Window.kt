package goldenhuaji.me.fanchess.pack

import goldenhuaji.me.fanchess.GLog
import goldenhuaji.me.fanchess.settings.ConfigManager
import goldenhuaji.me.fanchess.ui.MyVFlowLayout
import java.io.*
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

        panel.add(JButton("更改资源包").apply {
            addActionListener {
                Window()
                PackChooseWindow()
                dispose()
            }
            isFocusPainted = false
        })
        panel.add(JButton("获取示例资源包").apply {
            addActionListener {
                try {
                    val jfc = JFileChooser()
                    jfc.dialogTitle = "选择保存位置"
                    if (jfc.showSaveDialog(this@Window) == JFileChooser.CANCEL_OPTION) {
                        return@addActionListener
                    }
                    val file: File = jfc.selectedFile
                    if (!file.exists()) {
                        file.createNewFile()
                    }
                    if (!file.canWrite()) {
                        GLog.loge(FileNotFoundException("无法写入文件。请检查是否拥有权限。"))
                        return@addActionListener
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
                            return@addActionListener
                        }
                    }
                    val fileOutputStream = FileOutputStream(file)
                    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                    fileOutputStream.write(FileInputStream(File(ResLoader.loadRes("examplePack.zip")!!
                        .toURI())).readAllBytes())
                    fileOutputStream.flush()
                    fileOutputStream.close()
                    JOptionPane.showMessageDialog(null, "成功")
                } catch (t: KotlinNullPointerException) {
                    GLog.loge(t, "错误：找不到原文件")
                } catch (t: Throwable) {
                    GLog.loge(t)
                }
                isFocusPainted = false
            }
        })
        panel.add(JButton("如何制作？").apply {
            isFocusPainted = false
            addActionListener {
                JOptionPane.showMessageDialog(null,
                    """1. 文件目录如下，并且打成 Zip 压缩包:
                    | - 黑车.png
                    | - 黑将.png
                    | - 黑马.png
                    | - 黑炮.png
                    | - 黑士.png
                    | - 黑象.png
                    | - 黑卒.png
                    | - 红车.png
                    | - 红马.png
                    | - 红帅.png
                    | - 红炮.png
                    | - 红士.png
                    | - 红相.png
                    | - 红兵.png
                    | - 军师.png
                    | - 空棋.png
                    | - 棋盘.png
                    | - config.json
                    | 2. 关于 config.json: 
                    | 使用 JSON 格式，包含：desc（对材质的描述）、author、copyright
                    | 3. 更多教程请提取示例资源包，打开其 JSON 文件
                """.trimMargin())
            }
        })
        panel.add(JLabel("<html>把资源包放置在 ${System.getenv("APPDATA")}\\FanChess\\pack 来识别</html>"))
        this.add(panel)
        this.isVisible = true
    }
}