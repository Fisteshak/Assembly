package com.rtuitlab.assemble.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import org.apache.pdfbox.printing.PDFPageable
import java.awt.print.PrinterJob
import javax.print.PrintService
import javax.print.PrintServiceLookup


@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class PdfPrinter {
    actual suspend fun printPdf(pdfData: ByteArray): PrintResult =
        withContext(Dispatchers.IO) {
            try {
                val document = PDDocument().apply {
                    val page = PDPage(PDRectangle.A4)
                    addPage(page)
                    val pdImage: PDImageXObject =
                        PDImageXObject.createFromByteArray(this, pdfData, "qr-code")


                    val contents: PDPageContentStream = PDPageContentStream(this, page)

                    contents.drawImage(pdImage, 20f, 20f, 300f, 300f);

                    contents.close();
                }

                val printerJob = PrinterJob.getPrinterJob()

                printerJob.setPageable(PDFPageable(document))

                if (printerJob.printDialog()) {
                    printerJob.print()
                    document.close()
                    PrintResult(true, "PDF printed successfully")
                } else {
                    document.close()
                    PrintResult(false, "Print dialog cancelled")
                }
            } catch (e: Exception) {
                PrintResult(false, "Print failed: ${e.message}")
            }
        }

    actual suspend fun getAvailablePrinters(): List<String> = withContext(Dispatchers.IO) {
        try {
            PrintServiceLookup.lookupPrintServices(null, null)
                .map { it.name }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun findPrintService(printerName: String): PrintService? {
        return PrintServiceLookup.lookupPrintServices(null, null)
            .firstOrNull { it.name.equals(printerName, ignoreCase = true) }
    }
}