package com.rtuitlab.assemble.domain.entities

import com.rtuitlab.assemble.data.entities.ContainerOutDTO

data class Container(
    val number: String,
    val room: String,
    val amount: Long,
    val componentId: Long,
) {
    fun formatAmount(): String {
        val n = amount
        val end = n % 10
        val end2 = n % 100

        return "$n детал" +
                if (end2 in 11..19)
                    "ей"
                else
                    when (end) {
                        0L, 5L, 6L, 7L, 8L, 9L -> "ей"
                        1L -> "ь"
                        2L, 3L, 4L -> "и"
                        else -> "ей"
                    }
    }
}

fun ContainerOutDTO.toContainer(): Container {
    return Container(
        number, room, amount, componentId
    )
}

