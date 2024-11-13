package com.rozoomcool.limitly

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

//@Composable
//fun FocusModeToggleButton() {
//    val context = LocalContext.current
//    var isFocusModeEnabled by remember { mutableStateOf(false) }
//
//    Button(
//        onClick = {
//            isFocusModeEnabled = !isFocusModeEnabled
//            if (isFocusModeEnabled) {
//                // Включение службы доступности
//                enableAccessibilityService(context)
//            } else {
//                // Отключение службы доступности
//                disableAccessibilityService(context)
//            }
//        },
//        modifier = Modifier.padding(8.dp)
//    ) {
//        Text(if (isFocusModeEnabled) "Отключить Фокусировщик" else "Включить Фокусировщик")
//    }
//}

@Composable
fun FocusModeToggleButton() {
    val context = LocalContext.current
    val installedApps = remember { getInstalledApps(context) }
    var selectedApps by remember {
        mutableStateOf(installedApps.associate { it.packageName to false })
    }
    var isFocusModeEnabled by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Настроить Фокусировщик")
        }

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = {
                isFocusModeEnabled = !isFocusModeEnabled
                if (isFocusModeEnabled) {
                    val blockedApps = selectedApps.filterValues { it }.keys.toList()
                    enableAccessibilityService(context, blockedApps)
                } else {
                    disableAccessibilityService(context)
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(if (isFocusModeEnabled) "Отключить Фокусировщик" else "Включить Фокусировщик")
        }
    }

    if (showDialog) {
        FocusModeDialog(
            installedApps = installedApps,
            selectedApps = selectedApps,
            onSelectionChange = { packageName, isChecked ->
                selectedApps = selectedApps.toMutableMap().apply {
                    this[packageName] = isChecked
                }
            },
            onDismissRequest = { showDialog = false }
        )
    }
}


fun enableAccessibilityService(context: Context, blockedApps: List<String>) {
    // Проверка, включена ли служба доступности
    if (!isAccessibilityServiceEnabled(context)) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        context.startActivity(intent)
    }

    // Включение службы только для заблокированных приложений
    // Убедитесь, что используется только список blockedApps
    // Логика работы с `blockedApps` в службе доступности
}

fun disableAccessibilityService(context: Context) {
    // Логика для отключения службы доступности
    // Важно: Программно отключить службу доступности невозможно, пользователь должен сделать это вручную
    // Можно уведомить пользователя о необходимости отключения службы в настройках
}

fun isAccessibilityServiceEnabled(context: Context): Boolean {
    // Проверка, включена ли служба доступности
    // Реализуйте эту функцию в соответствии с вашими требованиями
    return false
}
