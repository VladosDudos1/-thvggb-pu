package vlados.dudos.pixelseller

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

fun getPhotoFromUri(context: Context, uri: Uri): Bitmap {
    return BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri))
}
