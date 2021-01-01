package goldenhuaji.me.fanchess

import java.awt.*
import java.io.PrintWriter

import java.io.StringWriter
import java.net.UnknownHostException
import javax.swing.*


object GLog {
    const val EXIT_TYPE_ERR = -1

    fun logd(msg: String) {
        println("FanFanChess Log/Debug: $msg")
    }

    fun logi(msg: String) {
        println("FanFanChess Log/Info: $msg")
    }

    fun loge(t: Throwable, msg: String = "") {
        println("FanFanChess Log/Error: ${getStackTraceString(t)}")
        JOptionPane.showOptionDialog(
            null,
            JPanel(BorderLayout()).also {
                it.add(JLabel("错误：程序运行时捕捉到错误如下。"), BorderLayout.NORTH)
                it.add(JScrollPane(TextArea("${getStackTraceString(t)}")), BorderLayout.SOUTH)
                if (msg.isNotEmpty()) {
                    it.add(JLabel("<html>$msg</html>"))
                }
            },
            "错误",
            JOptionPane.OK_OPTION,
            JOptionPane.ERROR_MESSAGE,
            null,
            arrayOf("确定"),
            "确定"
        )
    }

    fun getStackTraceString(tr: Throwable?): String {
        if (tr == null) {
            return ""
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        var t = tr
        while (t != null) {
            if (t is UnknownHostException) {
                return ""
            }
            t = t.cause
        }
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        tr.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }
}