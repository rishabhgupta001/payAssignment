package com.sample.vkoelassign

import com.sample.vkoelassign.utility.Utils
import org.junit.Assert.assertEquals
import org.junit.Test

class UtilsTest {

    @Test
    fun mobileNum_invalid() {
        val result = Utils.isValidMobile("12345")
        assertEquals(false, result)
    }

    @Test
    fun mobileNum_valid() {
        val result = Utils.isValidMobile("9718448443")
        assertEquals(true, result)
    }

    @Test
    fun mobileNum_empty() {
        val result = Utils.isValidMobile("")
        assertEquals(false, result)
    }
}