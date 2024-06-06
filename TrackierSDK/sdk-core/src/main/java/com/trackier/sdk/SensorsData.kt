package com.trackier.sdk

import android.app.Activity
import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LifecycleObserver
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SensorsData: LifecycleObserver, Activity() {
    
    companion object {
        lateinit var sensorManager: SensorManager
        private var gyroscopeSensor: Sensor? = null
        private var magnetometer: Sensor? = null
    
        fun init(application: Application) {
            Log.d("trackiersdk", "Gyroscope data init ")
            sensorManager = application.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
            registerActivityLifecycleCallbacks(application)
        }
    
        private fun registerActivityLifecycleCallbacks(application: Application) {
            application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    Log.d("trackiersdk","onActivityCreated -------------")
                }
            
                override fun onActivityStarted(activity: Activity) {
                    Log.d("trackiersdk","onActivityStarted -------------")
                }
            
                override fun onActivityResumed(activity: Activity) {
                    Log.d("trackiersdk","onActivityResumed -------------")
                    if (gyroscopeSensor != null) {
                        sensorManager.registerListener(listener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL)
                        sensorManager.registerListener(listener , magnetometer, SensorManager.SENSOR_DELAY_NORMAL
                        )
                        //sensorManager.registerListener( this as SensorEventListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL )
                    }
                   
                }
            
                override fun onActivityPaused(activity: Activity) {}
            
                override fun onActivityStopped(activity: Activity) {}
            
                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            
                override fun onActivityDestroyed(activity: Activity) {}
            })
        }
    
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val sVS = JSONArray()
                val jsonObj = JSONObject()
                val floatValues = event!!.values
                Log.d("trackiersdk","SensorEvent -------------")
                when (event.sensor.type) {
                    Sensor.TYPE_GYROSCOPE -> {
                        Log.d("trackiersdk","TYPE_GYROSCOPE -------------")
                        val timestamp = System.currentTimeMillis()
                        try {
                            jsonObj.put("sT", timestamp)
                            for (gyroValue in floatValues) {
                                sVS.put(gyroValue)
                            }
                            jsonObj.put("sVS", sVS)
                            jsonObj.put("sV", event.sensor.name)
                            jsonObj.put("sN", event.sensor.stringType)
                
                            // Convert the JSONObject to string
                            val json = jsonObj.toString()
                            Log.d("trackiersdk", "Gyroscope data $json")
                            println("Gyroscope data: $json")
                
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                    Sensor.TYPE_MAGNETIC_FIELD -> {
                        try {
                            jsonObj.put("sT", event.timestamp)
                            for (value in floatValues) {
                                sVS.put(value)
                            }
                            jsonObj.put("sVS", sVS)
                
                            jsonObj.put("sV", event.sensor.name)
                            jsonObj.put("sN", event.sensor.stringType)
                
                            println(jsonObj.toString(4))
                            Log.d("trackiersdk", "TYPE_MAGNETIC_FIELD data ${jsonObj.toString()}") // Print with indentation for readability
                
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
    
            override fun onAccuracyChanged(sensor: Sensor, acc: Int) {
                // Do nothing for this implementation
            }
        }
    }
}