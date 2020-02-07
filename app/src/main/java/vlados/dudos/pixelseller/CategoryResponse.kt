package vlados.dudos.pixelseller

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class CategoryResponse(
    val category_id: Int,
    val description: String
): Parcelable, ItemView