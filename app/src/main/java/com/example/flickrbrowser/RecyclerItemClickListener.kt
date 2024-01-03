package com.example.flickrbrowser

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView

class RecyclerItemClickListener(private val context:Context,private val recyclerView:RecyclerView,listener:OnRecyclerItemClick):RecyclerView.SimpleOnItemTouchListener() {
    private val TAG = "RecyclerItemListener"

    interface OnRecyclerItemClick {
        fun onCLickItem(view: View,position:Int)
        fun onLongCLickItem(view: View,position:Int)
    }
    private val gestureDetector = GestureDetectorCompat(context,object : GestureDetector.SimpleOnGestureListener(){
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            Log.d(TAG,"onSingleTapUp() called")
            val child = recyclerView.findChildViewUnder(e.x,e.y)
            listener.onCLickItem(child!!,recyclerView.getChildAdapterPosition(child))
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            Log.d(TAG,"onLongPress() called")
            val child = recyclerView.findChildViewUnder(e.x,e.y)
            listener.onLongCLickItem(child!!,recyclerView.getChildAdapterPosition(child))
        }
    })
    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        Log.d(TAG, "onInterceptTouchEvent() with motion event $e")
        //        return super.onInterceptTouchEvent(rv, e)
        return gestureDetector.onTouchEvent(e)
    }
}