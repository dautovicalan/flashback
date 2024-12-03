package hr.algebra.flashback.service

import hr.algebra.flashback.dto.upload.PhotoFormat
import net.coobird.thumbnailator.Thumbnails
import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.ConvolveOp
import java.awt.image.Kernel
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.imageio.ImageIO


class PhotoTransformationService {

    // BUILDER PATTERN
    class Builder(
        private val bufferedImage: BufferedImage
    ) {
        private var width: Int = 0
        private var height: Int = 0
        private var blur: Int = 0
        private var sepia: Int = 0
        private var format: PhotoFormat = PhotoFormat.JPG

        fun width(width: Int) = apply { this.width = width }
        fun height(height: Int) = apply { this.height = height }
        fun blur(blur: Int) = apply { this.blur = blur }
        fun sepia(sepia: Int) = apply { this.sepia = sepia }
        fun format(format: PhotoFormat) = apply { this.format = format }

        fun buildProcessedImage(): InputStream {

            val newImage = Thumbnails
                .of(bufferedImage)
                .size(width, height)
                .outputFormat(format.name)
                .asBufferedImage()

            return newImage
                .applyBlur(blur)
                .applySepia(sepia)
                .toInputStream(format)
        }
    }

}

// FACADE PATTERN
fun BufferedImage.toInputStream(format: PhotoFormat): InputStream{
    val outputStream = ByteArrayOutputStream()
    ImageIO.write(this, format.name, outputStream)
    return ByteArrayInputStream(outputStream.toByteArray())
}

fun BufferedImage.applyBlur(blur: Int): BufferedImage {
    if (blur == 0) {
        return this
    }
    val blurPercentage = 100.coerceAtMost(1.coerceAtLeast(blur))
    val kernelSize: Int = 3 + (blurPercentage / 5) * 2

    val blurKernel = FloatArray(kernelSize * kernelSize)
    for (i in blurKernel.indices) {
        blurKernel[i] = 1.0f / blurKernel.size
    }

    val kernel = Kernel(kernelSize, kernelSize, blurKernel)
    val op = ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null)
    return op.filter(this, null)
}

fun BufferedImage.applySepia(sepia: Int): BufferedImage {
    if (sepia == 0) {
        return this
    }
    val intensity = sepia.coerceIn(1, 10)
    val sepiaImage = BufferedImage(width, height, type)

    for (y in 0 until height) {
        for (x in 0 until width) {
            val color = Color(getRGB(x, y))

            val gray = (0.3 * color.red + 0.59 * color.green + 0.11 * color.blue).toInt()

            val red = (gray + 2 * 10 * intensity).coerceIn(0, 255)
            val green = (gray + 10 * intensity).coerceIn(0, 255)
            val blue = (gray - 10 * intensity).coerceIn(0, 255)

            val sepiaColor = Color(red, green, blue)
            sepiaImage.setRGB(x, y, sepiaColor.rgb)
        }
    }

    return sepiaImage
}