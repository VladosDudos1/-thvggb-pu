package vlados.dudos.pixelseller

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.App
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.row_header.view.*
import kotlinx.android.synthetic.main.shop_fragment.*
import vlados.dudos.pixelseller.ItemView
import kotlinx.android.synthetic.main.shop_view.view.*

class ShopFragment : Fragment() {


    fun isHeader1(position: Int): Boolean {
        return position == 0
    }

    fun isHeader2(position: Int): Boolean {
        return position == 4
    }

    fun isHeader3(position: Int): Boolean {
        return position == 8
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val asd = inflater.inflate(R.layout.shop_fragment, container, false)
        val rv2 = asd.findViewById(R.id.rvS) as RecyclerView
        val gridLayoutManager = GridLayoutManager(activity, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (isHeader1(position)) gridLayoutManager.spanCount
                else if (isHeader2(position)) gridLayoutManager.spanCount
                else if (isHeader3(position)) gridLayoutManager.spanCount
                else 1
            }
        }
        rv2.layoutManager = gridLayoutManager
        val aasd = App.dm.api
            .phones()
            .map {
                it.forEachIndexed { index, shopResponce -> shopResponce.id = index }
                return@map it.groupBy { it.categoryCode }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ g ->
                val phones = mutableListOf<ItemView>()
                g.entries.forEach {
                    phones.addAll(it.value)
                }
                phones.add(0, CategoryHeader("Phones"))
                phones.add(4, CategoryHeader("TV"))
                phones.add(8, CategoryHeader("PC"))
                rv2.adapter = ShopAdapter(phones) { item ->
                    toShopProfile(item)
                }
            }, {})

        return asd

    }

    private fun toShopProfile(shop: ShopResponce) {
        ShopModelWrapper.shopResponce = shop
        startActivity(Intent(PhoneActivity.newIntent(requireActivity(), shop)))
    }

    inner class ShopAdapter(
        private val list: List<ItemView>,
        private val onClick: (ShopResponce) -> Unit
    ) :
        BaseAdapter<ItemView, CommonViewHolder<ItemView>>() {

        override val dataList: List<ItemView> = list
        private val shopItem = 1
        private val headerItem = 2


        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CommonViewHolder<ItemView> {
            val item: CommonViewHolder<ItemView> = when (viewType) {
                shopItem -> ShopView(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.shop_view,
                        parent,
                        false
                    ), onClick
                ) as CommonViewHolder<ItemView>
                headerItem -> Header(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.row_header,
                        parent,
                        false
                    )
                ) as CommonViewHolder<ItemView>
                else -> throw RuntimeException()
            }
            return item
        }

        override fun getItemViewType(position: Int): Int {
            return when (list[position]) {
                is ShopResponce -> shopItem
                is CategoryHeader -> headerItem
                else -> super.getItemViewType(position)
            }
        }
    }
}

class ShopView(view: View, private val onClick: (ShopResponce) -> Unit) :
    CommonViewHolder<ShopResponce>(view) {

    override fun bind(model: ShopResponce) {
        with(itemView) {
            shop_txt.text = model.title

            shop_cost.text = model.amount.toString()
            Glide.with(shop_img)
                .load(model.img)
                .into(shop_img)
            setOnClickListener {
                onClick(model)
            }
        }
    }
}

class Header(
    view: View
) : CommonViewHolder<CategoryHeader>(view) {

    override fun bind(model: CategoryHeader) {
        itemView.row_header_text.text = model.title

    }

}

abstract class CommonViewHolder<T : ItemView>(
    view: View
) : RecyclerView.ViewHolder(view) {
    abstract fun bind(model: T)
}


