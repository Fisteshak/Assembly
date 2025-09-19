package com.rtuitlab.assemble.domain.entities

data class ContainerForPrinting(
    val number: String,
    val amount: Int,
    val isChecked: Boolean,
    val qr: List<Byte>
) {

}