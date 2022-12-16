package uz.yusufbekibragimov.testapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.yusufbekibragimov.testapp.R
import uz.yusufbekibragimov.testapp.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            animScreenBtn.setOnClickListener { findNavController().navigate(R.id.action_mainFragment_to_homeAnimFragment) }
            faceIdScreenBtn.setOnClickListener { findNavController().navigate(R.id.action_mainFragment_to_faceDetectionFragment) }
            cardReadCameraScreenBtn.setOnClickListener { findNavController().navigate(R.id.action_mainFragment_to_cardScanCameraFragment) }
            passportReadScreenBtn.setOnClickListener { findNavController().navigate(R.id.action_mainFragment_to_passportReadCameraFragment) }
            nfcHumoScreenBtn.setOnClickListener { findNavController().navigate(R.id.action_mainFragment_to_cardScanNfcFragment) }
            nfcPassportScreenBtn.setOnClickListener { findNavController().navigate(R.id.action_mainFragment_to_nfcPassportFragment) }
            livenessDetectionScreenBtn.setOnClickListener { findNavController().navigate(R.id.action_mainFragment_to_indetifyFragment) }
        }
    }

}