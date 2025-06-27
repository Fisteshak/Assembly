package com.rtuitlab.assemble.data.qr

/**
 * Pdf document for QR codes
 */
class QrPdf(
    val cols: Int,
    val horizontalMargin: Length = 10.0.mm,
    val verticalMargin: Length = 10.0.mm,
    val horizontalInnerPadding: Length = 10.0.mm,
    val verticalInnerPadding: Length = 15.0.mm,
) {
    private val pdfService = pdfService()

    private val pageWidth: Length = 210.0.mm
    private val pageHeight = 297.0.mm
    private val imageWidth =
        (pageWidth - 2 * horizontalMargin - (cols - 1) * horizontalInnerPadding) / cols
    private val imageHeight = imageWidth
    private var imageCount = 0

    /**
     *
     */
    suspend fun print(): PrintResult = pdfService.print()

    suspend fun addQr(pngImageData: ByteArray, name: String) {
        val j = imageCount % cols
        val i = imageCount / cols

        val x = horizontalMargin + j * (imageWidth + horizontalInnerPadding)
        val y = verticalMargin + i * (imageHeight + verticalInnerPadding)

        if (y + imageHeight > pageHeight - verticalMargin) {
            println("adding page")
            pdfService.addPage()
            imageCount = 0
            addQr(pngImageData, name)
        } else {
            println("placing at $x, $y")
            pdfService.addImage(pngImageData, name, x, y, imageWidth, imageHeight)
            imageCount++
            pdfService.addCenteredText(
                name,
                x + imageWidth / 2,
                y + imageHeight + verticalInnerPadding / 2
            )
        }
    }


}

