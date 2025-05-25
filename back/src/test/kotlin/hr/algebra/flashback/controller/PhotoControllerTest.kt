package hr.algebra.flashback.controller

import hr.algebra.flashback.dto.upload.PhotoFormat
import hr.algebra.flashback.dto.upload.UpdatePhotoMetadataDto
import hr.algebra.flashback.model.upload.Photo
import hr.algebra.flashback.model.user.SubscriptionPlan
import hr.algebra.flashback.model.user.User
import hr.algebra.flashback.repository.PhotoRepository
import hr.algebra.flashback.repository.UserRepository
import jakarta.transaction.Transactional
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper
import java.time.LocalDate
import org.assertj.core.api.Assertions.assertThat
import org.springframework.test.context.ActiveProfiles

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class PhotoControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var photoRepository: PhotoRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    private lateinit var testUser: User
    private lateinit var testPhoto: Photo

    @BeforeEach
    @Transactional
    fun setUp() {
        photoRepository.deleteAll()
        userRepository.deleteAll()

        testUser = createTestUser()
        testPhoto = createTestPhoto()

    }

    @Test
    fun updatePhotoMetadata() {
        val updatePhotoMetadataDto = UpdatePhotoMetadataDto(
            description = "New Description",
            tags = setOf("tag1", "tag2"),
        )

        val authentication = TestingAuthenticationToken("test-user-id", null)


        mockMvc.perform(
            put("/api/v1/photos/${testPhoto.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(updatePhotoMetadataDto))
                .principal(authentication)
        )
            .andExpect { status().isOk }
            .andExpect { result -> {
                val response = result.response.contentAsString
                val updatedPhoto = ObjectMapper().readValue(response, Photo::class.java)
                assertThat(updatedPhoto.description).isEqualTo("New Description")
                assertThat(updatedPhoto.tags.map { it.name }).containsExactlyInAnyOrder("tag1", "tag2")
            } }

    }

    @Test
    fun shouldThrowExceptionIfPhotoNotFoundWhileUpdating() {
        val nonExistentPhotoId = 999L
        val updatePhotoMetadataDto = UpdatePhotoMetadataDto(
            description = "New Description",
            tags = setOf("tag1", "tag2"),
        )

        val authentication = TestingAuthenticationToken("test-user-id", null)

        mockMvc.perform(
            put("/api/v1/photos/$nonExistentPhotoId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(updatePhotoMetadataDto))
                .principal(authentication)
        )
            .andExpect { status().isNotFound }
    }

    private fun createTestPhoto(): Photo {
        val photo = Photo(
            key = "test-photo-key",
            url = "http://example.com/test-photo.jpg",
            uploadDate = LocalDate.now(),
            width = 1000,
            height = 800,
            description = "Updated Description",
            format = PhotoFormat.JPEG,
            createdBy = "test-user-id",
            userFullName = "Alan Dautovic",
        )
        return photoRepository.save(photo)
    }

    private fun createTestUser(): User {
        val user = User(
            id = "test-user-id",
            subscriptionPlan = SubscriptionPlan.GOLD,
            lastSubscriptionChange = LocalDate.now(),
            isProfileCompleted = true,
            dailyUpload = 0
        )
        return userRepository.save(user)
    }
}