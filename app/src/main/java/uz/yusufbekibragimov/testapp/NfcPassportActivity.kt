package uz.yusufbekibragimov.testapp

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.nfc.NfcAdapter
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.google.android.datatransport.runtime.backends.BackendResponse.ok
import com.invan.salamatina.ui.CaptureActivity
import com.invan.salamatina.ui.CaptureActivity.Companion.DOC_TYPE
import com.invan.salamatina.ui.DocType
import org.jmrtd.lds.icao.MRZInfo
import uz.yusufbekibragimov.testapp.databinding.NfcPasportFragmentBinding
import uz.yusufbekibragimov.testapp.util.PermissionUtil
import java.io.*

class NfcPassportActivity : AppCompatActivity() {

    private var _binding: NfcPasportFragmentBinding? = null
    private val binding get() = _binding!!

    private val REQUEST_IMAGE_CAPTURE = 10050
    private val APP_CAMERA_ACTIVITY_REQUEST_CODE = 10051

    val MRZ_RESULT = "MRZ_RESULT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = NfcPasportFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.nfcPassportScreenBtn.setOnClickListener {
            val mNfcAdapter = NfcAdapter.getDefaultAdapter(this.applicationContext)
            if (!mNfcAdapter.isEnabled) {
                showNFC()
            }
        }
    }

    private fun showNFC() {
        val intent = Intent(Settings.ACTION_NFC_SETTINGS)
        startActivity(intent)
    }

    private fun requestPermissionForCamera() {
        val permissions =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val isPermissionGranted: Boolean = PermissionUtil.hasPermissions(this, permissions)
        if (!isPermissionGranted) {
            AppUtil.showAlertDialog(
                this,
                getString(R.string.app_name),
                getString(R.string.app_name),
                getString(R.string.app_name),
                false
            ) { dialogInterface, i ->
                ActivityCompat.requestPermissions(
                    this,
                    permissions,
                    PermissionUtil.REQUEST_CODE_MULTIPLE_PERMISSIONS
                )
            }
        } else {
            openCameraActivity()
        }
    }

    private fun openCameraActivity() {
        val intent = Intent(this, CaptureActivity::class.java)
        intent.putExtra(DOC_TYPE, DocType.PASSPORT)
        startActivityForResult(intent, APP_CAMERA_ACTIVITY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val extras = data!!.extras
            val image = extras!!["data"] as Bitmap?
            val path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            )
            val f = File(path, System.currentTimeMillis().toString() + ".jpg")
            if (!f.exists()) {
                try {
                    f.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            val bos = ByteArrayOutputStream()
            image!!.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
            val bitmapdata = bos.toByteArray()

            //write the bytes in file
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(f)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            try {
                if (fos != null) {
                    fos.write(bitmapdata)
                    fos.flush()
                    fos.close()
                    Glide.with(this)
                        .load(Uri.fromFile(f).path)
                        .into(binding.nfcReadImage)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                e.printStackTrace()
            }
        }

        if (resultCode == RESULT_OK) {
            when (10050) {
                10050 -> {
                    val mrzInfo = data!!.getSerializableExtra(MRZ_RESULT) as MRZInfo?
                    if (mrzInfo != null) {
                        setMrzData(mrzInfo)
                        findNavController(R.id.main_nav)
                        if (data.hasExtra("image")) {
                            val byteArray = data.getByteArrayExtra("image")
                            val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)
                            binding.nfcReadImage.setImageBitmap(bmp)
                        }
                    } else {
                        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                    }
                }
                else -> {}
            }
        } else {
            if (resultCode == RESULT_CANCELED) {
                finish()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setMrzData(mrzInfo: MRZInfo) {
        val adapter = NfcAdapter.getDefaultAdapter(this)
        val passportNumber = mrzInfo.documentNumber
        val expirationDate = mrzInfo.dateOfExpiry
        val birthDate = mrzInfo.dateOfBirth
        try {
            binding.pasNoMRZ.setText(passportNumber)
            binding.eDateMRZ.setText(
                expirationDate.substring(4, 6) + "." + expirationDate.substring(
                    2,
                    4
                ) + "." + expirationDate.substring(0, 2)
            )
            binding.bDateMRZ.setText(
                birthDate.substring(4, 6) + "." + birthDate.substring(
                    2,
                    4
                ) + "." + birthDate.substring(0, 2)
            )
            Log.d("Mrx", "nationality = $mrzInfo")
            Glide.with(this)
                .load("file:///android_asset/nfc_gif.gif")
                .into(binding.nfcReadImage)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}