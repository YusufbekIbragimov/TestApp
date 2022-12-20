package uz.yusufbekibragimov.testapp

import android.content.Intent
import android.graphics.BitmapFactory
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import org.jmrtd.lds.icao.MRZInfo
import uz.yusufbekibragimov.testapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    val MRZ_RESULT = "MRZ_RESULT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
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
                            nfcReadImage.setImageBitmap(bmp)
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
    }

    private fun setMrzData(mrzInfo: MRZInfo) {
//        adapter = NfcAdapter.getDefaultAdapter(this)
//        mainLayout.setVisibility(View.GONE)
//        mainContent.setVisibility(View.GONE)
//        imageLayout.setVisibility(View.VISIBLE)
        val passportNumber = mrzInfo.documentNumber
        val expirationDate = mrzInfo.dateOfExpiry
        val birthDate = mrzInfo.dateOfBirth
        Log.d("TTT", "setMrzData: $birthDate")
        try {
            binding.pasNoMRZ.setText(passportNumber)
            eDateMRZ.setText(
                expirationDate.substring(4, 6) + "." + expirationDate.substring(
                    2,
                    4
                ) + "." + expirationDate.substring(0, 2)
            )
            bDateMRZ.setText(
                birthDate.substring(4, 6) + "." + birthDate.substring(
                    2,
                    4
                ) + "." + birthDate.substring(0, 2)
            )
            Log.d("Mrx", "nationality = $mrzInfo")
            // Log.d("MrxIS", "Issuing state " + country.get(mrzInfo.issuingState))
            Glide.with(this)
                .load("file:///android_asset/nfc_gif.gif")
                .into(nfcReadImage)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mrzContent = mrzInfo.documentNumber
    }

}