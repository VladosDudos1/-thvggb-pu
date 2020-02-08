package vlados.dudos.pixelseller

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShopResponce(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("category_code")
    val categoryCode: Int,
    val description: String,
    val img: String,
    val rating: Int,
    @SerializedName("sub_title")
    val subTitle: String,
    val title: String,
    var id:Int
) : Parcelable, ItemView

interface ItemView