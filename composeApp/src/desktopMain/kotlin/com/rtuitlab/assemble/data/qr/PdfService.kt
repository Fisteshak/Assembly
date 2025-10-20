package com.rtuitlab.assemble.data.qr

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType0Font
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.font.Standard14Fonts
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import org.apache.pdfbox.printing.PDFPageable
import java.awt.print.PrinterJob
import java.io.File


class JvmPdfService : PdfService {
    companion object {
        private const val POINTS_PER_INCH = 72
        private const val POINTS_PER_MM = 1 / (10 * 2.54f) * POINTS_PER_INCH
    }

    private val document = PDDocument()

    private lateinit var currentPage: PDPage
    private val pageHeight = PDRectangle.A4.height

    init {
        addPage()
    }

    override suspend fun print(): PrintResult {
        return withContext(Dispatchers.IO) {
            try {
                val printerJob = PrinterJob.getPrinterJob()

                printerJob.setPageable(PDFPageable(document))

                if (printerJob.printDialog()) {
                    printerJob.print()
                    PrintResult(true, "PDF printed successfully")
                } else {
                    PrintResult(false, "Print dialog cancelled")
                }
            } catch (e: Exception) {
                PrintResult(false, "Print failed with js exception: ${e.message}")
            }
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
        withContext(Dispatchers.IO) {
            val pdImage = PDImageXObject.createFromByteArray(document, pngImageData, "qr-code")
            val contents = PDPageContentStream(
                document, currentPage,
                PDPageContentStream.AppendMode.APPEND, true
            )

            val yNew = pageHeight - (y.toFloat() + height.toFloat()) * POINTS_PER_MM
            val xNew = x.toFloat() * POINTS_PER_MM

            contents.drawImage(
                pdImage,
                xNew,
                yNew,
                width.toFloat() * POINTS_PER_MM,
                height.toFloat() * POINTS_PER_MM
            )

            contents.close()
        }
    }

    override suspend fun addCenteredText(text: String, x: Length, y: Length) {
        withContext(Dispatchers.IO) {

            val contents = PDPageContentStream(
                document, currentPage,
                PDPageContentStream.AppendMode.APPEND, true
            )

            val fontSize = 14f
            val font = PDType1Font(Standard14Fonts.FontName.HELVETICA)
            val fontFile: File = File("NotoMono-Regular.ttf")
            val newFont: PDFont = PDType0Font.load(document, fontFile)
            contents.beginText()
            contents.setFont(newFont, fontSize);

            val textWidth = newFont.getStringWidth(text) / 1000 * fontSize
            val textHeight =
                newFont.fontDescriptor.getFontBoundingBox().height / 1000 * fontSize

            val yNew = pageHeight - y.toFloat() * POINTS_PER_MM
            val xNew = x.toFloat() * POINTS_PER_MM - textWidth / 2


            contents.newLineAtOffset(xNew, yNew);
            contents.showText(text)
            contents.endText()
            contents.close()
        }
    }

    override fun addPage() {
        currentPage = PDPage(PDRectangle.A4)
        document.addPage(currentPage)

    }
}

actual fun pdfService(): PdfService = JvmPdfService()