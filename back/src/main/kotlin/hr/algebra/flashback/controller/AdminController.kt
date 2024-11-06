package hr.algebra.flashback.controller

import hr.algebra.flashback.dto.photo.PhotoDto
import hr.algebra.flashback.dto.upload.UpdatePhotoDto
import hr.algebra.flashback.dto.user.KeycloakUser
import hr.algebra.flashback.dto.user.UpdateUserDataDto
import hr.algebra.flashback.dto.user.UserDto
import hr.algebra.flashback.model.log.LogAction
import hr.algebra.flashback.service.LogService
import hr.algebra.flashback.service.PhotoAdminService
import hr.algebra.flashback.service.UserAdminService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.time.LocalDate


@RestController
@RequestMapping("/api/v1/admin")
class AdminController(
    @Autowired
    private val userAdminService: UserAdminService,
    @Autowired
    private val photoService: PhotoAdminService,
    @Autowired
    private val logService: LogService
) {

    @GetMapping("/users")
    fun getPaginatedUsers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "", required = false) query: String?,
        authAdmin: Authentication
    ): ResponseEntity<Page<KeycloakUser>> {
        val pageable = PageRequest.of(page, size)
        val paginatedPhotos = userAdminService.findPaginatedUsers(pageable, query, authAdmin)
        return ResponseEntity.ok(paginatedPhotos)
    }

    @GetMapping("/users/all")
    fun getAllUsers(): ResponseEntity<List<KeycloakUser>> {
        val users = userAdminService.findAll()
        return ResponseEntity.ok(users)
    }

    @GetMapping("/users/{id}")
    fun getById(@PathVariable id: String): ResponseEntity<KeycloakUser> {
        val user = userAdminService.findById(id)
        return ResponseEntity.ok(user)
    }

    @GetMapping("/users/{id}/details")
    fun getUserDetails(@PathVariable id: String): ResponseEntity<UserDto> {
        val user = userAdminService.findUserDetailsById(id)
        return ResponseEntity.ok(user)
    }

    @PutMapping("/users/{id}")
    fun updateUserData(
        @PathVariable id: String,
        @RequestBody modifyData: UpdateUserDataDto,
        authAdmin: Authentication
    ): ResponseEntity<UserDto> {
        val modifiedUser = userAdminService.updateUserData(id, modifyData, authAdmin)
        logService.logAction(
            authAdmin.name,
            "User data updated from admin: ${authAdmin.name}",
            LogAction.UPDATE_USER
        )
        return ResponseEntity.ok(modifiedUser)
    }

    @DeleteMapping("/users/{id}")
    fun deleteUser(
        @PathVariable id: String,
        authAdmin: Authentication
    ): ResponseEntity<Unit> {
        userAdminService.deleteUser(id, authAdmin)
        logService.logAction(
            authAdmin.name,
            "User deleted from admin: $id",
            LogAction.DELETE_ACCOUNT
        )
        return ResponseEntity.ok().build()
    }

    @GetMapping("/photos")
    fun getPaginatedPhotos(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "", required = false) query: String?,
        @RequestParam(required = false) fromDate: LocalDate?,
        @RequestParam(required = false) toDate: LocalDate?
    ): ResponseEntity<Page<PhotoDto>> {
        val pageable = PageRequest.of(page, size)
        val paginatedPhotos = photoService.findPaginatedPhotos(pageable, query, fromDate, toDate)
        return ResponseEntity.ok(paginatedPhotos)
    }

    @GetMapping("/photos/{photoId}")
    fun getPhotoById(@PathVariable photoId: Long): ResponseEntity<PhotoDto> {
        val photo = photoService.findById(photoId)
        return ResponseEntity.ok(photo)
    }

    @PutMapping("/photos/{photoId}")
    fun updatePhotoMetadataAsAdmin(
        @PathVariable photoId: Long,
        @RequestBody updatePhotoDto: UpdatePhotoDto,
        authAdmin: Authentication
    ): ResponseEntity<PhotoDto> {
        val updatedPhoto = photoService.updatePhotoMetadata(photoId, updatePhotoDto)
        logService.logAction(
            authAdmin.name,
            "Photo metadata updated as admin: ${updatedPhoto.id}",
            LogAction.UPDATE_PHOTO
        )
        return ResponseEntity.ok(updatedPhoto)
    }

    @DeleteMapping("/photos/{id}")
    fun deletePhotoAsAdmin(
        @PathVariable id: Long,
        authAdmin: Authentication
    ): ResponseEntity<Unit> {
        photoService.deletePhoto(id)
        logService.logAction(
            authAdmin.name,
            "Photo deleted as admin: $id",
            LogAction.DELETE_PHOTO
        )
        return ResponseEntity.ok().build()
    }

}