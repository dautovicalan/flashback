package hr.algebra.flashback.dto.upload

import jakarta.validation.constraints.Size


enum class PhotoFormat {
    JPEG, PNG, BMP, JPG, GIF, WBMP
}

data class UpdatePhotoMetadataDto(
    val description: String?,
    val tags : Set<String>?,
)

data class PhotoMetadataDto(
    @Size(min = 10, max = 10000)
    val width: Int,
    @Size(min = 10, max = 10000)
    val height: Int,
    val format: PhotoFormat
)

data class UpdatePhotoDto(
    @Size(min = 10, max = 10000)
    val width: Int,
    @Size(min = 10, max = 10000)
    val height: Int,
    val format: PhotoFormat,
    val description: String?,
    val tags: Set<String>?
)