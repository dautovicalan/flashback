package hr.algebra.flashback.service

import hr.algebra.flashback.dto.upload.UpdatePhotoMetadataDto
import hr.algebra.flashback.exception.NotOwnerOfPhotoException
import hr.algebra.flashback.exception.PhotoNotFoundException
import hr.algebra.flashback.model.upload.Photo
import hr.algebra.flashback.repository.PhotoRepository
import hr.algebra.flashback.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import kotlin.test.Test


@Transactional
class PhotoServiceTest : AbstractBaseContainerTest() {

    @Autowired
    private lateinit var photoService: PhotoService
    @Autowired
    private lateinit var photoRepository: PhotoRepository
    @Autowired
    private lateinit var userRepository: UserRepository


    private lateinit var user: User
    private lateinit var updatePhotoMetadataDto: UpdatePhotoMetadataDto

    @BeforeEach
    fun setUp() {
        photoRepository.deleteAll()
        userRepository.deleteAll()

        val testUser = User(flashbackUsername = "test@example.com")
        userRepository.save(testUser)

        val photo = Photo(
            key = "test-key",
            description = "test-description",
            user = testUser
        )

        photoRepository.save(photo)

        user = testUser
        updatePhotoMetadataDto = UpdatePhotoMetadataDto(
            description = "Updated description",
            tags = setOf("tag1", "tag2")
        )
    }

    @Test
    fun `should find all photos`() {
        val result = photoService.findPhotos()

        assertThat(result).isNotEmpty
        assertThat(result).hasSize(1)
    }

    @Test
    fun `test update photo metadata`() {
        val photoId = photoRepository.findAll().first().id

        photoService.updatePhotoMetadata(photoId, updatePhotoMetadataDto, user)

        val updatedPhoto = photoRepository.findById(photoId).get()

        assertThat(updatedPhoto.description).isEqualTo("Updated description")
        assertThat(updatedPhoto.tags).hasSize(2)
        assertThat(updatedPhoto.tags.map { it.name }).contains("tag1", "tag2")
    }

    @Test
    fun `test photo id does not exist exception while update photo metadata`() {
        val photoId = 999L

        assertThrows<PhotoNotFoundException> {
            photoService.updatePhotoMetadata(photoId, updatePhotoMetadataDto, user)
        }
    }

    @Test
    fun `test you are not the owner exception while update photo metadata`() {
        val newUser = User(flashbackUsername = "haha@haha.com")
        userRepository.save(newUser)

        val photoId = photoRepository.findAll().first().id

        assertThrows<NotOwnerOfPhotoException> {
            photoService.updatePhotoMetadata(photoId, updatePhotoMetadataDto, newUser)
        }
    }

    @Test
    fun `test delete photo`() {
        val photoId = photoRepository.findAll().first().id

        photoService.deletePhoto(photoId, user)

        val result = photoRepository.findById(photoId)

        assertThat(result).isEmpty()
    }

    @Test
    fun `test try delete photo that you are not the owner`() {
        val newUser = User(flashbackUsername = "haha@haha.com")
        userRepository.save(newUser)

        val photoId = photoRepository.findAll().first().id
        assertThrows<NotOwnerOfPhotoException> {
            photoService.deletePhoto(photoId, newUser)
        }
    }

}