package sisi.uni.objects

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

class NxtConnection(var address: String) {
    var connected = false

    private val TAG = MainActivity.TAG + " - NxtConn"
    private val bluetoothAdapter: BluetoothAdapter
    private var bluetoothSocket: BluetoothSocket? = null

    init {
        Log.d(TAG, "MacAdr set to: $address")
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    }

    fun send(direction: String) {
        Log.d(TAG, direction)
        if (!writeMessage(direction[0].toInt())) {
            Log.d(TAG, "error sending $direction")
        }
    }

    fun setBluetooth(state: Boolean) {
        Log.d(TAG, "Setting state to $state")
        if (state == BT_ON) {
            // Check if bluetooth is off
            if (this.bluetoothAdapter.isEnabled) {
                this.bluetoothAdapter.enable()
                while (!this.bluetoothAdapter.isEnabled) {
                    //wait till connection is on
                }
                Log.d(TAG, "Bluetooth turned on")
            }
        } else if (state == BT_OFF) {
            // Check if bluetooth is enabled
            if (this.bluetoothAdapter.isEnabled) {
                this.bluetoothAdapter.disable()
                while (this.bluetoothAdapter.isEnabled) {

                }
                Log.d(TAG, "Bluetooth turned off")
            }
        }
    }

    fun connect(): Boolean {
        this.connected = false
        val nxt = this.bluetoothAdapter.getRemoteDevice(this.address)

        try {
            this.bluetoothSocket =
                nxt.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
            this.bluetoothSocket!!.connect()
            connected = true
            Log.d(TAG, "Connection to " + this.address + " established")

        } catch (e: Exception) {
            Log.d(TAG, "Error connecting to BT Device, E:$e")
        }
        return connected
    }

    fun readMessage(): Int? {
        var message: Int?

        if (this.bluetoothSocket != null) {
            try {
                val input = InputStreamReader(this.bluetoothSocket!!.inputStream)
                message = input.read()
                Log.d(TAG, "Successfully read message")
            } catch (e: Exception) {
                message = null
                Log.d(TAG, "Couldn't read message")
            }

        } else {
            message = null
            Log.d(TAG, "Couldn't read message")
        }
        return message
    }

    private fun writeMessage(msg: Int): Boolean {
        if (this.bluetoothSocket != null) {
            try {
                val writer = OutputStreamWriter(this.bluetoothSocket!!.outputStream)
                writer.write(msg)
                writer.flush()
                Log.d(TAG, "Successfully wrote message")
                return true
            } catch (e: Exception) {
                Log.d(TAG, "Couldn't write message")
            }
        } else {
            Log.d(TAG, "Couldn't write message, no connetion")
        }
        return false
    }

    companion object {
        val BT_ON = true
        val BT_OFF = false
    }
}
