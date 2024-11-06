package hr.algebra.flashback.service

import hr.algebra.flashback.dto.upload.UpdatePhotoMetadataDto
import hr.algebra.flashback.exception.NotOwnerOfPhotoException
import hr.algebra.flashback.exception.PhotoNotFoundException
import hr.algebra.flashback.model.upload.Photo
import hr.algebra.flashback.model.upload.PhotoTag
import hr.algebra.flashback.repository.PhotoRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.security.core.Authentication
import java.util.*
import kotlin.test.Test


class PhotoServiceTest {

    private lateinit var photoService: PhotoService
    private val photoRepository: PhotoRepository = mock(PhotoRepository::class.java)
    private val fileStorageService: FileStorageService = mock(FileStorageService::class.java)
    private val tagService: TagService = mock(TagService::class.java)


    private lateinit var updatePhotoMetadataDto: UpdatePhotoMetadataDto
    private lateinit var authUser: Authentication

    @BeforeEach
    fun setUp() {

       `when`(photoRepository.findAll()).thenReturn(
            listOf(
                Photo(
                    id = 1,
                    description = "Description 1",
                    tags = mutableSetOf(),
                    createdBy = "1"
                ),
                Photo(
                    id = 2,
                    description = "Description 2",
                    tags = mutableSetOf(),
                    createdBy = "1"
                )
            )
        )

        `when`(photoRepository.findById(1)).thenReturn(
            Optional.of(
                Photo(
                    id = 1,
                    description = "Description",
                    tags = mutableSetOf(),
                    createdBy = "1"
                )
            )
        )

        `when`(photoRepository.findByCreatedBy("1")).thenReturn(
            listOf(
                Photo(
                    id = 1,
                    description = "Description 1",
                    tags = mutableSetOf(),
                    createdBy = "1"
                ),
                Photo(
                    id = 2,
                    description = "Description 2",
                    tags = mutableSetOf(),
                    createdBy = "1"
                )
            )
        )

        `when`(photoRepository.deleteById(1)).then {
            `when`(photoRepository.findById(1)).thenReturn(Optional.empty())
        }

        photoService = PhotoService(photoRepository, fileStorageService, tagService)

        authUser = mock(Authentication::class.java)
        `when`(authUser.name).thenReturn("1")
        updatePhotoMetadataDto = UpdatePhotoMetadataDto(
            description = "Updated description",
            tags = setOf("tag1", "tag2")
        )
    }

    @Test
    fun `should find all photos`() {
        val result = photoService.findPhotos()

        assertThat(result).isNotEmpty
        assertThat(result).hasSize(2)
    }

    @Test
    fun `test update photo metadata`() {
        val photoId = 1L
        `when`(photoRepository.save(any(Photo::class.java))).thenReturn(
            Photo(
                id = 1,
                description = "Updated description",
                tags = mutableSetOf(
                    PhotoTag(1, "tag1"),
                    PhotoTag(2, "tag2")
                ),
                createdBy = "1"
            )
        )

        val result = photoService.updatePhotoMetadata(photoId, updatePhotoMetadataDto, authUser)

        assertThat(result).isNotNull
        assertThat(result.id).isEqualTo(1)
        assertThat(result.description).isEqualTo("Updated description")
        assertThat(result.tags).hasSize(2)
        assertThat(result.tags.map { it.name }).contains("tag1", "tag2")
    }

    @Test
    fun `test photo id does not exist exception while update photo metadata`() {
        val photoId = 999L

        assertThrows<PhotoNotFoundException> {
            photoService.updatePhotoMetadata(photoId, updatePhotoMetadataDto, authUser)
        }
    }

    @Test
    fun `test you are not the owner exception while update photo metadata`() {
        val notPhotoOwnerUser = mock(Authentication::class.java)
        `when`(notPhotoOwnerUser.name).thenReturn("99")

        val photoId = photoRepository.findById(1).get().id

        assertThrows<NotOwnerOfPhotoException> {
            photoService.updatePhotoMetadata(photoId, updatePhotoMetadataDto, notPhotoOwnerUser)
        }
    }

    @Test
    fun `test delete photo`() {
        val photoId = photoRepository.findById(1).get().id
        photoService.deletePhoto(photoId, authUser)

        val result = photoRepository.findById(photoId)
        assertThat(result).isEmpty()
    }

    @Test
    fun `test try delete photo that you are not the owner`() {
        val notOwnerUser = mock(Authentication::class.java)
        `when`(notOwnerUser.name).thenReturn("99")

        val photoId = photoRepository.findAll().first().id
        assertThrows<NotOwnerOfPhotoException> {
            photoService.deletePhoto(photoId, notOwnerUser)
        }
    }

    @Test
    fun `test delete all photos by user`() {

        val photos = photoRepository.findByCreatedBy("1")

        `when`(photoRepository.deleteAll(photos)).then {
            `when`(photoRepository.findByCreatedBy("1")).thenReturn(emptyList())
        }

        photoService.deleteAllPhotosByUser(authUser)

        val result = photoRepository.findByCreatedBy("1")
        assertThat(result).isEmpty()
    }

}