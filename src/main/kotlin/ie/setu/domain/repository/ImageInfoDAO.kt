package ie.setu.domain.repository

import ie.setu.domain.ImageInfo
import ie.setu.domain.db.ImageInfos
import ie.setu.utils.mapToImageInfo
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class ImageInfoDAO {

    //Get all the images in the database regardless of user id
    fun getAll():ArrayList<ImageInfo> {
        val imageList: ArrayList<ImageInfo> = arrayListOf()
        transaction {
            ImageInfos.selectAll().map {
                imageList.add(mapToImageInfo(it))
            }
        }
        return imageList
    }

    //Find a specific image info by image id
    fun findByImageInfoId(id: Int): ImageInfo? {
        return transaction {
            ImageInfos
                .select { ImageInfos.id eq id}
                .map { mapToImageInfo(it) }
                .firstOrNull()
        }
    }

    //Find all Image Infos for a specific user id
    fun findInfoByUserId(userId: Int): List <ImageInfo> {
        return transaction {
            ImageInfos
                .select { ImageInfos.userId eq userId }
                .map { mapToImageInfo(it) }
        }
    }

    //Save an image to the database
    fun save(image: ImageInfo): Int {
        return transaction {
            ImageInfos.insert{
                it[imageDescription] = image.imageDescription
                it[imagePath] = image.imagePath
                it[uploadTime] = image.uploadTime
                it[userId] = image.userId
            }
        } get ImageInfos.id
    }

    //update an image information by the image id
    fun updateByImageInfoId(imageId: Int, imageToUpdate: ImageInfo): Int {
        return transaction {
            ImageInfos.update({ ImageInfos.id eq imageId }) {
                it[imagePath] = imageToUpdate.imagePath
                it[uploadTime] = imageToUpdate.uploadTime
                it[imageDescription] = imageToUpdate.imageDescription
                it[userId] = imageToUpdate.userId
            }
        }
    }

    //Delete an Image information by the information id
    fun deleteByImageId(imageId: Int): Int {
        return transaction {
            ImageInfos.deleteWhere{ ImageInfos.id eq imageId }
        }
    }

    //Delete an Image information by the user's id
    fun deleteInfoByUserId(userId: Int): Int {
        return transaction {
            ImageInfos.deleteWhere { ImageInfos.userId eq userId }
        }
    }
}