package com.royaletracker

import android.app.Service
import android.content.Context
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.*
import android.widget.Button
import android.widget.TextView

class OverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var view: View

    private var elixir = 5

    override fun onBind(intent: android.content.Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        view = LayoutInflater.from(this).inflate(R.layout.overlay, null)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        val elixirText = view.findViewById<TextView>(R.id.elixirText)
        val plus = view.findViewById<Button>(R.id.plus)
        val minus = view.findViewById<Button>(R.id.minus)

        plus.setOnClickListener {
            elixir++
            elixirText.text = "Elixir: $elixir"
        }

        minus.setOnClickListener {
            elixir--
            elixirText.text = "Elixir: $elixir"
        }

        view.setOnTouchListener(object : View.OnTouchListener {
            var x = 0
            var y = 0
            var touchX = 0f
            var touchY = 0f

            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        x = params.x
                        y = params.y
                        touchX = event.rawX
                        touchY = event.rawY
                    }
                    MotionEvent.ACTION_MOVE -> {
                        params.x = x + (event.rawX - touchX).toInt()
                        params.y = y + (event.rawY - touchY).toInt()
                        windowManager.updateViewLayout(view, params)
                    }
                }
                return true
            }
        })

        windowManager.addView(view, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(view)
    }
}
