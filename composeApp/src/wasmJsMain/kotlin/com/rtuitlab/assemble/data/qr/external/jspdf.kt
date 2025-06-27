package com.rtuitlab.assemble.data.qr.external

import org.w3c.dom.HTMLImageElement

@JsModule("jspdf")
external class jsPDF : JsAny {
    constructor(
        orientation: JsString = definedExternally,
        unit: JsString = definedExternally,
        format: JsString = definedExternally,
        putOnlyUsedFonts: JsBoolean = definedExternally,
        floatPrecision: JsNumber = definedExternally // or "smart", default is 16
    )

    fun text(
        text: JsString,
        x: JsNumber,
        y: JsNumber,
        options: JsAny
    ): jsPDF

    fun autoPrint(): jsPDF

    fun output(type: JsString): jsPDF

    fun addImage(
        imageData: HTMLImageElement,
        format: JsString,
        x: JsNumber,
        y: JsNumber,
        w: JsNumber,
        h: JsNumber,
    ): jsPDF

    fun addPage(
        format: JsString = definedExternally,
        orientation: JsString = definedExternally
    ): jsPDF

    fun setFontSize(
        size: JsNumber
    ): jsPDF

    fun setFont(
        fontName: JsString,
        fontStyle: JsString
    ): jsPDF
}

@JsModule("jspdf")
external interface TextOptionsLight {
    val align: JsString
}