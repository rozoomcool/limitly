package com.rozoomcool.limitly

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class AppBlockerService(
//    val blockedApps: List<String>
) : AccessibilityService() {

    val blockedApps = listOf(
        "com.facebook.katana", // Facebook
        "com.instagram.android", // Instagram
        "com.twitter.android", // Twitter
        "com.snapchat.android", // Snapchat
        "com.google.android.youtube", // YouTube
        "com.vkontakte.android", // ВКонтакте
        "ru.ok.android", // Одноклассники
        "com.tencent.mm", // WeChat
        "com.zhiliaoapp.musically", // TikTok
        "com.pinterest", // Pinterest
        "com.reddit.frontpage", // Reddit
        "com.linkedin.android", // LinkedIn
        "com.skype.raider", // Skype
        "com.whatsapp", // WhatsApp
        "org.telegram.messenger", // Telegram
        "com.badoo.mobile", // Badoo
        "com.tinder", // Tinder
        "com.viber.voip", // Viber
        "com.discord", // Discord
        "com.snapchat.android", // Snapchat
        "com.tumblr", // Tumblr
        "com.kakao.talk", // KakaoTalk
//        "com.linecorp.line", // LINE
//        "com.yandex.browser", // Яндекс.Браузер
//        "com.opera.browser", // Opera
//        "com.microsoft.emmx", // Microsoft Edge
//        "com.android.chrome" // Google Chrome
    )

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString()
            if (packageName != null && blockedApps.contains(packageName)) {
                // Блокируем приложение
                performGlobalAction(GLOBAL_ACTION_HOME)
                // Здесь можно добавить уведомление пользователю о блокировке
            }
        }
    }

    override fun onInterrupt() {
        Log.d("::::::::::", "APP_BLOCKER_SERVICE INTERRUPTED")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        }
        this.serviceInfo = info
    }
}

@SuppressLint("QueryPermissionsNeeded")
fun getInstalledApps(context: Context): List<AppInfo> {
    val pm = context.packageManager
    return pm.getInstalledApplications(0).filter { app ->
        (app.flags and ApplicationInfo.FLAG_SYSTEM) == 0 // Исключаем системные приложения
    }.map { app ->
        AppInfo(
            name = pm.getApplicationLabel(app).toString(),
            packageName = app.packageName
        )
    }
}