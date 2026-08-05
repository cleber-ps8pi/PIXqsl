package moe.zzy040330.taffyqsl.ui.grids

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

/**
 * Simple JSON cache of LoTW confirmed QSOs (as field maps) for grid view.
 */
class GridCache(context: Context) {

    private val file = File(context.filesDir, "lotw_grids_cache.json")

    data class Snapshot(
        val qsos: List<Map<String, String>>,
        val fetchedAtEpochMs: Long,
        val newestQsoDate: String? // YYYY-MM-DD or YYYYMMDD
    )

    fun load(): Snapshot? {
        if (!file.exists()) return null
        return runCatching {
            val root = JSONObject(file.readText())
            val arr = root.getJSONArray("qsos")
            val list = mutableListOf<Map<String, String>>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                val map = mutableMapOf<String, String>()
                obj.keys().forEach { k -> map[k] = obj.getString(k) }
                list.add(map)
            }
            Snapshot(
                qsos = list,
                fetchedAtEpochMs = root.optLong("fetchedAt", 0L),
                newestQsoDate = root.optString("newestQsoDate", null)?.takeIf { it.isNotBlank() }
            )
        }.getOrNull()
    }

    fun save(qsos: List<Map<String, String>>) {
        val arr = JSONArray()
        qsos.forEach { q ->
            val o = JSONObject()
            q.forEach { (k, v) -> o.put(k, v) }
            arr.put(o)
        }
        val newest = qsos.mapNotNull { it["QSO_DATE"]?.trim()?.takeIf { d -> d.isNotEmpty() } }
            .maxOrNull()
        val root = JSONObject()
            .put("fetchedAt", System.currentTimeMillis())
            .put("newestQsoDate", newest ?: JSONObject.NULL)
            .put("qsos", arr)
        file.writeText(root.toString())
    }

    fun clear() {
        runCatching { file.delete() }
    }

    fun hasCache(): Boolean = file.exists() && file.length() > 2
}
