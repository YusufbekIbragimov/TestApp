package uz.yusufbekibragimov.testapp.lib

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import uz.yusufbekibragimov.testapp.lib.LottiePullToRefreshLayout

class LottiePullToRefreshLayoutObservable(private val lottiePullToRefreshLayout: LottiePullToRefreshLayout) : Observable<LottiePullToRefreshes>() {
    override fun subscribeActual(observer: Observer<in LottiePullToRefreshes>) {
        val listener = Listener(lottiePullToRefreshLayout, observer)
        observer.onSubscribe(listener)
        lottiePullToRefreshLayout.onTriggerListener(listener.onPullToRefresh)
    }

    class Listener(private val lottiePullToRefreshLayout: LottiePullToRefreshLayout,
                   private val observer: Observer<in LottiePullToRefreshes>) : MainThreadDisposable() {

        val onPullToRefresh : () -> Unit = {
            if (!isDisposed) {
                observer.onNext(LottiePullToRefreshes)
            }
        }

        override fun onDispose() {
            lottiePullToRefreshLayout.removeOnTriggerListener(onPullToRefresh)
        }
    }
}

fun LottiePullToRefreshLayout.refreshes(): Observable<LottiePullToRefreshes> = LottiePullToRefreshLayoutObservable(this)
