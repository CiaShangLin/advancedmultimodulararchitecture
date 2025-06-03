package com.shang.data.connectivity

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission

/**
 * [NetworkMonitorImp] 實作了 [NetworkMonitorInterface]，用於監控網路連線狀態。
 *
 * @param context Android 的 Context 物件，用於取得系統服務。
 */
class NetworkMonitorImp(context: Context) : NetworkMonitorInterface {

    /**
     * 取得系統的 ConnectivityManager，用於查詢網路連線資訊。
     */
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * 檢查目前是否有網路連線。
     *
     * 需要 [Manifest.permission.ACCESS_NETWORK_STATE] 權限。
     *
     * @return 如果有任何一種網路連線 (Wi-Fi, 行動網路, 乙太網路)，則返回 true，否則返回 false。
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override fun hasConnectivity(): Boolean {
        // 取得目前活動網路的 NetworkCapabilities。
        return connectivityManager.getNetworkCapabilities(
            connectivityManager.activeNetwork,
        )?.let { networkCapabilities ->
            // 檢查 NetworkCapabilities 是否包含任何一種我們關心的傳輸類型。
            listOf(
                NetworkCapabilities.TRANSPORT_WIFI,
                NetworkCapabilities.TRANSPORT_CELLULAR,
                NetworkCapabilities.TRANSPORT_ETHERNET,
            ).any {
                networkCapabilities.hasTransport(it)
            }
        } ?: false // 如果沒有活動網路，則視為沒有連線。
    }
}
