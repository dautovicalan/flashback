package hr.algebra.flashback.service

import hr.algebra.flashback.model.upload.PhotoTag
import hr.algebra.flashback.repository.TagRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TagService (
    @Autowired
    private val tagRepository: TagRepository
){
    fun createTags(tags: Set<String>): List<PhotoTag> {
        val filteredTags = tags.filter { it.isNotBlank() }.toSet()
        val existingTags = tagRepository.findByNameIn(filteredTags)
        val existingTagNames = existingTags.map { it.name }.toSet()

        val newTagNames = filteredTags.filterNot { existingTagNames.contains(it.trim()) }
        val newTags = newTagNames.map { PhotoTag(name = it) }

        return if(newTags.isNotEmpty()){
            tagRepository.saveAll(newTags) + existingTags
        } else {
            existingTags
        }
    }

    fun getAllTags(): List<String> {
        return tagRepository.findAll().map { it.name }
    }

}