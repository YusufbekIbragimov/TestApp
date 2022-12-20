package uz.yusufbekibragimov.testapp.fragments

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import uz.yusufbekibragimov.testapp.databinding.NfcPasportFragmentBinding

class NfcPassportFragment : Fragment() {

    private var _binding: NfcPasportFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NfcPasportFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mNfcAdapter = NfcAdapter.getDefaultAdapter(requireActivity().applicationContext)
        if (!mNfcAdapter.isEnabled) {
            showNFC()
        }
    }

    private fun showNFC() {
        val intent = Intent(Settings.ACTION_NFC_SETTINGS)
        startActivity(intent)
    }

}