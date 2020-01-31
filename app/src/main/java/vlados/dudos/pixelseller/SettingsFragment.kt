package vlados.dudos.pixelseller


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import kotlinx.android.synthetic.main.settings_fragment.*
import java.lang.Exception
import android.os.Environment.getExternalStorageDirectory
import androidx.core.app.ActivityCompat.getExternalFilesDirs
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class SettingsFragment : Fragment() {

    val shared = lazy { this.requireContext().getSharedPreferences("app", Context.MODE_PRIVATE) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED ||
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_DENIED
        ) {


            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 31
            )
        }
        user_photo.setOnClickListener {
            selectImg(view)
        }

        try {
            val path = shared.value.getString("photo_path", "")
            if (!path.isNullOrEmpty())
                user_photo.setImageURI(path.toUri())
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            31 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED))
                return
            }
        }
    }
    fun selectImg(view: View) {
        MaterialDialog(requireActivity())
            .title(text = "Choose from ")
            .listItemsSingleChoice(
                items = listOf("Gallery", "Camera"),
                selection = { dialog, index, text ->
                    if (index == 0) gallery() else camera()
                    dialog.cancel()
                }).show {}
    }

    private fun camera() {
        startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), 1)
    }

    private fun gallery() {
        try {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        } catch (ex: Exception) {

        }
    }
    private var bitmap: Bitmap? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
        if (data == null) return
        if (requestCode == 0) {
            data.data?.let {
                bitmap = getPhotoFromUri(requireContext(), it)
            }
        } else if (requestCode == 1) {
            bitmap = data.extras!!.get("data") as Bitmap
        }
        user_photo.setImageBitmap(bitmap)

        if (bitmap != null) {
            val path = getExternalStorageDirectory().toString()
            var fOut: OutputStream?
            val file = File(path, "Yuuechka.jpg")
            fOut = FileOutputStream(file)
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 85, fOut)
            fOut.flush()
            fOut.close()
            shared.value.edit().putString("photo_path",file.absolutePath).apply()
        }

    }

    companion object {
        private const val PHOTO_KEY = "photo_path"
    }

}