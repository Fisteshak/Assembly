package com.rtuitlab.assemble.ui.qrprint

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun getQrCodeLabel(amount: Int): String {
    val rem100 = amount % 100
    val rem10 = amount % 10
    return when {
        rem100 in 11..14 -> "qr-кодов"
        rem10 == 1 -> "qr-код"
        rem10 in 2..4 -> "qr-кода"
        else -> "qr-кодов"
    }
}

@Composable
fun QrPrintFooter(
    amount: Int,
    onPrintClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Spacer(Modifier.height(14.dp))




        Text(
            "На печать будет отправлено $amount ${getQrCodeLabel(amount)}",
            modifier = Modifier,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(24.dp))


        Button(
            onClick = onPrintClick,
            modifier = Modifier.width(260.dp).height(45.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(10.dp),
            elevation = null,

            ) {
            Text(
                "Отправить на печать",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium
            )
        }


    }
}