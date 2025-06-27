package com.rtuitlab.assemble.data.qr

import com.rtuitlab.assemble.data.js
import com.rtuitlab.assemble.data.qr.external.jsPDF
import com.rtuitlab.assemble.data.qr.external.parsePng

fun getOptions(): JsAny = js("({ align: 'center' })")

class JsPdfService : PdfService {
    private val pdf: jsPDF = jsPDF().apply {
        setFont("Helvetica".js, "".js)
        setFontSize(14.js)
    }

    override suspend fun print(): PrintResult {
        return try {
            pdf.autoPrint()
            pdf.output("dataurlnewwindow".toJsString())
            PrintResult(true, "Print ok")
        } catch (e: JsException) {
            PrintResult(false, "Print failed with js exception: ${e.message}")
        }
    }

    override suspend fun addImage(
        pngImageData: ByteArray,
        name: String,
        x: Length,
        y: Length,
        width: Length,
        height: Length
    ) {
        val png = parsePng(pngImageData.map { it.toUByte().toInt().js }.toJsArray())
        pdf.addImage(png, "PNG".js, x.js, y.js, width.js, height.js)
    }

    override suspend fun addCenteredText(text: String, x: Length, y: Length) {
        pdf.text(text.js, x.js, y.js, getOptions())
    }

    override fun addPage() {
        pdf.addPage()
    }
}

actual fun pdfService(): PdfService = JsPdfService()
