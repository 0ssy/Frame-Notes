// ExifParserTest.kt (plain JUnit)
package com.you.framenotes.exif

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ExifParserTest {
    @Test fun rational_fraction() = assertEquals(0.004, ExifParser.rational("1/250")!!, 1e-9)
    @Test fun rational_decimal()  = assertEquals(4.0, ExifParser.rational("4.0")!!, 1e-9)
    @Test fun rational_zeroDen()  = assertNull(ExifParser.rational("5/0"))
    @Test fun rational_junk()     = assertNull(ExifParser.rational("abc"))
    @Test fun date_valid()        = assertNotNull(ExifParser.dateTaken("2024:06:01 18:30:00"))
    @Test fun date_zeroed()       = assertNull(ExifParser.dateTaken("0000:00:00 00:00:00"))
    @Test fun camera_dedupesMake() = assertEquals("Canon EOS R6", ExifParser.camera("Canon", "Canon EOS R6"))
    @Test fun camera_prefixesMake() = assertEquals("SONY ILCE-7M3", ExifParser.camera("SONY", "ILCE-7M3"))
    @Test fun shutter_fast()      = assertEquals("1/250s", ExifParser.formatShutter(0.004f))
    @Test fun shutter_long()      = assertEquals("2s", ExifParser.formatShutter(2f))
}
