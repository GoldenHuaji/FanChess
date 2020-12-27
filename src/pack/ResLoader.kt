package goldenhuaji.me.fanchess.pack

import goldenhuaji.me.fanchess.settings.ConfigManager
import goldenhuaji.me.fanchess.unzip
import java.io.File
import java.net.URL

object ResLoader {
    fun loadRes(name: String): URL? {
        if (ConfigManager.get().getString(ConfigManager.ENABLED_PACK_PATH) != null) {
            val pack = File(ConfigManager.get().getString(ConfigManager.ENABLED_PACK_PATH)!!)
            val path = System.getProperty("java.io.tmpdir") + "FanChess\\UsingPack\\"
            pack.unzip(path)
            val f = File(path + name)
            if (!f.exists()) {
                return javaClass.classLoader.getResource(name)
            }
            return f.toURI().toURL()
        }
        return javaClass.classLoader.getResource(name)
    }
}