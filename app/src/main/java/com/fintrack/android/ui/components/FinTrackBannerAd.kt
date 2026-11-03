package com.fintrack.android.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.fintrack.android.BuildConfig
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun FinTrackBannerAd(modifier: Modifier = Modifier) {
    var adView by remember { mutableStateOf<AdView?>(null) }

    AndroidView(
        modifier = modifier.fillMaxWidth().height(60.dp),
        factory = { viewContext ->
            AdView(viewContext).apply {
                adUnitId = BuildConfig.ADMOB_BANNER_ID
                setAdSize(AdSize.BANNER)
                loadAd(AdRequest.Builder().build())
                adView = this
            }
        }
    )

    DisposableEffect(adView) {
        onDispose { adView?.destroy() }
    }
}