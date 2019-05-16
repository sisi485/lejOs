package sisi.uni.objects

import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import kotlinx.android.synthetic.main.activity_main.*
import android.bluetooth.BluetoothAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.content.Intent
import android.util.Log

/**
 * Main activity.
 * @author Simon Schlätker
 *
 * This class handles the main activity, initialize click listener and is the first view shown, by starting the app.
 */
class MainActivity : AppCompatActivity() {
    /**
     * string to COCKY mac adr
     */
    var COCKY = "00:16:53:18:8F:D3"
    //view elements
    /**
     * statusBlueTv text view displays current state
     */
    lateinit var statusBlueTv: TextView
    /**
     * paired Button uses onClickListener to send kill signal
     */
    lateinit var pairedTv: TextView
    /**
     * on Button uses onClickListener to turn bt on
     */
    lateinit var onBtn: Button
    /**
     * off Button uses onClickListener to turn bt off
     */
    lateinit var offBtn: Button
    /**
     * paired Button uses onClickListener to show paired devices
     */
    lateinit var pairedBtn: Button
    /**
     * connect Button uses onClickListener to connect to COCKY
     */
    lateinit var connectBtn: Button
    /**
     * control Button uses onClickListener to start control activity
     */
    lateinit var controlBtn: Button
    /**
     * bt adapter used to turn bt on and off
     */
    lateinit var blueAdapter: BluetoothAdapter

    //static bt connection
    companion object {
        /**
         * TAG, logging depending information
         */
        var TAG = "ObjectS"
        /**
         * nxtConnection, single object to share with other activities
         */
        lateinit var nxtConnection: NxtConnection
    }

    /**
     * On create function, called by android if the page shows up.
     * Initializes onclick events and creates depending objects(nxtConnection..).
     *
     * @param savedInstanceState    android based information
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //init
        statusBlueTv = findViewById(R.id.statusBluetooth)
        pairedTv = findViewById(R.id.statusBluetooth)
        onBtn = findViewById(R.id.onBtn)
        offBtn = findViewById(R.id.offBtn)
        pairedBtn = findViewById(R.id.pairedBtn)
        connectBtn = findViewById(R.id.connectBtn)
        controlBtn = findViewById(R.id.controlBtn)
        blueAdapter = BluetoothAdapter.getDefaultAdapter()
        nxtConnection = NxtConnection(COCKY)

        onBtn.setOnClickListener {
            if (!blueAdapter.isEnabled) {
                showToast("Turning On Bluetooth...")
                //intent to on bluetooth
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent, 0)
            }
        }

        offBtn.setOnClickListener {
            if (blueAdapter.isEnabled()) {
                blueAdapter.disable();
                showToast("Turning Bluetooth Off");
            } else {
                showToast("Bluetooth is already off");
            }
        }

        pairedBtn.setOnClickListener {
            if (blueAdapter.isEnabled) {
                pairedTv.text = "Paired Devices"
                val devices = blueAdapter.bondedDevices
                for (device in devices) {
                    pairedTv.append("\nDevice: " + device.name + ", " + device)
                }
            } else {
                //bluetooth is off so can't get paired devices
                showToast("Turn on bluetooth to get paired devices")
            }
        }

        connectBtn.setOnClickListener {
            nxtConnection.setState(NxtConnection.BT_ON)
            if (nxtConnection.connect()) {
                Log.d(TAG, "BT connected")
                statusBlueTv.setText(String.format("Connected to %s", nxtConnection.address))
            } else {
                Log.d(TAG, "BT connection failed")
                statusBlueTv.setText(String.format("No connection"))
            }
        }

        controlBtn.setOnClickListener {
            val intent = Intent(this, ControlActivity::class.java)
            startActivity(intent);
        }
    }


    /**
     * Prints depending string to the user.
     *
     * @param str   String to toast
     */
    private fun showToast(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }
}
