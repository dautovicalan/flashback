package hr.algebra.flashback.model.upload

import hr.algebra.flashback.dto.upload.PhotoFormat
import jakarta.persistence.*
import java.time.Clock
import java.time.LocalDate


@Entity
@Table(name = "photo_tag")
class PhotoTag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @Column(nullable = false, unique = true)
    val name: String = ""
)

@Entity
@Table(name = "photo")
class Photo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @Column(nullable = false)
    val key : String = "",
    @Column(nullable = false)
    val url : String = "",
    @Column(nullable = false)
    val uploadDate: LocalDate = LocalDate.now(Clock.systemUTC()),
    @Column(nullable = false)
    var width : Int = 0,
    @Column(nullable = false)
    var height : Int = 0,
    @Column(nullable = true)
    var description : String? = null,
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var format : PhotoFormat = PhotoFormat.JPEG,
    @Column(nullable = false)
    val createdBy: String = "",
    @Column(nullable = false)
    val userFullName: String = "",
    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinTable(
        name = "photo_photo_tag",
        joinColumns = [JoinColumn(name = "photo_id")],
        inverseJoinColumns = [JoinColumn(name = "photo_tag_id")]
    )
    var tags: MutableSet<PhotoTag> = HashSet()
)
