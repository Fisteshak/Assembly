package com.rtuitlab.assemble.data

expect class PdfPrinter() {
    suspend fun printPdf(pdfData: ByteArray): PrintResult
    suspend fun getAvailablePrinters(): List<String>
}

data class PrintResult(
    val success: Boolean,
    val message: String
)