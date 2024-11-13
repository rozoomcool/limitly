package com.rozoomcool.limitly

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
    val context = LocalContext.current
    var usageStats by remember { mutableStateOf<List<UsageStats>>(emptyList()) }
    var permissionGranted by remember { mutableStateOf(false) }
    var totalUsageTime by remember { mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        permissionGranted = hasUsageStatsPermission(context)
        if (permissionGranted) {
            usageStats = getUsageStats(context)
            totalUsageTime = usageStats.sumOf { it.totalTimeInForeground }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Экранное время приложений") }
            )
        }
    ) { paddingValues ->
        Column {
            if (permissionGranted) {
                UsageStatsScreen(
                    totalUsageTime = totalUsageTime,
                    usageStats = usageStats,
                    modifier = Modifier.padding(paddingValues)
                )
            } else {
                PermissionRequestScreen {
                    context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                }
            }
        }
    }
}

@Composable
fun UsageStatsScreen(
    totalUsageTime: Long,
    usageStats: List<UsageStats>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        FocusModeToggleButton()
        Text(
            text = "Общее время использования: ${formatTime(totalUsageTime)}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        LazyColumn {
            items(usageStats.filter { it.totalTimeInForeground > 0 }
                .sortedByDescending { it.totalTimeInForeground }) { stat ->
                UsageStatsItem(
                    appName = stat.packageName,
                    usageTime = stat.totalTimeInForeground,
                    totalUsageTime = totalUsageTime
                )
            }
        }
    }
}

@Composable
fun UsageStatsItem(appName: String, usageTime: Long, totalUsageTime: Long) {
    val usagePercentage = if (totalUsageTime > 0) {
        usageTime.toFloat() / totalUsageTime
    } else {
        0f
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = appName.split(".").last().replaceFirstChar { it.uppercase() },
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "Время использования: ${formatTime(usageTime)}",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = usagePercentage,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
        Text(
            text = "${(usagePercentage * 100).toInt()}%",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
fun PermissionRequestScreen(onRequestPermission: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Для отображения экранного времени приложений необходимо предоставить разрешение.",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(onClick = onRequestPermission) {
            Text("Предоставить разрешение")
        }
    }
}

fun hasUsageStatsPermission(context: Context): Boolean {
    val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as android.app.AppOpsManager
    val mode = appOps.checkOpNoThrow(
        android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
        android.os.Process.myUid(),
        context.packageName
    )
    return mode == android.app.AppOpsManager.MODE_ALLOWED
}

fun getUsageStats(context: Context): List<UsageStats> {
    val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    val calendar = Calendar.getInstance()
    val endTime = calendar.timeInMillis
    calendar.add(Calendar.DAY_OF_YEAR, -1)
    val startTime = calendar.timeInMillis

    return usageStatsManager.queryUsageStats(
        UsageStatsManager.INTERVAL_DAILY,
        startTime,
        endTime
    ).orEmpty()
}

fun formatTime(timeInMillis: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(timeInMillis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMillis) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillis) % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}
