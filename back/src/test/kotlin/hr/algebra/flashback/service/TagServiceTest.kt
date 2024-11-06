package hr.algebra.flashback.service

import hr.algebra.flashback.model.upload.PhotoTag
import hr.algebra.flashback.repository.TagRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@Transactional
class TagServiceTest : AbstractBaseContainerTest() {

    @Autowired
    private lateinit var tagService: TagService
    @Autowired
    private lateinit var tagRepository: TagRepository


    @BeforeEach
    fun setUp() {
        tagRepository.deleteAll()
        tagRepository.saveAll(listOf(
            PhotoTag(name = "tag1"),
            PhotoTag(name = "tag2"),
            PhotoTag(name = "tag3")
        ))
    }

    @Test
    fun `test new create tags`(){
        tagService.createTags(setOf("tag99"))
        val tags = tagRepository.findAll()

        assertThat(tags).hasSize(4)
        assertThat(tags.map { it.name }).contains("tag99")
    }

    @Test
    fun `test get all tags`(){
        val tags = tagService.getAllTags()
        assertThat(tags).containsExactlyInAnyOrder("tag1", "tag2", "tag3")
    }
}