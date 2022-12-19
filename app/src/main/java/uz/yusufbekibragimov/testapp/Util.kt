package uz.yusufbekibragimov.testapp

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * Created by Ibragimov Yusufbek
 * Date: 12/17/2022
 * Project: Test App
 **/

/**
 * The current version of motionLayout (2.0.0-beta04) does not honor the position
 * of a RecyclerView if it is wrapped in a SwipeRefreshLayout:
 * When scrolling back to top, the motionLayout transition
 * would be triggered immediately instead of only as soon as the RecyclerView scrolled back to top.
 *
 * This workaround checks if the SwipeRefresh layout can still scroll back up.
 * If so, it does not trigger the motionLayout transition.
 */
class SwipeRefreshMotionLayout : MotionLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {

        if (target is RecyclerView) {
            Log.d("TAGTAG", "onNestedPreScroll: ishladi")
        } else {
            Log.d("TAGTAG", "onNestedPreScroll: ishladi01")
            // return super.onNestedPreScroll(target, dx, dy/4, consumed, type)
        }

        //val recyclerView = target.getChildAt(0)
        if (target is NestedScrollView) {
            Log.d("TAGTAG", "onNestedPreScroll: ishladi2")
        } else {
            Log.d("TAGTAG", "onNestedPreScroll: ishladi3")
            return super.onNestedPreScroll(target, dx, dy/4, consumed, type)
        }

        val recyclerView = target.getChildAt(0)
        val canScrollVertically = recyclerView.canScrollVertically(-1)
        if (dy < -100 && canScrollVertically) {
            // don't start motionLayout transition
            Log.d("TAGTAG", "onNestedPreScroll: ishladi3")
            return
        }

        //super.onNestedPreScroll(target, dx, dy/4, consumed, type)
    }
}