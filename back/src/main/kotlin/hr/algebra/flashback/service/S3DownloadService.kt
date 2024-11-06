package hr.algebra.flashback.service

import hr.algebra.flashback.dto.photo.PhotoFiltersDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.util.UUID
import javax.imageio.ImageIO

interface DownloadService {
    fun downloadPhoto(photoId: Long, photoFiltersDto: PhotoFiltersDto?): Pair<Resource, String>
}

@Service
class S3DownloadService(
    @Autowired
    private val fileStorageService: FileStorageService,
    @Autowired
    private val photoService: PhotoService,
) : DownloadService {

    override fun downloadPhoto(photoId: Long, photoFiltersDto: PhotoFiltersDto?): Pair<Resource, String> {
        val photo = photoService.findById(photoId)

        val downloadedPhoto = fileStorageService.downloadFile(photo)

        if (photoFiltersDto != null) {
            val bufferedImage = ImageIO.read(downloadedPhoto)
            val transformedInputStream = ImageTransformationService.Builder(bufferedImage)
                .width(photoFiltersDto.width)
                .height(photoFiltersDto.height)
                .blur(photoFiltersDto.blur)
                .sepia(photoFiltersDto.sepia)
                .format(photoFiltersDto.format)
                .buildProcessedImage()
            return Pair(InputStreamResource(transformedInputStream), "${UUID.randomUUID()}.${photoFiltersDto.format}")
        }
        return Pair(InputStreamResource(downloadedPhoto), "${UUID.randomUUID()}.${photo.format}")
    }
}