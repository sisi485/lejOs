package sisi.uni.objects

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.text.method.MovementMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_control.*
import java.lang.Exception
import java.util.*
import android.widget.Toast
import com.intel.bluetooth.RemoteDeviceHelper.getAddress
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import java.io.BufferedInputStream
import java.lang.Thread.sleep


/**
 * Control activity.
 * @author Simon Schlätker
 *
 * This class handles the control activity, initialize touch listener and maps the bt commands.
 */
class ControlActivity : AppCompatActivity(), View.OnTouchListener {

    /**
     * TAG, logging depending information
     */
    private var TAG = "${MainActivity.TAG} - CtrlActivity"

    /**
     * enum class to indicate whether direction is up or down
     */
    enum class Direction {
        /**
         * UP, direction up
         */
        UP,
        /**
         * DOWN, direction down
         */
        DOWN
    }

    /**
     * nxtConnection object, represents bt service
     */
    lateinit var nxtConnection: NxtConnection
    /**
     * up Button object uses onTouchListener to drive upside
     */
    lateinit var upBtn: Button
    /**
     * down Button uses onTouchListener to drive downside
     */
    lateinit var downBtn: Button
    /**
     * left Button uses onTouchListener to drive left
     */
    lateinit var leftBtn: Button
    /**
     * right Button uses onTouchListener to drive right
     */
    lateinit var rightBtn: Button
    /**
     * init Button uses onTouchListener to calibrate
     */
    lateinit var initBtn: Button
    /**
     * control Button uses onTouchListener to drive in app mode
     */
    lateinit var ctrlBtn: Button
    /**
     * auto Button uses onTouchListener to drive in auto mode
     */
    lateinit var autoBtn: Button
    /**
     * speed up Button uses onTouchListener to set the speed up
     */
    lateinit var speedUpBtn: Button
    /**
     * slow down Button uses onTouchListener to lower the speed
     */
    lateinit var slowDownBtn: Button
    /**
     * stop Button uses onTouchListener to send stop signal
     */
    lateinit var stopBtn: Button
    /**
     * kill Button uses onTouchListener to send kill signal
     */
    lateinit var killBtn: Button
    /**
     * kill Button uses onTouchListener to send kill signal
     */
    lateinit var killBtBtn: Button
    /**
     * connected text view displays current state
     */
    lateinit var connectedView: TextView

    @SuppressLint("ClickableViewAccessibility")

