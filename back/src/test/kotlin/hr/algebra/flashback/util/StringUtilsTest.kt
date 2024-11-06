package hr.algebra.flashback.util

import hr.algebra.flashback.dto.upload.PhotoFormat
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class StringUtilsTest {

    @Test
    fun `should return jpg photo format`() {
        val format = "photo.jpg".getPhotoType()
        assertTrue { format == PhotoFormat.JPG }
    }

    @Test
    fun `should return jpeg photo format`() {
        val format = "photo.jpeg".getPhotoType()
        assertTrue { format == PhotoFormat.JPEG }
    }

    @Test
    fun `should return png photo format`() {
        val format = "photo.png".getPhotoType()
        assertTrue { format == PhotoFormat.PNG }
    }

    @Test
    fun `should return jpg when unknown photo format`() {
        val format = "photo.exe".getPhotoType()
        assertTrue { format == PhotoFormat.JPG }
    }

    @Test
    fun `test empty string`() {
        val format = "".getPhotoType()
        assertTrue { format == PhotoFormat.JPG }
    }

}