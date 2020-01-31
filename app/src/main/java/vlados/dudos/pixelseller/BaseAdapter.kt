package vlados.dudos.pixelseller

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<Data: ItemView,
        ViewHolder: CommonViewHolder<Data>>: RecyclerView.Adapter<ViewHolder>() {

    abstract val dataList: List<Data>

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

}