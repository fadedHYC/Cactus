package com.gyf.cactus.service

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.gyf.cactus.Cactus
import com.gyf.cactus.CactusConfig
import com.gyf.cactus.ICactusInterface
import com.gyf.cactus.ext.setNotification
import com.gyf.cactus.ext.startLocalService

/**
 * 远程服务
 * @author geyifeng
 * @date 2019-08-28 17:05
 */
class RemoteService : Service() {

    /**
     * 配置信息
     */
    private lateinit var mCactusConfig: CactusConfig

    private lateinit var remoteBinder: RemoteBinder

    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            startLocalService(this, true, mCactusConfig)
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            service?.let {
                ICactusInterface.Stub.asInterface(it)
                    ?.apply {
                        wakeup(mCactusConfig)
                    }
            }
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        intent.getParcelableExtra<CactusConfig>(Cactus.CACTUS_CONFIG)?.let {
            mCactusConfig = it
        }
        setNotification(mCactusConfig.notificationConfig)
        startLocalService(mServiceConnection)
        log("RemoteService is run")
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        remoteBinder = RemoteBinder()
        return remoteBinder
    }

    inner class RemoteBinder : ICactusInterface.Stub() {

        override fun wakeup(config: CactusConfig) {
            setNotification(config.notificationConfig)
            mCactusConfig = config
        }
    }

    private fun log(msg: String) {
        if (mCactusConfig.defaultConfig.debug) {
            Log.d(Cactus.CACTUS_TAG, msg)
        }
    }
}