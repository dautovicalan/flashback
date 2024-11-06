package hr.algebra.flashback.dto.photo

import hr.algebra.flashback.dto.upload.PhotoFormat
import hr.algebra.flashback.model.upload.Photo
import jakarta.validation.constraints.Size
import java.time.LocalDate
import java.time.LocalDateTime

data class PhotoDto(
    val id : Long = 0L,
    val description: String?,
    val tags: List<String> = emptyList(),
    val url: String,
    val width: Int,
    val height: Int,
    val author: String,
    val ownerId: String,
    val uploadDate: LocalDate,
    val format: PhotoFormat
)

fun Photo.toDto() =
    PhotoDto(
        id,
        description,
        tags.map { it.name },
        url,
        width,
        height,
        userFullName,
        createdBy,
        uploadDate,
        format
    )

data class PhotoFiltersDto(
    @Size(min = 10, max = 10000)
    val width: Int,
    @Size(min = 10, max = 10000)
    val height: Int,
    @Size(min = 0, max = 100)
    val sepia: Int,
    @Size(min = 0, max = 100)
    val blur: Int,
    val format: PhotoFormat
)

