package com.rozoomcool.limitly

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

data class AppInfo(val name: String, val packageName: String)

@Composable
fun FocusModeDialog(
    installedApps: List<AppInfo>,
    selectedApps: Map<String, Boolean>,
    onSelectionChange: (String, Boolean) -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Выберите приложения для блокировки") },
        text = {
            LazyColumn {
                items(installedApps) { app ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = selectedApps[app.packageName] ?: false,
                            onCheckedChange = { isChecked ->
                                onSelectionChange(app.packageName, isChecked)
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(app.name)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Готово")
            }
        }
    )
}


//@Composable
//fun FocusModeScreen() {
//    val context = LocalContext.current
//    var isFocusModeEnabled by remember { mutableStateOf(false) }
//    val allApps = listOf(
//        AppInfo("Facebook", "com.facebook.katana"),
//        AppInfo("Instagram", "com.instagram.android"),
//        AppInfo("Twitter", "com.twitter.android"),
//        AppInfo("Snapchat", "com.snapchat.android"),
//        AppInfo("YouTube", "com.google.android.youtube"),
//        AppInfo("ВКонтакте", "com.vkontakte.android"),
//        AppInfo("Одноклассники", "ru.ok.android"),
//        AppInfo("WeChat", "com.tencent.mm"),
//        AppInfo("TikTok", "com.zhiliaoapp.musically"),
//        AppInfo("Pinterest", "com.pinterest"),
//        AppInfo("Reddit", "com.reddit.frontpage"),
//        AppInfo("LinkedIn", "com.linkedin.android"),
//        AppInfo("Skype", "com.skype.raider"),
//        AppInfo("WhatsApp", "com.whatsapp"),
//        AppInfo("Telegram", "org.telegram.messenger"),
//        AppInfo("Badoo", "com.badoo.mobile"),
//        AppInfo("Tinder", "com.tinder"),
//        AppInfo("Viber", "com.viber.voip"),
//        AppInfo("Discord", "com.discord"),
//        AppInfo("Tumblr", "com.tumblr"),
//        AppInfo("KakaoTalk", "com.kakao.talk"),
//        AppInfo("LINE", "com.linecorp.line"),
//        AppInfo("Яндекс.Браузер", "com.yandex.browser"),
//        AppInfo("Opera", "com.opera.browser"),
//        AppInfo("Microsoft Edge", "com.microsoft.emmx"),
//        AppInfo("Google Chrome", "com.android.chrome")
//    )
//
//    // Состояние для сохранения выбранных приложений
//    var selectedApps by remember {
//        mutableStateOf(allApps.associate { it.packageName to false })
//    }
//    var showDialog by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Button(
//            onClick = { showDialog = true },
//            modifier = Modifier.padding(8.dp)
//        ) {
//            Text("Настроить Фокусировщик")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(
//            onClick = {
//                isFocusModeEnabled = !isFocusModeEnabled
//                if (isFocusModeEnabled) {
//                    // Включение службы доступности и передача списка блокируемых приложений
//                    enableAccessibilityService(context, selectedApps.filterValues { it }.keys.toList())
//                } else {
//                    // Отключение службы доступности
//                    disableAccessibilityService(context)
//                }
//            },
//            modifier = Modifier.padding(8.dp)
//        ) {
//            Text(if (isFocusModeEnabled) "Отключить Фокусировщик" else "Включить Фокусировщик")
//        }
//    }
//
//    // Отображение диалогового окна для выбора приложений
//    if (showDialog) {
//        AlertDialog(
//            onDismissRequest = { showDialog = false },
//            title = { Text("Выберите приложения для блокировки") },
//            text = {
//                LazyColumn {
//                    items(allApps) { app ->
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(vertical = 4.dp)
//                        ) {
//                            Checkbox(
//                                checked = selectedApps[app.packageName] ?: false,
//                                onCheckedChange = { isChecked ->
//                                    selectedApps = selectedApps.toMutableMap().apply {
//                                        this[app.packageName] = isChecked
//                                    }
//                                }
//                            )
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text(app.name)
//                        }
//                    }
//                }
//            },
//            confirmButton = {
//                TextButton(onClick = { showDialog = false }) {
//                    Text("Готово")
//                }
//            }
//        )
//    }
//}