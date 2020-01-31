package vlados.dudos.pixelseller

import android.content.Context
import android.view.View
import app.App

object Case {

    var openFragment = ""
    val sharedPreferences = lazy { App.context.getSharedPreferences("app", Context.MODE_PRIVATE) }
}