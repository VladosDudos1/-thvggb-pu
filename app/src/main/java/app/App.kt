package app

import android.app.Application
import android.content.Context
import data.DataManager


class App : Application() {
    companion object {
        lateinit var dm: DataManager
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()

        context = baseContext
        dm = DataManager(baseContext)
    }
}