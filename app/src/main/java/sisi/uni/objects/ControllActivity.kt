package sisi.uni.objects

import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_controll.*

class ControllActivity : AppCompatActivity() {

    lateinit var nxtConnection: NxtConnection
    lateinit var upBtn: Button
    lateinit var downBtn: Button
    lateinit var leftBtn: Button
    lateinit var rightBtn: Button
    lateinit var connectedView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controll)
        setSupportActionBar(toolbar)

        upBtn = findViewById(R.id.buttonUp)
        downBtn = findViewById(R.id.buttonDown)
        leftBtn = findViewById(R.id.buttonLeft)
        rightBtn = findViewById(R.id.buttonRight)
        connectedView = findViewById(R.id.connectedView)

        nxtConnection = MainActivity.nxtConnection

        upBtn.setOnClickListener {
            nxtConnection.send("up")
        }

        downBtn.setOnClickListener {
            nxtConnection.send("down")
        }

        leftBtn.setOnClickListener {
            nxtConnection.send("left")
        }

        rightBtn.setOnClickListener {
            nxtConnection.send("right")
        }

        if (nxtConnection.connected) {
            connectedView.setText(String.format("Connected to %s", nxtConnection.address))
        } else {
            connectedView.setText(String.format("No connection"))
        }
    }
}
