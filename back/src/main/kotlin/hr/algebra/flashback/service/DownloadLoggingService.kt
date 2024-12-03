package hr.algebra.flashback.service

import hr.algebra.flashback.dto.photo.PhotoFiltersDto
import hr.algebra.flashback.model.log.LogAction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

// DECORATOR PATTERN
@Service
class DownloadLoggingService(
    @Autowired
    private val downloadService: DownloadService,
    @Autowired
    private val logService: LogService
) {
    fun downloadPhoto(photoId: Long, photoFiltersDto: PhotoFiltersDto?, user: Authentication): Pair<Resource, String> {
        val downloadedPhotoPair = downloadService.downloadPhoto(photoId, photoFiltersDto)
        logService.logAction(
            user.name,
            "Photo downloaded: ${downloadedPhotoPair.second}",
            LogAction.DOWNLOAD_PHOTO
        )
        return downloadedPhotoPair
    }
}