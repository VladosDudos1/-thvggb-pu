package vlados.dudos.pixelseller

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.App
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_phone.view.*
import kotlinx.android.synthetic.main.favorite_fragment.*
import kotlinx.android.synthetic.main.shop_view.view.*
import vlados.dudos.pixelseller.ShopModelWrapper.shopResponce

class FavoriteFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val asd = inflater.inflate(R.layout.favorite_fragment, container, false)
        val rv2 = asd.findViewById(R.id.rv2) as RecyclerView

        val listNew = mutableListOf<ShopResponce>()

        Case.sharedPreferences.value?.let {
            for (shopResponce in listNew) {
                val name = "fav${shopResponce.id}"
                val result = it.getBoolean(name, false)
                if (result)
                    listNew.add(shopResponce)
            }
        }

        rv2.layoutManager = GridLayoutManager(activity, 2)
        rv2.adapter = ShopAdapter(listNew)


//        val sec = App.dm.api()


        return asd
    }


    private fun toShopProfile(shop: ShopResponce) {
        shopResponce = shop
        startActivity(Intent(PhoneActivity.newIntent(requireActivity(), shop)))
    }

    inner class ShopAdapter(private val list: List<ShopResponce>) :
        RecyclerView.Adapter<ShopAdapter.ShopViewF>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ShopViewF(
            LayoutInflater.from(parent.context).inflate(
                R.layout.shop_view,
                parent,
                false
            )
        )

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: ShopViewF, position: Int) {
            val shop = list[position]
            holder.itemView.shop_txt.text = shop.title
            Glide.with(holder.itemView.shop_img)
                .load(shop.img)
                .into(holder.itemView.shop_img)

            holder.itemView.shop_cost.text = shop.amount.toString()

            holder.itemView.setOnClickListener {
                toShopProfile(shop)
            }
        }

        inner class ShopViewF(view: View) : RecyclerView.ViewHolder(view)
    }
}






