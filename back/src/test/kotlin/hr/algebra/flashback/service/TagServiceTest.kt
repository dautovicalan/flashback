package hr.algebra.flashback.service

import hr.algebra.flashback.model.upload.PhotoTag
import hr.algebra.flashback.repository.TagRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class TagServiceTest {

    private lateinit var tagService: TagService
    private val tagRepository: TagRepository = mock(TagRepository::class.java)


    @BeforeEach
    fun setUp() {

        tagService = TagService(tagRepository)
        `when`(tagRepository.findAll()).thenReturn(
            listOf(
                PhotoTag(name = "tag1"),
                PhotoTag(name = "tag2"),
                PhotoTag(name = "tag3")
            )
        )
    }

    @Test
    fun `test new create tags`(){
        val newTag = PhotoTag(name = "tag99")

        `when`(tagRepository.saveAll(anyList())).thenReturn(listOf(newTag))
        `when`(tagRepository.findAll()).thenReturn(
            listOf(
                PhotoTag(name = "tag1"),
                PhotoTag(name = "tag2"),
                PhotoTag(name = "tag3"),
                newTag
            )
        )

        tagService.createTags(setOf("tag99"))
        val tags = tagRepository.findAll()

        assertThat(tags).hasSize(4)
        assertThat(tags.map { it.name }).contains("tag99")
    }

    @Test
    fun `test create existing tags`(){

        `when`(tagRepository.findByNameIn(setOf("tag2"))).thenReturn(
            listOf(PhotoTag(name = "tag2"))
        )

        `when`(tagRepository.saveAll(anyList())).thenReturn(emptyList())

        val result = tagService.createTags(setOf("tag2"))

        assertThat(result).hasSize(1)
        assertThat(result.map { it.name }).containsExactly("tag2")
        verify(tagRepository, times(0)).saveAll(anyList())
    }

    @Test
    fun `test get all tags`(){
        val tags = tagService.getAllTags()
        assertThat(tags).containsExactlyInAnyOrder("tag1", "tag2", "tag3")
    }
}