    /**
     * On create function, called by android if the page shows up.
     *
     * @param savedInstanceState    android based information
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)
        setSupportActionBar(toolbar)

        upBtn = findViewById(R.id.buttonUp)
        downBtn = findViewById(R.id.buttonDown)
        leftBtn = findViewById(R.id.buttonLeft)
        rightBtn = findViewById(R.id.buttonRight)
        initBtn = findViewById(R.id.buttonInit)
        ctrlBtn = findViewById(R.id.buttonCont)
        autoBtn = findViewById(R.id.buttonAuto)
        speedUpBtn = findViewById(R.id.buttonSpeedUp)
        slowDownBtn = findViewById(R.id.buttonSlowDown)
        stopBtn = findViewById(R.id.buttonStop)
        killBtn = findViewById(R.id.buttonKill)
        killBtBtn = findViewById(R.id.buttonKillBt)
        connectedView = findViewById(R.id.connectedView)

        nxtConnection = MainActivity.nxtConnection

        upBtn.setOnTouchListener(this)
        downBtn.setOnTouchListener(this)
        leftBtn.setOnTouchListener(this)
        rightBtn.setOnTouchListener(this)
        initBtn.setOnTouchListener(this)
        ctrlBtn.setOnTouchListener(this)
        autoBtn.setOnTouchListener(this)
        speedUpBtn.setOnTouchListener(this)
        slowDownBtn.setOnTouchListener(this)
        stopBtn.setOnTouchListener(this)
        killBtn.setOnTouchListener(this)
        killBtBtn.setOnTouchListener(this)

        if (nxtConnection.connected) {
            connectedView.setText(String.format("Connected to %s", nxtConnection.address))
        } else {
            connectedView.setText(String.format("No connection"))
        }
    }

    /**
     * Parse commands for bluetooth connection, called by on touch listener
     *
     * @param btn   android button object
     * @param d     direction up or down, depend on whether the buttons is pressed or released
     */
    private fun parseCmd(btn: Button, d: Direction): BtCommand? {
        if (d == Direction.UP) {
            if (btn == upBtn || btn == downBtn || btn == leftBtn || btn == rightBtn) {
                return BtCommand.HALT
            } else {
                return null
            }
        }

        if (d == Direction.DOWN) {
            when (btn) {
                upBtn -> {
                    return BtCommand.FAHRE_VORWAERTS
                }
                downBtn -> {
                    return BtCommand.FAHRE_RUECKWAERTS
                }
                leftBtn -> {
                    return BtCommand.FAHRE_LINKS
                }
                rightBtn -> {
                    return BtCommand.FAHRE_RECHTS
                }
                initBtn -> {
                    return BtCommand.KALIBRIEREN
                }
                ctrlBtn -> {
                    return BtCommand.BTSTART
                }
                speedUpBtn -> {
                    return BtCommand.GESCHWINDIGKEIT_HOCH
                }
                slowDownBtn -> {
                    return BtCommand.GESCHINDIGKEIT_RUNTER
                }
                stopBtn -> {
                    return BtCommand.STOP
                }
                killBtn -> {
                    return BtCommand.KILL
                }
                killBtBtn -> {
                    return BtCommand.OK
                }
            }
        }
        return BtCommand.AUTOSTART
    }

    /**
     * On touch listener for all view elements in this activity.
     *
     * @param v     view element
     * @param m     motion event like button pressed, released...
     */
    override fun onTouch(v: View, m: MotionEvent): Boolean {
        val cmd: BtCommand?
        val b = v as Button

        when (m.action) {
            MotionEvent.ACTION_DOWN -> {
                cmd = parseCmd(b, Direction.DOWN)
            }
            MotionEvent.ACTION_UP -> {
                cmd = parseCmd(b, Direction.UP)
            }
            else -> {
                v.performClick()
                return true
            }
        }
        try {
            if (cmd != BtCommand.OK) {
                nxtConnection.send(cmd)
            } else {
                if (m.action == MotionEvent.ACTION_DOWN) {
                    toggleBtKill()
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "exception occurred by sending $cmd, $e")
        }
        return true
    }


    /**
     * Toggle bt kill.
     *
     */
    private fun toggleBtKill() {
        var bluetoothSocket: BluetoothSocket
        val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val mReceiver = SingBroadcastReceiver()
        bluetoothAdapter.startDiscovery()

        //let's make a broadcast receiver to register our things
        val ifilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        this.registerReceiver(mReceiver, ifilter)

    }

    private fun connectBt(address: String, bluetoothSocket: BluetoothSocket, bluetoothAdapter: BluetoothAdapter) {
        try {
            var lBtSocked = bluetoothSocket
            val nxt = bluetoothAdapter.getRemoteDevice(address)

            lBtSocked =
                nxt.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
            lBtSocked.connect()
            Log.d(TAG, "connection to " + address + " established")

        } catch (e: Exception) {
            Log.d(TAG, "error connecting to BT device, E:$e")
        }
    }


    class Macchinetta(var cmd: String) : Thread() {

        init {
            run()
            super.start()
        }

        override fun run() {
            try {

            } finally {

            }
        }


    }

    private inner class SingBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action //may need to chain this to a recognizing function
            if (BluetoothDevice.ACTION_FOUND == action) {
                // Get the BluetoothDevice object from the Intent
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if (device.address.startsWith(NxtConnection.PREFIX)) {
                    if(device.address != MainActivity.COCKY) {
                        NxtConnection.macAdr.add(device.address)
                        Log.d(TAG, "adding ${device.address}")
                    }
                }
            }
        }
    }
}
