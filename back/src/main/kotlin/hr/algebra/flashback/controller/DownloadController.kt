package hr.algebra.flashback.controller

import hr.algebra.flashback.dto.photo.PhotoFiltersDto
import hr.algebra.flashback.model.log.LogAction
import hr.algebra.flashback.service.DownloadLoggingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/download")
class DownloadController(
    @Autowired
    private val downloadService: DownloadLoggingService
) {

    @PostMapping("/{photoId}")
    fun downloadPhoto(
        @PathVariable photoId: Long,
        @RequestBody(required = false) photoFiltersDto: PhotoFiltersDto?,
        user: Authentication
    ): ResponseEntity<Resource> {
        val (photoResource, filename) = downloadService.downloadPhoto(photoId, photoFiltersDto, user)
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$filename")
            .body(photoResource)
    }
}