package hr.algebra.flashback.controller

import hr.algebra.flashback.service.TagService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/tags")
class TagController(
    @Autowired
    private val tagService: TagService
) {
    @GetMapping("/")
    fun getTags(): ResponseEntity<List<String>> {
        val tags = tagService.getAllTags()
        return ResponseEntity.ok(tags)
    }
}