package com.example.im.app

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.media.SoundPool
import cn.bmob.v3.Bmob
import com.example.im.BuildConfig.DEBUG
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMOptions
import com.hyphenate.chat.EMTextMessageBody
import com.example.im.R
import com.example.im.adapter.EMMessageListenerAdapter
import com.example.im.ui.activity.ChatActivity
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class IMApplication : Application() {

    private val supportGyroscope = setOf("BMI160", "BMI120")

    companion object {
        lateinit var instance: IMApplication
    }

    val soundPool = SoundPool(2, AudioManager.STREAM_MUSIC, 0)

    val duan by lazy {
        soundPool.load(instance, R.raw.duan, 0)
    }

    val yulu by lazy {
        soundPool.load(instance, R.raw.yulu, 0)
    }

    val messageListener = object : EMMessageListenerAdapter() {
        override fun onMessageReceived(p0: MutableList<EMMessage>?) {
            //如果是在前台，则播放短的声音
            if (isForeground()) {
                soundPool.play(duan, 1f, 1f, 0, 0, 1f)
            } else {
                //如果在后台，则播放长的声音
                soundPool.play(yulu, 1f, 1f, 0, 0, 1f)
                showNotification(p0)
            }
        }
    }

    private fun showNotification(p0: MutableList<EMMessage>?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        p0?.forEach {
            var contentText = getString(R.string.no_text_message)
            if (it.type == EMMessage.Type.TXT) {
                contentText = (it.body as EMTextMessageBody).message
            }
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("username", it.conversationId())
//            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val taskStackBuilder =
                TaskStackBuilder.create(this).addParentStack(ChatActivity::class.java).addNextIntent(intent)
            val pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            val notification = Notification.Builder(this)
                .setContentTitle(getString(R.string.receive_new_message))
                .setContentText(contentText)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.avatar1))
                .setSmallIcon(R.mipmap.ic_contact)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .notification
            notificationManager.notify(1, notification)
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        //初始化
        EMClient.getInstance().init(applicationContext, EMOptions())
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(DEBUG)
        EMClient.getInstance().chatManager().addMessageListener(messageListener)

        Bmob.initialize(applicationContext, "480728efee030cac339cae26c9a3f2b1")

        initGyroscope()
        initAccelerometer()
    }

    fun supportGyroscope(): Boolean {
        val sManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor = sManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)  //Sensor type
        return when (sensor.name.split(" ")[0]) {
            in supportGyroscope -> true
            else -> false
        }
    }

    private fun initGyroscope() {
        val sManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor = sManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)  //Sensor type
        val mainContext = sensor.name.split(" ")[0]
//        gyroscopeInfo.text = "${sensor.name}"

//        when (mainContext) {
//            in supportGyroscope -> fullscreen_content.text = mainContext
//            else -> fullscreen_content.text = mainContext
//        }

//        fullscreen_content.text = mainContext
        sManager.registerListener(object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }

            override fun onSensorChanged(event: SensorEvent?) {
//                var values: FloatArray = event!!.values
//                gyroscopeX.text = "X: " + values[0].toString()
//                gyroscopeY.text = "Y: " + values[1].toString()
//                gyroscopeZ.text = "Z: " + values[2].toString()

            }
        }, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }


    private fun initAccelerometer() {

        var sManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        var sensor = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

//        accelerometerInfo.text = "${sensor.name} "

        sManager.registerListener(object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }

            override fun onSensorChanged(event: SensorEvent?) {
                var values: FloatArray = event!!.values
//                accelerometerX.text = "X: " + values[0].toString()
//                accelerometerY.text = "Y: " + values[1].toString()
//                accelerometerZ.text = "Z: " + values[2].toString()
            }


        }, sensor, SensorManager.SENSOR_DELAY_NORMAL)


    }

    private fun isForeground(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (runningAppProcess in activityManager.runningAppProcesses) {
            if (runningAppProcess.processName == packageName) {
                //找到了app的进程
                return runningAppProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
            }
        }
        return false
    }
}