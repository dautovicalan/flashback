package hr.algebra.flashback.model.log

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document


enum class LogAction {
    LOGIN,
    LOGOUT,
    REGISTER,
    LOGIN_WITH_GOOGLE,
    LOGIN_WITH_GITHUB,
    UPDATE_USER,
    COMPLETE_PROFILE,
    UPLOAD_PHOTO,
    DELETE_PHOTO,
    UPDATE_PHOTO,
    DOWNLOAD_PHOTO,
    DELETE_ACCOUNT,
    DELETE_LOGS,
}

@Document(indexName = "logs")
data class LogEntry(
    @Id
    val id: String? = null,
    val userId: String,
    val action: String,
    val timestamp: Long = System.currentTimeMillis(),
    val logAction: LogAction
)