package goldenhuaji.me.fanchess

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.util.TypeUtils.castToJavaBean
import goldenhuaji.me.fanchess.GLog.loge
import goldenhuaji.me.fanchess.settings.ConfigManager
import java.util.HashMap
import java.io.IOException

import java.util.zip.ZipException

import java.io.FileOutputStream

import java.io.File

import java.util.zip.ZipEntry

import java.util.Enumeration

import java.util.zip.ZipFile

import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset

import java.util.ArrayList





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

    /**
     * 解压 Zip
     *
     * @param fileName 文件名
     * @param path 解压到...
     */
    fun unzip(fileName: String, path: String) {
        var fos: FileOutputStream? = null
        var `is`: InputStream? = null
        try {
            val zf = ZipFile(File(fileName))
            val en: Enumeration<*> = zf.entries()
            while (en.hasMoreElements()) {
                val zn = en.nextElement() as ZipEntry
                if (!zn.isDirectory) {
                    `is` = zf.getInputStream(zn)
                    val f = File(path + zn.name)
                    val file = f.parentFile
                    file.mkdirs()
                    fos = FileOutputStream(path + zn.name)
                    var len = 0
                    val buffer = ByteArray(2 shl 10)
                    while (-1 != `is`.read(buffer).also { len = it }) {
                        fos.write(buffer, 0, len)
                    }
                    fos.close()
                }
            }
        } catch (e: ZipException) {
            loge(e)
        } catch (e: IOException) {
            throw e
        } finally {
            try {
                `is`?.close()
                fos?.close()
            } catch (e: IOException) {
                loge(e)
            }
        }
    }

    /**
     * @Author AlphaJunS
     * @Date 11:36 2020/3/8
     * @Description 对.zip文件进行解压缩
     * @param zipFile 解压缩文件
     * @param descDir 压缩的目标地址，如：D:\\测试 或 /mnt/d/测试
     * @return java.util.List<java.io.File>
    </java.io.File> */
    fun unzipFile(zipFile: File?, descDir: String): List<File?>? {
        val _list: MutableList<File?> = ArrayList()
        try {
            val _zipFile = ZipFile(zipFile, Charset.forName("GBK"))
            val entries: Enumeration<*> = _zipFile.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement() as ZipEntry
                val _file = File(descDir + File.separator + entry.name)
                if (entry.isDirectory) {
                    _file.mkdirs()
                } else {
                    val _parent = _file.parentFile
                    if (!_parent.exists()) {
                        _parent.mkdirs()
                    }
                    val _in: InputStream = _zipFile.getInputStream(entry)
                    val _out: OutputStream = FileOutputStream(_file)
                    var len = 0
                    val _byte = ByteArray(1024)
                    while (_in.read(_byte).also { len = it } > 0) {
                        _out.write(_byte, 0, len)
                    }
                    _in.close()
                    _out.flush()
                    _out.close()
                    _list.add(_file)
                }
            }
        } catch (e: IOException) {
            throw e
        }
        return _list
    }

    class MyFileFilter : javax.swing.filechooser.FileFilter() {
        override fun accept(f: java.io.File): Boolean {
            if (f.isDirectory) return true;
            return f.name.endsWith(".json")
        }

        override fun getDescription(): String = "棋局文件(.json)"
    }

    class PackFileFilter : javax.swing.filechooser.FileFilter() {
        override fun accept(f: java.io.File): Boolean {
            if (f.isDirectory) return true;
            return f.name.endsWith(".zip")
        }

        override fun getDescription(): String = "资源包(.zip)"
    }
}

fun File.unzip(path: String) {
    Utils.unzipFile(this, path)
}