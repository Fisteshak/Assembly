package com.rtuitlab.assemble

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import com.rtuitlab.assemble.ui.assemble.AssembleFooter
import com.rtuitlab.assemble.ui.assemble.AssembleHeader
import com.rtuitlab.assemble.ui.assemble.AssembleScreen


@Preview
@Composable
fun ComponentRowPreview() {
//    AssembleComponentRow(
//        createSampleAssembles(1)[0].components!![0],
//        {},
//        {},
//        {},
//
//        MaterialTheme.colorScheme.primaryContainer,
//        MaterialTheme.colorScheme.onPrimaryContainer
//    )
}

@Preview
@Composable
fun AssembliesListPreview() {
    // AssembleComponentsList(createSampleAssembles(3)[0].components!!)
}

@Preview
@Composable
fun AssembliesScreenPreview() {
    AssembleScreen(5)
}


@Preview
@Composable
fun AssembleHeaderPreview() {
    AssembleHeader("Сборка 1", {}, {}, {})
}

@Preview
@Composable
fun AssembleFooterPreview() {
    AssembleFooter({}, {})
}
