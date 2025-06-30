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
            "На печать будет отправлено $amount qr-кода", // TODO обработать окончания
            modifier = Modifier,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(24.dp))


        Button(
            onClick = {

            },
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