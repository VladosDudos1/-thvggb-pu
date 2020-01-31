package vlados.dudos.pixelseller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import vlados.dudos.pixelseller.Case.openFragment

class MainActivity : AppCompatActivity() {

    private val fragmetShop = ShopFragment()
    private val manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        showShopFragment()

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigation_bar)


        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.shoping -> {
                    showShopFragment()
                }

                R.id.settings -> {
                    showSettingsFragment()
                }
                R.id.heart_img -> {
                    showFavoriteFragment()
                }

            }
            true
        }
    }

    override fun onStop() {
        super.onStop()
        openFragment = ""
    }

    override fun onBackPressed() {
        if (manager.backStackEntryCount > 0) {
            val count = manager.backStackEntryCount
            for (i in 0 until count)
                manager.popBackStack()
            manager.beginTransaction().replace(
                R.id.fragmet_holder, fragmetShop
            ).commit()
        } else {
            super.onBackPressed()
        }

    }

    fun replaceFragment(fmt: Fragment) {
        if (openFragment == "") {
            manager.beginTransaction().replace(R.id.fragmet_holder, fmt).commit()
        } else {
            manager.beginTransaction().replace(R.id.fragmet_holder, fmt).addToBackStack(null)
                .commit()
        }
    }

    fun showShopFragment() {
        if (openFragment != "ShopFragment opened") {
            replaceFragment(ShopFragment())
        }
        openFragment = "ShopFragment opened"
    }

    fun showSettingsFragment() {
        if (openFragment != "SettingsFragment opened") {
            replaceFragment(SettingsFragment())
        }
        openFragment = "SettingsFragment opened"
    }

    fun showFavoriteFragment() {
        if (openFragment != "FavoriteFragment opened") {
            replaceFragment(FavoriteFragment())
        }
        openFragment = "FavoriteFragment opened"
    }
}

