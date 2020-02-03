package vlados.dudos.pixelseller

import android.content.Context
import android.view.View
import app.App
import java.util.ArrayList

object Case {

    var openFragment = ""
    val sharedPreferences = lazy { App.context.getSharedPreferences("app", Context.MODE_PRIVATE) }

    val shopCostst = mutableListOf<Double>()

    var userName = ""

}