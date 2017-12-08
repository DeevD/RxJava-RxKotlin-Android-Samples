package com.freeankit.rxjava2samples.ui.operators

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.freeankit.rxjava2samples.R
import com.freeankit.rxjava2samples.model.GithubUser
import com.freeankit.rxjava2samples.model.User
import com.freeankit.rxjava2samples.utils.Constant
import com.freeankit.rxjava2samples.utils.Utils
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_map_operator.*

/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 08/12/2017 (MM/DD/YYYY )
 */
class MapOperatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_operator)

        btn.setOnClickListener({ executeMapOperator() })
    }

    private fun executeMapOperator() {
        getObservable()
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .map { apiUsers -> Utils().convertApiUserListToUserList(apiUsers) }
                .subscribe(getObserver())

    }

    private fun getObservable(): Observable<List<GithubUser>> {
        return Observable.create<List<GithubUser>> { e ->
            if (!e.isDisposed) {
                e.onNext(Utils().getApiUserList())
                e.onComplete()
            }
        }
    }

    private fun getObserver(): Observer<List<User>> {
        return object : Observer<List<User>> {

            override fun onSubscribe(d: Disposable) {
                Log.d(Constant().TAG, " onSubscribe : " + d.isDisposed)
            }

            override fun onNext(userList: List<User>) {
                textView.append(" onNext")
                textView.append(Constant().LINE_SEPARATOR)
                for (user in userList) {
                    textView.append(" login Name : " + user.login)
                    textView.append(Constant().LINE_SEPARATOR)
                }
                Log.d(Constant().TAG, " onNext : " + userList.size)
            }

            override fun onError(e: Throwable) {
                textView.append(" onError : " + e.message)
                textView.append(Constant().LINE_SEPARATOR)
                Log.d(Constant().TAG, " onError : " + e.message)
            }

            override fun onComplete() {
                textView.append(" onComplete")
                textView.append(Constant().LINE_SEPARATOR)
                Log.d(Constant().TAG, " onComplete")
            }
        }
    }
}