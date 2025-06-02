package com.shang.data.source

import com.google.gson.Gson
import com.shang.data.connectivity.NetworkMonitorInterface

class NetworkDataSource<SERVICE>(
  val service: SERVICE,
  val gson: Gson,
  val networkMonitor: NetworkMonitorInterface,
  val userIdProvider: () -> String,
)
