package com.rtuitlab.assemble.data.repositores

import com.rtuitlab.assemble.data.entities.AssembleForListOutDTO
import com.rtuitlab.assemble.data.entities.ComponentOutDTO
import com.rtuitlab.assemble.data.entities.FullAssembleDataOutDTO
import com.rtuitlab.assemble.data.entities.toAssembleIn
import com.rtuitlab.assemble.data.entities.toAssembleUpdateIn
import com.rtuitlab.assemble.data.httpClient
import com.rtuitlab.assemble.domain.entities.Assemble
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType


class AssembleApi(
) {
    val client = httpClient()

    suspend fun getAssembles(): List<AssembleForListOutDTO> {

        val response: HttpResponse = client.get("assemblies") {
        }

        val assemblies: List<AssembleForListOutDTO> = response.body()

        return assemblies
    }


    suspend fun getAssembleById(id: Long): FullAssembleDataOutDTO {
        val response = client.get("assemblies/$id") {}

        val assemble: FullAssembleDataOutDTO = response.body()

        return assemble
    }

    suspend fun getComponents(): List<ComponentOutDTO> {
        val response = client.get("components") { }
        val components: List<ComponentOutDTO> = response.body()

        return components
    }

    suspend fun createAssemble(assemble: Assemble): Assemble {
        val response = client.post("assemblies") {
            contentType(ContentType.Application.Json)
            setBody(assemble.toAssembleIn())
        }

        return assemble
    }

    suspend fun updateAssemble(assemble: Assemble): Assemble {
        val response = client.patch("assemblies/${assemble.assembleId}") {
            contentType(ContentType.Application.Json)
            setBody(assemble.toAssembleUpdateIn())
        }

        return assemble
    }


}

//fun createSampleAssembles(count: Int = 15): MutableList<Assemble> {
//    val sampleAssembles = mutableListOf<Assemble>()
//
//    for (i in 1..count) {
//        // Create some sample components for each assembly
//        val sampleComponents = listOf(
//            AssembleComponent(
//                componentId = (i * 100L) + 1,
//                name = "Resistor ${i}k Ohm",
//                amount = (i % 5 + 1).toLong(), // Vary amount
//                linkToPhoto = "photo_res_${i}.jpg",
//                linkToSound = "sound_res_${i}.mp3"
//            ),
//            AssembleComponent(
//                componentId = (i * 100L) + 2,
//                name = "Capacitor ${i}uF",
//                amount = (i % 3 + 1).toLong(), // Vary amount
//                linkToPhoto = "photo_cap_${i}.jpg",
//                linkToSound = "sound_cap_${i}.mp3"
//            ),
//            // Add more components if needed
//            AssembleComponent(
//                componentId = (i * 100L) + 3,
//                name = "LED Color ${i % 4}", // e.g., Color 0, 1, 2, 3
//                amount = 1,
//                linkToPhoto = "photo_led_${i}.jpg",
//                linkToSound = "sound_led_${i}.mp3"
//            )
//        )
//
//        // Create the Assemble object
//        val assemble = Assemble(
//            assembleId = i.toLong(),
//            name = "Sample Assemble #${i}",
//            instruction = "Follow instruction set $i.",
//            amountReady = (i % 10).toLong(), // Vary amount ready
//            linkToProject = "project_${i}.pdf",
//            linkToSound = if (i % 3 == 0) "assemble_sound_${i}.wav" else null,
//            userId = (100 + i).toLong(), // Sample user IDs
//            components = sampleComponents + sampleComponents + sampleComponents + sampleComponents + sampleComponents
//        )
//        sampleAssembles.add(assemble)
//    }
//
//    return sampleAssembles
//}