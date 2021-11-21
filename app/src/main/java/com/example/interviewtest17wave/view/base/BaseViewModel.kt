package com.example.mvvmcodebase.view.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel: ViewModel() {
    protected var TAG = javaClass.simpleName
}