package goldenhuaji.me.fanchess.settings

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import java.io.File
import java.io.FileWriter

class ConfigManager {
    var configFile: File? = null
    private var tmp: JSONObject

    init {
        if (configFile == null) {
            configFile = File("${System.getenv("APPDATA")}\\FanChess\\settings.json")
        }
        if (!configFile!!.exists()) {
            if (!File("${System.getenv("APPDATA")}\\FanChess").exists()) {
                File("${System.getenv("APPDATA")}\\FanChess").mkdir()
            }
            configFile!!.createNewFile()
        }
        tmp = JSON.parseObject(configFile!!.readText()) ?: JSONObject()
    }

    fun put(key: String, value: String) {
        tmp[key] = value
    }

    fun put(key: String, value: Boolean) {
        tmp[key] = value
    }

    fun changeBoolean(key: String) {
        tmp[key] = !getBooleanNonNull(key)
    }

    fun getLong(key: String): Long? {
        return tmp.getLong(key)
    }

    fun getInt(key: String): Int? {
        return tmp.getInteger(key)
    }

    fun getString(key: String): String? {
        return tmp.getString(key)
    }

    fun getBoolean(key: String): Boolean? {
        return tmp.getBoolean(key)
    }

    fun getBooleanNonNull(key: String): Boolean {
        return tmp.getBoolean(key) ?: false
    }

    fun save() {
        val fileWriter = FileWriter(configFile!!)
        fileWriter.write(tmp.toJSONString())
        fileWriter.flush()
        fileWriter.close()
    }

    companion object {
        var SELF: ConfigManager? = null
        fun get(): ConfigManager = SELF ?: ConfigManager().apply { SELF = this }

        val SHIT_MODE = "ShitMode".hashCode().toString()
    }
}