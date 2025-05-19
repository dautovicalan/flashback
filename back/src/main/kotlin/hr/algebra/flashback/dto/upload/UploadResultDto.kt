package hr.algebra.flashback.dto.upload

import java.time.LocalDate

data class UploadResultDto(
    val key: String,
    val uploadDate: LocalDate
)
