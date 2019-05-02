package sisi.uni.objects

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.widget.Button

import kotlinx.android.synthetic.main.activity_controll.*

class ControllActivity : AppCompatActivity() {

    lateinit var mNxtConnection: NxtConnection
    lateinit var mUpBtn: Button
    lateinit var mDownBtn: Button
    lateinit var mLeftBtn: Button
    lateinit var mRightBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controll)
        setSupportActionBar(toolbar)

        mUpBtn = findViewById(R.id.buttonUp)
        mDownBtn = findViewById(R.id.buttonDown)
        mLeftBtn = findViewById(R.id.buttonLeft)
        mRightBtn = findViewById(R.id.buttonRight)

        mNxtConnection = MainActivity.getNxtConnection()

        mUpBtn.setOnClickListener {
            mNxtConnection.drive("up");
        }

        mDownBtn.setOnClickListener {
            mNxtConnection.drive("down");
        }

        mLeftBtn.setOnClickListener {
            mNxtConnection.drive("left");
        }

        mRightBtn.setOnClickListener {
            mNxtConnection.drive("right");
        }
    }

}
