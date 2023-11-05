package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object ImageInfos: Table("images") {
    val id = integer("id").autoIncrement().primaryKey()
    val imagePath = varchar("image_path", 400)
    val imageDescription = varchar("description", 100)
    val uploadTime = datetime("upload_time")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}