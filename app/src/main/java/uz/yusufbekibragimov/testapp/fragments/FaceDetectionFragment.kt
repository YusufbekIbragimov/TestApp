package uz.yusufbekibragimov.testapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import uz.yusufbekibragimov.testapp.databinding.FaceDetectionFragmentBinding

class FaceDetectionFragment : Fragment() {

    private var _binding: FaceDetectionFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FaceDetectionFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            fingerprintIdScreenBtn.setOnClickListener { openFingerprintId() }
        }
    }

    private fun openFingerprintId() {
        getBiometryPrompt(this) {
            Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
        }
    }

}

inline fun getBiometryPrompt(
    activity: Fragment,
    crossinline onSuccess: () -> Unit = {},
) {
    try {
        val executor = ContextCompat.getMainExecutor(activity.requireContext())

        val callback = object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }
        }
        val promptInfo =
            BiometricPrompt.PromptInfo.Builder().setTitle("click_finger")
                .setNegativeButtonText("enter_password")
                .setConfirmationRequired(true).build()
        val biometricPrompt = BiometricPrompt(activity, executor, callback)
        biometricPrompt.authenticate(promptInfo)
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}