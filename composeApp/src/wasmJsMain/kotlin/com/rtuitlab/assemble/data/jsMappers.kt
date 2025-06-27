package com.rtuitlab.assemble.data


val Int.js: JsNumber
    get() = this.toJsNumber()

val Double.js: JsNumber
    get() = this.toJsNumber()

val String.js: JsString
    get() = this.toJsString()