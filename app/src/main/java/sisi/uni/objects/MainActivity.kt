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


class MainActivity : AppCompatActivity() {

    var COCKY = "00:16:53:18:8F:D3"

    //view element
    lateinit var statusBlueTv: TextView
    lateinit var pairedTv: TextView
    lateinit var onBtn: Button
    lateinit var offBtn: Button
    lateinit var pairedBtn: Button
    lateinit var connectBtn: Button
    lateinit var controllBtn: Button
    lateinit var blueAdapter: BluetoothAdapter

    //static bt connection
    companion object {
        var TAG = "ObjectS"
        lateinit var nxtConnection: NxtConnection
    }

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
        controllBtn = findViewById(R.id.controllBtn)
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
            nxtConnection.setBluetooth(NxtConnection.BT_ON)
            if (nxtConnection.connect()) {
                Log.d(TAG, "BT connected")
                statusBlueTv.setText(String.format("Connected to %s", nxtConnection.address))
            } else {
                Log.d(TAG, "BT connection failed")
                statusBlueTv.setText(String.format("No connection"))
            }
        }

        controllBtn.setOnClickListener {
            val intent = Intent(this, ControllActivity::class.java)
            startActivity(intent);
        }
    }

    fun showToast(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }
}
