package hr.algebra.flashback.exception

class PhotoNotFoundException(override val message: String = "Photo not found"): IllegalArgumentException(message)
class NotOwnerOfPhotoException(override val message: String = "User is not the owner of the photo"): IllegalArgumentException(message)
class WrongFileFormatException(override val message: String = "File must be an image"): IllegalArgumentException(message)