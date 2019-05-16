package sisi.uni.objects

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.UTFDataFormatException
import java.util.*

/**
 * Nxt connection.
 * @author Simon Schlätker
 *
 * This class handles the bt connection to Cocky and provides functions to write or read msg.
 */
class NxtConnection(var address: String) {


    /**
     * Connection state, bluetooth adapter and depending socket.
     */
    var connected = false
    private val TAG = MainActivity.TAG + " - NxtConn"
    private val bluetoothAdapter: BluetoothAdapter
    private var bluetoothSocket: BluetoothSocket? = null

    init {
        Log.d(TAG, "MacAdr set to: $address")
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    }

    /**
     * Sets the bt state.
     *
     * @param state         Boolean to activate or deactivate the bt connection.
     */
    fun setState(state: Boolean) {
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


    /**
     * Connect to depending mac adr.
     *
     * @return          Boolean which indicates the success.
     */
    fun connect(): Boolean {
        this.connected = false
        val nxt = this.bluetoothAdapter.getRemoteDevice(this.address)

        try {
            this.bluetoothSocket =
                nxt.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
            this.bluetoothSocket!!.connect()
            connected = true
            Log.d(TAG, "connection to " + this.address + " established")

        } catch (e: Exception) {
            Log.d(TAG, "error connecting to BT device, E:$e")
        }
        return connected
    }

    /**
     * Wrapper function, to send a command to my bt connection and create log.
     *
     * @param command   Command to be send.
     * @return          Boolean which indicates the success.
     */
    fun send(command: BtCommand?) = when (writeMessage(command!!.ordinal)) {
        true -> {
            Log.d(TAG, "successfully wrote message $command")
        }
        false -> {
            Log.d(TAG, "error sending $command")
        }
    }


    /**
     * Sends a command to my bt connection.
     *
     * @param msg       Int to be send.
     * @return          Boolean which indicates the success.
     */
    private fun writeMessage(msg: Int): Boolean {
        try {
            val writer = OutputStreamWriter(this.bluetoothSocket!!.outputStream)
            writer.write(msg)
            writer.flush()
            return true
        } catch (e: Exception) {
            throw e
        }
    }


    /**
     * Reads a command from my bt connection.
     *
     * @return          Int which represents the commands.
     */
    fun readMessage(): Int? {
        var message: Int?
        try {
            val input = InputStreamReader(this.bluetoothSocket!!.inputStream)
            message = input.read()
            Log.d(TAG, "Successfully read message")
        } catch (e: Exception) {
            message = null
            Log.d(TAG, "Couldn't read message")
        }
        return message
    }

    companion object {
        val BT_ON = true
        val BT_OFF = false
    }
}
