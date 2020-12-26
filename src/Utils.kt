package goldenhuaji.me.fanchess

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.util.TypeUtils.castToJavaBean
import java.util.HashMap


object Utils {
    fun getChessMapInfo(chessMap: Array<ChessItem?>): Array<String> = Array(32) {
        val fields = HashMap<String, Any>()
        fields["chessType"] = chessMap[it]!!.chessType
        fields["chessColor"] = chessMap[it]!!.chessColor
        fields["shown"] = chessMap[it]!!.shown
        JSON.toJSONString(fields)
    }

    fun jsonToChessMap(str: Array<String>): Array<ChessItem?> = Array(32) { index ->
        val obj = JSON.parseObject(str[index])
        castToJavaBean(obj, ChessItem::class.java)
    }

    fun getChessMapFileContent(chessMap: Array<ChessItem?>, turn: Int): String {
        val fields = HashMap<String, Any>()
        fields["chessMap"] = getChessMapInfo(chessMap)
        fields["turn"] = turn
        return JSON.toJSONString(fields)
    }

    class MyFileFilter : javax.swing.filechooser.FileFilter() {
        override fun accept(f: java.io.File): Boolean {
            if (f.isDirectory) return true;
            return f.name.endsWith(".json");  //设置为选择以.class为后缀的文件
        }

        override fun getDescription(): String = "棋局文件(.json)"
    }
}