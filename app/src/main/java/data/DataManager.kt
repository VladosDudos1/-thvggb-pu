package data

import android.content.Context
import android.content.SharedPreferences

class DataManager {
    private val baseContext: Context
    private val pref: SharedPreferences

    val api = Api.createApi()

    constructor(baseContext: Context) {
        this.baseContext = baseContext
        pref = baseContext.getSharedPreferences("WS", Context.MODE_PRIVATE)
    }
}
