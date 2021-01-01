package goldenhuaji.me.fanchess.pack

import com.alibaba.fastjson.JSONObject
import goldenhuaji.me.fanchess.GLog
import goldenhuaji.me.fanchess.GLog.logd
import goldenhuaji.me.fanchess.Utils
import goldenhuaji.me.fanchess.getPackInfo
import goldenhuaji.me.fanchess.settings.ConfigManager
import goldenhuaji.me.fanchess.ui.MyVFlowLayout
import java.awt.*
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.io.File
import java.io.FileNotFoundException
import javax.swing.*

class PackChooseWindow : JFrame() {
    var mgr: ConfigManager

    init {
        iconImage = ImageIcon(javaClass.classLoader.getResource("军师.png")).image
        title = "资源包"
        setBounds(600, 300, 150, 300)
        defaultCloseOperation = DISPOSE_ON_CLOSE
        mgr = ConfigManager.get()

        val panel = JPanel()
        panel.add(JPanel(MyVFlowLayout()).apply {
            val packPath = File("${System.getenv("APPDATA")}\\FanChess\\pack")
            if (!packPath.exists()) {
                val dir: File
                if (!(File("${System.getenv("APPDATA")}\\FanChess")).apply {
                        logd(this.absolutePath)
                        dir = this
                    }.exists()) {
                    dir.mkdir()
                }
                packPath.mkdir()
            }
            val packParent = object : JPanel(MyVFlowLayout()) {
                private lateinit var myParent: Component
                fun refresh(parent: Component) {
                    removeAll()
                    myParent = parent
                    add(JButton("打开资源包文件夹").also {
                        it.addActionListener {
                            Runtime.getRuntime().exec("explorer ${System.getenv("APPDATA")}\\FanChess\\pack")
                        }
                        it.isFocusPainted = false
                    })
                    add(newPackPanel(null, null, parent, true))
                    val files = packPath.listFiles() ?: return
                    files.forEach {
                        if (it.isDirectory || !it.name.endsWith(".zip")) {
                            return@forEach
                        }
                        val packInfo = it.getPackInfo() ?: return@forEach
                        add(newPackPanel(it, packInfo, parent))
                    }
                }

                override fun validate() {
                    refresh(myParent)
                    super.validate()
                }
            }
            contentPane = JScrollPane(packParent.apply list@{ refresh(this@list) })
        })
        this.add(panel)
        this.isVisible = true
    }

    private fun newPackPanel(it: File?, packInfo: JSONObject?, parent: Component, default: Boolean = false): JPanel {
        var name = it?.name
        var desc = packInfo?.get("desc")
        var path: String? = it?.absolutePath
        if (default || it == null || packInfo == null) {
            name = "默认资源包"
            desc = "系统默认"
            path = null
        }
        val panel = object: JPanel() {
            override fun validate() {
                super.validate()
                background = if (mgr.getString(ConfigManager.ENABLED_PACK_PATH) == path) {
                    Color(163, 184, 255)
                } else {
                    Color(0f, 0f, 0f, 0f)
                }
            }
        }
        return panel.apply panel@{
            background = if (mgr.getString(ConfigManager.ENABLED_PACK_PATH) == path) {
                Color(163, 184, 255)
            } else {
                Color(0f, 0f, 0f, 0f)
            }
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            add(JLabel("<html>$name</html>"))
            add(JLabel("<html>$desc</html>"))
            addMouseMotionListener(object : MouseMotionListener {
                override fun mouseDragged(e: MouseEvent?) {
                    // nothing
                }

                override fun mouseMoved(e: MouseEvent?) {
                    if (mgr.getString(ConfigManager.ENABLED_PACK_PATH) != path) {
                        background = Color(163, 184, 204)
                    }
                }
            })
            addMouseListener(object : MouseListener {
                override fun mouseClicked(e: MouseEvent?) {
                    if (default) {
                        mgr.remove(ConfigManager.ENABLED_PACK_PATH)
                    } else {
                        mgr.put(ConfigManager.ENABLED_PACK_PATH, it!!.absolutePath)
                    }
                    background = Color(163, 184, 255)
                    this@panel.validate()
                    this@panel.repaint()
                    parent.validate()
                    parent.repaint()
                }

                override fun mousePressed(e: MouseEvent?) {
                    // nothing
                }

                override fun mouseReleased(e: MouseEvent?) {
                    // nothing
                }

                override fun mouseEntered(e: MouseEvent?) {
                    // nothing
                }

                override fun mouseExited(e: MouseEvent?) {
                    if (mgr.getString(ConfigManager.ENABLED_PACK_PATH) != path) {
                        background = Color(0f, 0f, 0f, 0f)
                        parent.validate()
                        parent.repaint()
                    }
                }
            })
        }
    }
}