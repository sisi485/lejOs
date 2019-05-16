package sisi.uni.objects

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_control.*
import java.lang.Exception

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
            nxtConnection.send(cmd)
        } catch (e: Exception) {
            Log.d(TAG, "exception occurred by sending $cmd, $e")
        }
        return true
    }
}
