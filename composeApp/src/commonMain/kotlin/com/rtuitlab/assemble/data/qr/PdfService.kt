package com.rtuitlab.assemble.data.qr

expect fun pdfService(): PdfService

interface PdfService {
    suspend fun print(): PrintResult
    suspend fun addImage(
        pngImageData: ByteArray,
        name: String,
        x: Length,
        y: Length,
        width: Length,
        height: Length
    )

    suspend fun addCenteredText(text: String, x: Length, y: Length)
    fun addPage()
}

