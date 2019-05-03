package sisi.uni.objects

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_controll.*

class ControllActivity : AppCompatActivity(), View.OnTouchListener {

    var TAG = "${MainActivity.TAG} - Ctl"

    lateinit var nxtConnection: NxtConnection
    lateinit var upBtn: Button
    lateinit var downBtn: Button
    lateinit var leftBtn: Button
    lateinit var rightBtn: Button
    lateinit var initBtn: Button
    lateinit var ctrlBtn: Button
    lateinit var autoBtn: Button
    lateinit var connectedView: TextView

    //TODO luci
    @SuppressLint("ClickableViewAccessibility")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controll)
        setSupportActionBar(toolbar)

        upBtn = findViewById(R.id.buttonUp)
        downBtn = findViewById(R.id.buttonDown)
        leftBtn = findViewById(R.id.buttonLeft)
        rightBtn = findViewById(R.id.buttonRight)
        initBtn = findViewById(R.id.buttonInit)
        ctrlBtn = findViewById(R.id.buttonCont)
        autoBtn = findViewById(R.id.buttonAuto)
        connectedView = findViewById(R.id.connectedView)

        nxtConnection = MainActivity.nxtConnection

        upBtn.setOnTouchListener(this)
        downBtn.setOnTouchListener(this)
        leftBtn.setOnTouchListener(this)
        rightBtn.setOnTouchListener(this)
        initBtn.setOnTouchListener(this)
        ctrlBtn.setOnTouchListener(this)
        autoBtn.setOnTouchListener(this)

        if (nxtConnection.connected) {
            connectedView.setText(String.format("Connected to %s", nxtConnection.address))
        } else {
            connectedView.setText(String.format("No connection"))
        }
    }

    override fun onTouch(v: View, m: MotionEvent): Boolean {
        val cmd: String
        val b = v as Button

        when (m.action) {
            MotionEvent.ACTION_DOWN -> {
                cmd = b.text[0].toUpperCase().toString()
            }
            MotionEvent.ACTION_UP -> {
                cmd = b.text[0].toLowerCase().toString()
            }
            else -> {
                v.performClick()
                return true
            }
        }
        nxtConnection.send(cmd)
        return true
    }
}
