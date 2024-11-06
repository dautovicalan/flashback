package hr.algebra.flashback.util

import hr.algebra.flashback.dto.upload.PhotoFormat

fun String.getPhotoType(): PhotoFormat {
    if (this.contains(".")){
        val type = this.substringAfterLast(".")
        return when (type) {
            "jpg" -> PhotoFormat.JPG
            "jpeg" -> PhotoFormat.JPEG
            "png" -> PhotoFormat.PNG
            "bmp" -> PhotoFormat.BMP
            "gif" -> PhotoFormat.GIF
            "wbmp" -> PhotoFormat.WBMP
            else -> PhotoFormat.JPG
        }
    }
    return PhotoFormat.JPG
}