package hr.algebra.flashback.repository

import hr.algebra.flashback.model.upload.Photo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.time.LocalDate

interface PhotoRepository : CrudRepository<Photo, Long> {
    fun findByCreatedBy(createdBy: String): List<Photo>
    fun findAll(pageable: Pageable): Page<Photo>

    @Query("""
    SELECT p FROM Photo p 
    LEFT JOIN p.tags t
    WHERE 
        (:query IS NULL OR (
            p.description LIKE %:query% OR 
            CAST(p.format AS string) LIKE %:query% OR 
            CAST(p.width AS string) LIKE %:query% OR 
            CAST(p.height AS string) LIKE %:query% OR 
            p.userFullName LIKE %:query%
        ))  
        AND (CAST(:fromDate AS DATE) IS NULL OR p.uploadDate >= :fromDate) 
        AND (CAST(:toDate AS DATE) IS NULL OR p.uploadDate <= :toDate)
    ORDER BY p.uploadDate DESC
""")
    fun findAllByQuery(
        pageable: Pageable,
        query: String?,
        fromDate: LocalDate?,
        toDate: LocalDate?
    ): Page<Photo>
}