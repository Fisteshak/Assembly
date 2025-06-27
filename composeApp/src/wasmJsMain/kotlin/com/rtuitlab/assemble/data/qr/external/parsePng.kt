package com.rtuitlab.assemble.data.qr.external

import org.w3c.dom.HTMLImageElement

fun parsePng(
    png: JsArray<JsNumber>
): HTMLImageElement =
    js(
        """{
            let img = new Image();
            img.src = "data:image/png;base64," + btoa(String.fromCharCode.apply(this, png));
            return img;
    }"""
    )