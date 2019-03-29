package com.merricklabs.lyft.models

import com.fasterxml.jackson.databind.ObjectMapper
import org.testng.Assert.assertNotNull
import org.testng.annotations.Test

class RideTest {

    @Test()
    fun `Deserialize ride object`(){
        val rideString = this.javaClass.getResource("/ride.json").readText(Charsets.UTF_8)
        val mapper = ObjectMapper()
        val ride = mapper.readValue(rideString, Ride::class.java)
        assertNotNull(ride)
    }
}