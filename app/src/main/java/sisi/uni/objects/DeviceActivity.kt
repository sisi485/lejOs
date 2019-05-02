package sisi.uni.objects

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter
import android.widget.ListView

import kotlinx.android.synthetic.main.activity_device.*

class DeviceActivity : AppCompatActivity() {

    lateinit var mBlueAdapter: BluetoothAdapter
    lateinit var mDeviceListView: ListView
    lateinit var mDeviceList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)
        setSupportActionBar(toolbar)

        mDeviceListView = findViewById(R.id.deviceListView)
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter()
        mDeviceList = ArrayList<String>()

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)

        if (mBlueAdapter.isDiscovering()) {
            mBlueAdapter.cancelDiscovery()
        }

        if (!mBlueAdapter.startDiscovery()) {

        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action
            when (action) {
                BluetoothDevice.ACTION_FOUND -> {
                    //Intent/ Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address // MAC address
                    if (mDeviceList.find { el -> el.contains(deviceHardwareAddress.toString())} != null) {
                        return
                    }
                    mDeviceList.add(deviceName + "\n" + deviceHardwareAddress)
                    mDeviceListView.setAdapter(
                        ArrayAdapter<String>(
                            context,
                            android.R.layout.simple_list_item_1,
                            mDeviceList
                        )
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(receiver)
    }
}
