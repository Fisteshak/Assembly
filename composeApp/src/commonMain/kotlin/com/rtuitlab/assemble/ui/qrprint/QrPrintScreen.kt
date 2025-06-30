package com.rtuitlab.assemble.ui.qrprint

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun QrPrintScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {


    Box(
        modifier = modifier.fillMaxSize().padding(top = 30.dp, start = 10.dp, end = 10.dp),
        contentAlignment = Alignment.Center
    ) {


        Scaffold(
            modifier = Modifier.width(700.dp),
            scaffoldState = rememberScaffoldState(),
            topBar = {
                Column {
                    QrPrintHeader(Modifier.fillMaxWidth())
                    Spacer(Modifier.height(30.dp))
                }
            },
            bottomBar = {
                Column {
                    Spacer(Modifier.height(24.dp))
                    QrPrintFooter(1, {}, Modifier.fillMaxWidth())
                }
            }
        ) { innerPadding ->


            QrPrintElementList(
                List(15, { Unit }),
                scrollState = rememberScrollState(),
                modifier = Modifier.padding(innerPadding)
            )


        }


    }
}
