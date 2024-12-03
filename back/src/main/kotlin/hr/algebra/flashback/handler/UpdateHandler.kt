package hr.algebra.flashback.handler

import hr.algebra.flashback.dto.upload.UpdatePhotoMetadataDto
import hr.algebra.flashback.exception.NotOwnerOfPhotoException
import hr.algebra.flashback.model.upload.Photo
import org.springframework.security.core.Authentication

interface UpdateHandler<T, U> {
    fun handle(entity: T, updateData: U, authUser: Authentication): T
}

// CHAIN OF RESPONSIBILITY PATTERN
class PhotoOwnershipValidationHandler<T> : UpdateHandler<T, UpdatePhotoMetadataDto> {
    override fun handle(entity: T, updateData: UpdatePhotoMetadataDto, authUser: Authentication): T {
        if (entity is Photo && entity.createdBy != authUser.name) {
            throw NotOwnerOfPhotoException("You are not the owner of this photo")
        }
        return entity
    }
}


class DescriptionUpdateHandler<T> : UpdateHandler<T, UpdatePhotoMetadataDto> {
    override fun handle(entity: T, updateData: UpdatePhotoMetadataDto, authUser: Authentication): T {
        if (entity is Photo) {
            entity.description = updateData.description
        }
        return entity
    }
}