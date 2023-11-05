package ie.setu.controllers

import ie.setu.domain.ImageInfo
import ie.setu.domain.repository.ImageInfoDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object ImageInfoController {

    private val userDao = UserDAO()
    private val imageInfoDao = ImageInfoDAO()

    fun getAllImageInfos(ctx: Context) {
        val imageInfos = imageInfoDao.getAll()
        if(imageInfos.size != 0) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(imageInfos)
    }

    fun getImageInfosByUserId(ctx: Context) {
        val userId = userDao.findById(ctx.pathParam("user-id").toInt())
        val imageInfos = imageInfoDao.findInfoByUserId(ctx.pathParam("user-id").toInt())

        if (userId != null) {
            if (imageInfos.isNotEmpty()) {
                ctx.json(imageInfos)
                ctx.status(200)
            } else {
                ctx.status(404)
            }
        } else {
            ctx.status(404)
        }
    }

    fun getImagesByImageInfoId(ctx: Context) {
        val imageInfo = imageInfoDao.findByImageInfoId(ctx.pathParam("image-id").toInt())
        if (imageInfo != null) {
            ctx.json(imageInfo)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun addImageInfo(ctx: Context) {
        val imageInfo: ImageInfo = jsonToObject(ctx.body())
        val userId = userDao.findById(imageInfo.userId)

        if (userId != null) {
            val imageId = imageInfoDao.save(imageInfo)
            imageInfo.id = imageId
            ctx.json(imageInfo)
            ctx.status(201)
        } else {
            ctx.status(404)
        }
    }

    fun deleteImageInfoByImageId(ctx: Context) {
        val imageId = ctx.pathParam("image-id").toInt()

        if(imageInfoDao.deleteByImageId(imageId) != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    fun deleteImageInfoByUserId(ctx: Context) {
        val userId = ctx.pathParam("user-id").toInt()

        if (imageInfoDao.deleteInfoByUserId(userId) != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    fun updateImageInfo(ctx: Context) {
        val imageInfo: ImageInfo = jsonToObject(ctx.body())
        val imageId = ctx.pathParam("image-id").toInt()

        if (imageInfoDao.updateByImageInfoId(imageId, imageToUpdate = imageInfo) != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

}