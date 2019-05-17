package sisi.uni.objects

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView
import android.hardware.Sensor.TYPE_ACCELEROMETER
import android.hardware.SensorEvent

class AccelerometerActivity : AppCompatActivity(), SensorEventListener {

    lateinit var displayTv: TextView
    lateinit var sensorManager: SensorManager
    private var lastUpdate: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accelerometer)

        displayTv = findViewById(R.id.displayTv)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        lastUpdate = System.currentTimeMillis()
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event)
        }
    }

    private fun getAccelerometer(event: SensorEvent) {
        val actualTime = event.timestamp
        if (actualTime - lastUpdate < 20000) {
            //flow control
            return
        }
        lastUpdate = actualTime

        // Movement
        val values = event.values
        val x = values[0]
        val y = values[1]
        val z = values[2]
        displayTv.text = "x=$x\n\ny=$y\n\nz=$z"


//        val accelationSquareRoot = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH)
//        if (accelationSquareRoot >= 2) {
//            lastUpdate = actualTime
//        }
    }
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

    }

    override fun onResume() {
        super.onResume()
        // register this class as a listener for the orientation and
        // accelerometer sensors
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onPause() {
        // unregister listener
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}
