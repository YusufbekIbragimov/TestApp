package uz.yusufbekibragimov.testapp.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.yusufbekibragimov.testapp.CustomLayoutManager
import uz.yusufbekibragimov.testapp.adapter.HomeAdapter
import uz.yusufbekibragimov.testapp.databinding.HomeAnimFragmentBinding
import uz.yusufbekibragimov.testapp.lib.SimplePullToRefreshLayout

class HomeAnimFragment : Fragment() {

    private var _binding: HomeAnimFragmentBinding? = null
    private val binding get() = _binding!!

    private val adapterMain by lazy { HomeAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeAnimFragmentBinding.inflate(
            LayoutInflater.from(container?.context),
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setFullScreenMode()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setViews()
        }
    }

    private fun setViews() {
        binding.apply {

            nestedView.layoutManager = CustomLayoutManager(requireContext(), true)
            nestedView.adapter = adapterMain
            adapterMain.refresh()

            collapseToolBar.onTriggerListener {
                lifecycleScope.launch {
                    collapseToolBar.setState(SimplePullToRefreshLayout.State.TRIGGERING)
                    nestedView.layoutManager = CustomLayoutManager(requireContext(), false)
                    vibrate(10, requireContext())
                    delay(4000)
                    nestedView.layoutManager = CustomLayoutManager(requireContext(), true)
                    collapseToolBar.setState(SimplePullToRefreshLayout.State.IDLE)
                    collapseToolBar.stopRefreshing()
                }
            }

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