package com.rtuitlab.assemble.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import assembly.composeapp.generated.resources.Res
import assembly.composeapp.generated.resources.volume_icon
import com.rtuitlab.assemble.domain.entities.Assemble
import org.jetbrains.compose.resources.painterResource

@Composable
fun AssembleCard(
    assemble: Assemble,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFEEF1F4),
    contentColor: Color = Color.DarkGray
) {
    Box(
        modifier = Modifier
            .width(190.dp).height(120.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {

        Column(
            modifier = Modifier .padding(10.dp)

        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "№${assemble.assembleId}",

                    style = MaterialTheme.typography.titleMedium,
                    color = contentColor
                )
                if (assemble.linkToSound != null) {
                    Icon(
                        painter = painterResource(resource = Res.drawable.volume_icon),
                        contentDescription = "Audio Indicator",
                        tint = Color.Unspecified,
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = assemble.name,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = contentColor,
                modifier = Modifier.align(Alignment.Start)
            )
        }
    }

}


@Composable
fun PlaceholderAssembleCard(modifier: Modifier = Modifier) {
    Box(modifier = modifier.height(120.dp).width(190.dp))
}