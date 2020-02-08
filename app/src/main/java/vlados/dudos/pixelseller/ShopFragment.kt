package vlados.dudos.pixelseller

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.App
import com.bumptech.glide.Glide
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.row_header.view.*
import kotlinx.android.synthetic.main.shop_view.view.*

class ShopFragment : Fragment() {

//    val categoryList = mutableListOf<>()
    val phones = App.dm.api.phones()
    val category = App.dm.api.category()



    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val asd = inflater.inflate(R.layout.shop_fragment, container, false)
        val rv2 = asd.findViewById(R.id.rvS) as RecyclerView
        val gridLayoutManager = GridLayoutManager(activity, 2)

        rv2.layoutManager = gridLayoutManager

        val afs =Observable.zip(phones,category,
            BiFunction{ goodList:List<ShopResponce>, categoryList:List<CategoryResponse> -> val result :MutableMap<CategoryResponse,List<ShopResponce>> = mutableMapOf()
                categoryList.forEach { category ->
                    val filtredlist =
                        goodList.filter { goods -> goods.categoryCode == category.category_id }
                    result[category] = filtredlist
                }
                return@BiFunction result
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ s ->
                val goods = mutableListOf<ItemView>()
                s.entries.forEach {
                    Log.d("test", it.key.description)
                    goods.add(CategoryResponse(it.key.category_id,it.key.description))

                    it.value.forEach {
                        Log.d("Test",it.description)
                        goods.add(ShopResponce(it.amount,it.categoryCode,it.description,it.img,it.rating,it.subTitle,it.title,it.id))
                    }
                }


                rv2.adapter = ShopAdapter(goods) { item ->
                    toShopProfile(item)
                }
            }, {})
        return asd
    }

    private fun filter(view: View) {

    }

    private fun toShopProfile(shop: ShopResponce) {
        ShopModelWrapper.shopResponce = shop
        startActivity(Intent(PhoneActivity.newIntent(requireActivity(), shop)))
    }
}

 class ShopAdapter(
    private val list: List<ItemView>,
    private val onClick: (ShopResponce) -> Unit
) :
    BaseAdapter<ItemView, CommonViewHolder<ItemView>>() {

    override val dataList: List<ItemView> = list
    private val goodItem = 1
    private val headerItem = 2

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommonViewHolder<ItemView> {
        val item: CommonViewHolder<ItemView> = when (viewType) {
            goodItem -> ShopView(
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
            is ShopResponce -> goodItem
            is CategoryResponse -> headerItem
            else -> super.getItemViewType(position)
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
) : CommonViewHolder<CategoryResponse>(view) {

    override fun bind(model: CategoryResponse) {
        itemView.row_header_text.text = model.description
    }
}

abstract class CommonViewHolder<T : ItemView>(
    view: View
) : RecyclerView.ViewHolder(view) {
    abstract fun bind(model: T)
}