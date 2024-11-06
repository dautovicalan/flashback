package hr.algebra.flashback.repository

import hr.algebra.flashback.model.upload.PhotoTag
import org.springframework.data.repository.CrudRepository

interface TagRepository : CrudRepository<PhotoTag, Long> {
    fun findByNameIn(names: Set<String>): List<PhotoTag>
}