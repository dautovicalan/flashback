package hr.algebra.flashback.dto.upload

import java.time.LocalDate
import java.time.LocalDateTime

data class UploadResultDto(
    val key: String,
    val uploadDate: LocalDate
)
