package com.view.musicplayer.spotifyclone.ext

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationManagerCompat
import com.view.musicplayer.spotifyclone.R


fun Activity.checkNotificationPermission(onGranted: () -> Unit = {}) {
    if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
        showAllowNotificationPermissionDialog()
    } else {
        onGranted()
    }
}

fun Activity.showAllowNotificationPermissionDialog(onCancel: () -> Unit = {}) {
    AlertDialog.Builder(this)
        .setTitle(getString(R.string.permission_title))
        .setMessage(getString(R.string.permission_detail))
        .setPositiveButton(getString(R.string.confirm)) { _, _ ->
            openNotificationSetting(this)
        }
        .setNegativeButton(getString(R.string.cancel)) { _, _ ->
            onCancel()
        }
        .show()
}

private fun openNotificationSetting(activity: Activity) {
    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.putExtra(Settings.EXTRA_APP_PACKAGE, activity.packageName)
    activity.startActivity(intent)
}