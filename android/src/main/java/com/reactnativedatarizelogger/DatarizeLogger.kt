package com.reactnativedatarizelogger

import android.content.Context
import androidx.preference.PreferenceManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.UUID


object DatarizeLogger {
  private const val KEY_UUID = "key.datarizelogger.uuid"

  private var appVersion: String? = null

  // screen size where app draws its contents (e.g. excluding bottom navigation bar height)
  private var screenResolution: String? = null

  private var deviceId: String? = null
  var userId: String? = null

  private fun createMetaEntity(context: Context): MetaEntity {
    ensureMeta(context)
    return MetaEntity(
      appVersion = appVersion,
      screenResolution = screenResolution,
      timestamp = System.currentTimeMillis(),
      deviceId = deviceId,
      userId = userId,
    )
  }

  private fun ensureMeta(context: Context) {
    if (appVersion == null) {
      val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
      appVersion = pInfo.versionName
    }

    if (screenResolution == null) {
      val displayMetrics = context.resources.displayMetrics
      val width = displayMetrics.widthPixels
      val height = displayMetrics.heightPixels
      screenResolution = "${width}x${height}"
    }

    if (deviceId == null) {
      val pref = PreferenceManager.getDefaultSharedPreferences(context)
      var uuid = pref.getString(KEY_UUID, null)
      if (uuid == null) {
        uuid = UUID.randomUUID().toString()
        pref.edit().putString(KEY_UUID, uuid).apply()
      }
      deviceId = uuid
    }
  }

  internal fun sendClickLog(context: Context, click: ClickEntity, impression: List<ViewEntity>) {
    val entity = ClickLogEntity(
      meta = createMetaEntity(context),
      click = click,
      impression = impression,
    )

    GlobalScope.launch {
      kotlin.runCatching {
        ApiServer.service.clickLog(entity)
      }
    }
  }

}
