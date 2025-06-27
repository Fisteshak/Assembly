package com.rtuitlab.assemble.data.qr

// measured in millimetres
typealias Length = Double

val Double.mm: Length
    get() = this

val Double.inches: Length
    get() = this * 25.4
