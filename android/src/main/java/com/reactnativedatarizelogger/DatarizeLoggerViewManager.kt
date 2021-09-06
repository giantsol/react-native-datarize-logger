package com.reactnativedatarizelogger

import android.graphics.Color
import android.view.View
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.annotations.ReactProp

class DatarizeLoggerViewManager : ViewGroupManager<DatarizeLoggerView>() {
  override fun getName() = "DatarizeLoggerView"

  override fun createViewInstance(reactContext: ThemedReactContext): DatarizeLoggerView {
    return DatarizeLoggerView(reactContext)
  }

  @ReactProp(name = "color")
  fun setColor(view: View, color: String) {
    view.setBackgroundColor(Color.parseColor(color))
  }
}
