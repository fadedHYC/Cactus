package com.gyf.cactus

import android.app.PendingIntent
import android.content.Context
import android.os.Build
import com.gyf.cactus.ext.isMain
import com.gyf.cactus.ext.register
import com.gyf.cactus.ext.registerCactus
import com.gyf.cactus.ext.registerJobCactus

/**
 * Cactus保活方案，Cactus有两种形式处理回调事件，
 * 第一种使用CactusCallback，
 * 第二种注册CACTUS_WORK和CACTUS_STOP广播监听器
 *
 * @author geyifeng
 * @date 2019-08-28 17:22
 */
class Cactus private constructor() {

    /**
     * 通知栏信息
     */
    private var mNotificationConfig = NotificationConfig()

    /**
     * 默认配置信息
     */
    private val mDefaultConfig = DefaultConfig()

    companion object {
        const val CACTUS_WORK = "com.gyf.cactus.work"
        const val CACTUS_STOP = "com.gyf.cactus.stop"
        internal const val CACTUS_TAG = "cactus"
        internal const val CACTUS_CONFIG = "cactusConfig"
        internal const val CACTUS_NOTIFICATION_CONFIG = "notificationConfig"
        internal val CALLBACKS = arrayListOf<CactusCallback>()
        internal var mCactusConfig = CactusConfig()
        @JvmStatic
        val instance by lazy { Cactus() }
    }

    /**
     * 设置通知栏信息
     * @param notificationConfig NotificationConfig
     * @return Cactus
     */
    fun setNotificationConfig(notificationConfig: NotificationConfig) = apply {
        mNotificationConfig = notificationConfig
    }

    /**
     * 设置PendingIntent，用来处理通知栏点击事件
     * @param pendingIntent PendingIntent
     * @return Cactus
     */
    fun setPendingIntent(pendingIntent: PendingIntent) = apply {
        mNotificationConfig.pendingIntent = pendingIntent
    }

    /**
     * 是否隐藏通知栏，只支持sdk N(包含N)以下版本
     * @param hide Boolean
     * @return Cactus
     */
    fun hideNotification(hide: Boolean) = apply {
        mNotificationConfig.hideNotification = hide
    }

    /**
     * 服务Id
     * @param serviceId Int
     * @return Cactus
     */
    fun setServiceId(serviceId: Int) = apply {
        mNotificationConfig.serviceId = serviceId
    }

    /**
     * 渠道Id
     * @param channelId String
     * @return Cactus
     */
    fun setChannelId(channelId: String) = apply {
        mNotificationConfig.channelId = channelId
    }

    /**
     * 渠道名
     * @param channelName String
     * @return Cactus
     */
    fun setChannelName(channelName: String) = apply {
        mNotificationConfig.channelName = channelName
    }

    /**
     * 通知栏标题
     * @param title String
     * @return Cactus
     */
    fun setTitle(title: String) = apply {
        mNotificationConfig.title = title
    }

    /**
     * 通知栏内容
     * @param content String
     * @return Cactus
     */
    fun setContent(content: String) = apply {
        mNotificationConfig.content = content
    }

    /**
     * 通知栏小图标
     * @param smallIcon Int
     * @return Cactus
     */
    fun setSmallIcon(smallIcon: Int) = apply {
        mNotificationConfig.smallIcon = smallIcon
    }

    /**
     * 通知栏大图标
     * @param largeIcon Int
     * @return Cactus
     */
    fun setLargeIcon(largeIcon: Int) = apply {
        mNotificationConfig.largeIcon = largeIcon
    }

    /**
     * 增加回调
     * @param cactusCallback CactusCallback
     * @return Cactus
     */
    fun addCallback(cactusCallback: CactusCallback) = apply {
        CALLBACKS.add(cactusCallback)
    }

    /**
     * 是否可以播放音乐
     * @param enabled Boolean
     * @return Cactus
     */
    fun setMusicEnabled(enabled: Boolean) = apply {
        mDefaultConfig.musicEnabled = enabled
    }

    /**
     * 设置自定义音乐
     * @param musicId Int
     * @return Cactus
     */
    fun setMusicId(musicId: Int) = apply {
        mDefaultConfig.musicId = musicId
    }

    /**
     * 设置音乐间隔时间，时间间隔越长，越省电
     * @param repeatInterval Long
     * @return Cactus
     */
    fun setMusicInterval(repeatInterval: Long) = apply {
        if (repeatInterval >= 0L) {
            mDefaultConfig.repeatInterval = repeatInterval
        }
    }

    /**
     * 是否可以使用一像素
     * @param enabled Boolean
     * @return Cactus
     */
    fun setOnePixEnabled(enabled: Boolean) = apply {
        mDefaultConfig.onePixEnabled = enabled
    }

    /**
     * 一像素模式，感觉没啥用
     * @param onePixModel OnePixModel
     * @return Cactus
     */
    fun setOnePixModel(onePixModel: OnePixModel) = apply {
        mDefaultConfig.onePixModel = onePixModel
    }

    /**
     * 是否Debug模式
     * @param isDebug Boolean
     * @return Cactus
     */
    fun isDebug(isDebug: Boolean) = apply {
        mDefaultConfig.debug = isDebug
    }

    /**
     * 必须调用，建议在Application里初始化，使用Kotlin扩展函数不需要调用此方法
     * @param context Context
     */
    fun register(context: Context) {
        context.apply {
            val cactusConfig = CactusConfig(
                mNotificationConfig,
                mDefaultConfig
            )
            mCactusConfig = cactusConfig
            context.register(cactusConfig)
        }
    }
}