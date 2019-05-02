package sisi.uni.objects

import android.os.Bundle
import android.support.v7.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_main.*
import android.bluetooth.BluetoothAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.content.Intent
import android.util.Log


class MainActivity : AppCompatActivity() {

    var TAG = "LeJOSDroid"
    var COCKY = "00:16:53:18:8F:D3"

    //view element
    lateinit var mStatusBlueTv: TextView
    lateinit var mPairedTv: TextView
    lateinit var mBlueIv: ImageView
    lateinit var mOnBtn: Button
    lateinit var mOffBtn: Button
    lateinit var mDiscoverBtn: Button
    lateinit var mPairedBtn: Button
    lateinit var mConnectBtn: Button
    lateinit var mControllBtn: Button
    lateinit var mBlueAdapter: BluetoothAdapter

    //static bt connection
    companion object {
        lateinit var mNxtConnection: NxtConnection

        fun getNxtConnection() : NxtConnection {
            return mNxtConnection
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //init
        mStatusBlueTv = findViewById(R.id.statusBluetoothTv)
        mPairedTv = findViewById(R.id.statusBluetoothTv)
        mBlueIv = findViewById(R.id.bluetoothIv)
        mOnBtn = findViewById(R.id.onBtn)
        mOffBtn = findViewById(R.id.offBtn)
        mDiscoverBtn = findViewById(R.id.discoverBtn)
        mPairedBtn = findViewById(R.id.pairedBtn)
        mConnectBtn = findViewById(R.id.connectBtn)
        mControllBtn = findViewById(R.id.controllBtn)
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter()
        mNxtConnection = NxtConnection(COCKY)

        mOnBtn.setOnClickListener {
            if (!mBlueAdapter.isEnabled) {
                showToast("Turning On Bluetooth...")
                //intent to on bluetooth
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent, 0)
            }
        }

        mDiscoverBtn.setOnClickListener {
            intent = Intent(this, DeviceActivity::class.java)
            startActivity(intent)
        }

        mOffBtn.setOnClickListener {
            if (mBlueAdapter.isEnabled()) {
                mBlueAdapter.disable();
                showToast("Turning Bluetooth Off");
//                mBlueIv.setImageResource(R.drawable.ic_action_off);
            } else {
                showToast("Bluetooth is already off");
            }
        }

        mPairedBtn.setOnClickListener {
            if (mBlueAdapter.isEnabled) {
                mPairedTv.text = "Paired Devices"
                val devices = mBlueAdapter.bondedDevices
                for (device in devices) {
                    mPairedTv.append("\nDevice: " + device.name + ", " + device)
                }
            } else {
                //bluetooth is off so can't get paired devices
                showToast("Turn on bluetooth to get paired devices")
            }
        }

        mConnectBtn.setOnClickListener {
            mNxtConnection.setBluetooth(NxtConnection.BT_ON)
            if(mNxtConnection.connect()) {
                Log.d(TAG, "BT connected")
            } else {
                Log.d(TAG, "BT connection failed")
            }
        }

        mControllBtn.setOnClickListener {
            val intent = Intent(this, ControllActivity::class.java)
            startActivity(intent);
        }
    }

    fun showToast(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }
}
