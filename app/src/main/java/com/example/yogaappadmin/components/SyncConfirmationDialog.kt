package com.example.yogaappadmin.components

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun SyncConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit, hasInternet: Boolean) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = if (hasInternet) "Sync Confirmation" else "No Internet Connection", color = Color(0xFF6600CC), fontSize = 30.sp, fontWeight = FontWeight.Bold) },
        text = {
            Text(
                text = if (hasInternet) {
                    "Are you certain you want to proceed with syncing to Firestore?"
                } else {
                    "Please verify your internet connection and try again."
                },
                color = Color(0xFF6600CC),
                fontSize = 20.sp
            )
        },

        confirmButton = {
            if (hasInternet) {
                TextButton(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))

                ) {
                    Text("Yes", fontSize = 20.sp)
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5F5D5D))
            ) {
                Text(if (hasInternet) "No" else "OK", fontSize = 20.sp)
            }
        }
    )
}

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        else -> false
    }
}