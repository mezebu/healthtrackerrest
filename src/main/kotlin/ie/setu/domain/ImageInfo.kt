package ie.setu.domain
import org.joda.time.DateTime
data class ImageInfo(
    var id: Int,
    var imagePath: String, // Path to the image file
    var imageDescription: String, // Description of the image
    var uploadTime: DateTime, // Timestamp when the image was uploaded
    var userId: Int // ID of the user who uploaded the image
)