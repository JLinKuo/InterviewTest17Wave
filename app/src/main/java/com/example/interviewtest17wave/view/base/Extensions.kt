package com.example.interviewtest17wave.view.base

import android.util.Log
import androidx.fragment.app.Fragment
import com.example.interviewtest17wave.BuildConfig.ISDEBUG
import com.example.interviewtest17wave.R
import com.example.interviewtest17wave.view.main.MainActivity
import com.example.mvvmcodebase.model.network.Resource
import java.io.IOException

fun Fragment.handleApiError(
    failure: Resource.Failure
) {
    val mainActivity = (activity as MainActivity)

    when (failure.errorCode) {
        400, 404, 500 -> {
            val msg = String.format(getString(R.string.dialog_error_server_busy), failure.errorCode)
            mainActivity.showOneButtonNoTitleDialog(msg, null)
        }

        else -> {
            when(failure.throwable) {
                is IOException -> {
                    mainActivity.showOneButtonNoTitleDialog(
                        getString(R.string.dialog_error_network), null)
                }
                else -> {
                    if(ISDEBUG) {
                        Log.e("JLin", "error message: ${failure.throwable.message}")
                    }
                }
            }
        }
    }
}
