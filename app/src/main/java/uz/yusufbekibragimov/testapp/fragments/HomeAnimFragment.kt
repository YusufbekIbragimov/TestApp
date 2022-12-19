package uz.yusufbekibragimov.testapp.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionLayout.TransitionListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.yusufbekibragimov.testapp.R
import uz.yusufbekibragimov.testapp.adapter.HomeAdapter
import uz.yusufbekibragimov.testapp.databinding.HomeAnimFragmentBinding

class HomeAnimFragment : Fragment() {

    private var _binding: HomeAnimFragmentBinding? = null
    private val binding get() = _binding!!

    private val adapterMain by lazy { HomeAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeAnimFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setFullScreenMode()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setViews()
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setViews() {
        binding.apply {

            nestedView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            nestedView.adapter = adapterMain
            adapterMain.refresh()

            motionLayout.setTransitionListener(object : TransitionListener {
                override fun onTransitionStarted(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int
                ) {
                    Log.d("TAGTTT", "onTransitionChange: START")
                }

                override fun onTransitionChange(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int,
                    progress: Float
                ) {
                    Log.d("TAGFFF", "onTransitionChange: 111111111 progress=$progress")
                }

                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                    Log.d("TAGTTT", "onTransitionChange: progress23132=${motionLayout?.progress}")
                    lifecycleScope.launch(Dispatchers.Main) {
                        if (currentId == R.id.end) {
                            binding.lottieView.playAnimation()
                            vibrate(10, requireContext())
                            binding.nestedView.isEnabled = false
                            delay(1000)
                            binding.nestedView.isEnabled = true
                            motionLayout?.transitionToState(R.id.start,10)
                        }
                    }
                }

                override fun onTransitionTrigger(
                    motionLayout: MotionLayout?,
                    triggerId: Int,
                    positive: Boolean,
                    progress: Float
                ) {
                    Log.d("TAGTTT", "onTransitionChange: positive=$positive")
                }

            })
        }
    }

    override fun onDestroyView() {
        clearFullScreenMode()
        super.onDestroyView()
    }

}

fun Fragment.setFullScreenMode() {
    requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
}

fun Fragment.clearFullScreenMode() {
    requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
}

fun vibrate(milliSecond: Long, context: Context) {

    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        vibrator.vibrate(
            VibrationEffect.createOneShot(
                milliSecond,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
        )
    else vibrator.vibrate(milliSecond)

}