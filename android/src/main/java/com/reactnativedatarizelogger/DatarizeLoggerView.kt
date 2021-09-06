package com.reactnativedatarizelogger

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import com.facebook.react.views.view.ReactViewGroup
import java.util.LinkedList


class DatarizeLoggerView(context: Context) : ReactViewGroup(context) {
  private val allChildViews: ArrayList<View> = arrayListOf()
  private val rect = Rect()

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)
    allChildViews.clear()
    allChildViews.addAll(getNestedChildViews())
  }

  private fun getNestedChildViews(): List<View> {
    val queue = LinkedList<View>()
    queue.add(this)
    val childViews = arrayListOf<View>()

    while (queue.isNotEmpty()) {
      val polled = queue.poll()
      if (polled is ViewGroup) {
        if (polled !is DatarizeLoggerView) {
          childViews.add(polled)
        }

        for (c in polled.children) {
          if (c is ViewGroup && c !is DatarizeLoggerView) {
            queue.add(c)
          } else {
            childViews.add(c)
          }
        }
      } else {
        childViews.add(polled!!)
      }
    }

    return childViews
  }

  override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
    val res = super.dispatchTouchEvent(ev)

    if (res && ev.actionMasked == MotionEvent.ACTION_UP) {
      val clickedViews = arrayListOf<View>()
      // get all pressed children and add to logging
      for (childView in allChildViews) {
        Log.d("hello", "child: $childView")
        if (childView.isPressed) {
          clickedViews.add(childView)
        }
      }

      if (clickedViews.isNotEmpty()) {
        // prepare to send click log
        val viewEntities: List<ViewEntity> = clickedViews.flatMap { v ->
          if (v is ViewGroup) {
            v.children.map { v2 -> createViewEntity(v2) }.asIterable()
          } else {
            listOf(createViewEntity(v))
          }
        }

        val clickEntity = ClickEntity(
          x = ev.rawX,
          y = ev.rawY,
          views = viewEntities,
        )

        DatarizeLogger.sendClickLog(context, clickEntity, listOf())
      }
    }

    return res
  }

  private fun createViewEntity(v: View): ViewEntity {
    val idName = if (v.id > 0) {
      try {
        resources.getResourceEntryName(v.id)
      } catch (e: Exception) {
        "NO_ID"
      }
    } else {
      "NO_ID"
    }

    if (!v.getGlobalVisibleRect(rect)) {
      rect.set(-1, -1, -1, -1)
    }

    val text = (v as? TextView)?.text?.takeIf { it.isNotEmpty() }?.toString()

    return ViewEntity(
      id = idName,
      left = rect.left,
      top = rect.top,
      right = rect.right,
      bottom = rect.bottom,
      text = text,
    )
  }
}
