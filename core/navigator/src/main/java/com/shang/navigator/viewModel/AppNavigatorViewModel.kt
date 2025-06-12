package com.shang.navigator.viewModel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.shang.navigator.core.AppNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppNavigatorViewModel @Inject constructor(
    private val appNavigator: AppNavigator
) : ViewModel() , AppNavigator by appNavigator {

}