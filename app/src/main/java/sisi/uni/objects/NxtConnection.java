package sisi.uni.objects;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.InputStreamReader;
import java.util.UUID;

public class NxtConnection {

    public boolean connected = false;

    private static final String TAG = "NxtConn";
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private String address;

    public static final boolean BT_ON = true;
    public static final boolean BT_OFF = false;

    public NxtConnection(String address) {
        Log.d(TAG, "MacAdr set to: " + address);
        this.address = address;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void drive(String direction) {
        Log.d(TAG, direction);
    }

    public void setBluetooth(boolean state) {
        Log.d(TAG, "Setting state to " + state);
        if (state == BT_ON) {
            // Check if bluetooth is off
            if (this.bluetoothAdapter.isEnabled()) {
                this.bluetoothAdapter.enable();
                while (!this.bluetoothAdapter.isEnabled()) {
                    //wait till connection is on
                }
                Log.d(TAG, "Bluetooth turned on");
            }
        }
        // Check if bluetooth is enabled
        else if (state == BT_OFF) {
            // Check if bluetooth is enabled
            if (this.bluetoothAdapter.isEnabled()) {
                this.bluetoothAdapter.disable();
                while (this.bluetoothAdapter.isEnabled()) {

                }
                Log.d(TAG, "Bluetooth turned off");
            }
        }
    }

    public boolean connect() {

        this.connected = false;
        BluetoothDevice nxt = this.bluetoothAdapter.getRemoteDevice(this.address);

        try {
            this.bluetoothSocket = nxt.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            this.bluetoothSocket.connect();
            connected = true;
            Log.d(TAG, "Connection to " + this.address + " established");

        } catch (Exception e) {
            Log.d(TAG, "Error connecting to BT Device, E:" + e.toString());
        }
        return connected;
    }

    public Integer readMessage() {
        Integer message;

        if (this.bluetoothSocket != null) {
            try {
                InputStreamReader input = new InputStreamReader(this.bluetoothSocket.getInputStream());
                message = input.read();
                Log.d(TAG, "Successfully read message");

            } catch (Exception e) {
                message = null;
                Log.d(TAG, "Couldn't read message");
            }
        } else {
            message = null;
            Log.d(TAG, "Couldn't read message");
        }
        return message;
    }
}
