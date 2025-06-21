package com.rtuitlab.assemble.data

//import com.rtuitlab.assemble.data.external.isOdd
import com.rtuitlab.assemble.data.external.isOdd


//external var globalCounter: Int

actual class PdfPrinter {
    actual suspend fun printPdf(pdfData: ByteArray): PrintResult {
        return try {

            println(isOdd(1.toJsNumber()))
//            val iframe = document.createElement("iframe")
//            document.body?.appendChild(iframe)
            PrintResult(true, "Print ok")

        } catch (e: Exception) {
            println("not ok  ${e.message}")

            PrintResult(false, "Print failed: ${e.message}")
        } catch (e: JsException) {
            println("js not ok  ${e.message}, ${e.cause}, ${e.thrownValue}")

            PrintResult(false, "Print failed: ${e.message}")
        }

    }

    actual suspend fun getAvailablePrinters(): List<String> {
        // WASM/Browser doesn't provide direct access to printer list
        // Return empty list or implement via JavaScript interop if needed
        return emptyList()
    }

}

