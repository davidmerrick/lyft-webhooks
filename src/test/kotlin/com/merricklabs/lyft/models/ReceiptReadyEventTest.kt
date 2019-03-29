package com.merricklabs.lyft.models

import com.fasterxml.jackson.databind.ObjectMapper
import org.testng.Assert.assertNotNull
import org.testng.annotations.Test

class ReceiptReadyEventTest {

    @Test()
    fun `Deserialize receipt ready event object`(){
        val eventString = this.javaClass.getResource("/receipt_ready_event.json").readText(Charsets.UTF_8)
        val mapper = ObjectMapper()
        val event = mapper.readValue(eventString, ReceiptReadyEvent::class.java)
        assertNotNull(event)
    }
}