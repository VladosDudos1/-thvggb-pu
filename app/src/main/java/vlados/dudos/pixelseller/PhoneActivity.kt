package vlados.dudos.pixelseller

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_phone.*
import vlados.dudos.pixelseller.Case.sharedPreferences
import vlados.dudos.pixelseller.ShopModelWrapper.shopResponce

class PhoneActivity : AppCompatActivity() {

    var shopResponce: ShopResponce? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone)

        if (savedInstanceState == null) {
            val extras = intent.extras
            if (extras == null) {
                shopResponce = null
            } else {
                shopResponce = extras.getParcelable(MODEL_KEY)
            }
        } else {
            shopResponce = savedInstanceState.getSerializable(MODEL_KEY) as ShopResponce
        }

        shopResponce?.let {
            Glide.with(imgg)
                .load(it.img)
                .into(imgg)
            description.text = it.description
            toolbar.title = it.title

            subtitle.text = shopResponce?.subTitle
            amount_txt.text = "Цена: "+shopResponce?.amount.toString()
        }
    }




    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(baseContext).inflate(R.menu.toolbar_menu, menu)
        menu?.let {
            it.getItem(0).setOnMenuItemClickListener {
                shopResponce?.apply {
                    val name = "fav${this.categoryCode}"
                    val result = sharedPreferences.value.getBoolean(name, false)
                    sharedPreferences.value.edit().putBoolean(name, !result).apply()
                    Toast.makeText(this@PhoneActivity, (!result).toString(), Toast.LENGTH_LONG)
                        .show()
                }

                true
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        private const val MODEL_KEY = "photo_model"

        fun newIntent(ac: Activity, model: ShopResponce) =
            Intent(ac, PhoneActivity::class.java).apply {
                putExtra(MODEL_KEY, model)
            }
    }

}