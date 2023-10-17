package ie.setu.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.repository.UserDAO
import ie.setu.domain.repository.ActivityDAO
import io.javalin.http.Context
import ie.setu.domain.User
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule

object HealthTrackerController {

    private val userDao = UserDAO()
    private val activityDAO = ActivityDAO()

    fun getAllUsers(ctx: Context) {
        ctx.json(userDao.getAll())
    }

    fun getUserById(ctx: Context) {
        val user = userDao.findById(ctx.pathParam("user-id").toInt())
        if(user != null) {
            ctx.json(user)
        }
    }

    fun addUser(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val user = mapper.readValue<User>(ctx.body())
        userDao.save(user)
        ctx.json(user)
    }


    fun getUsersByEmail(ctx: Context) {
        val user = userDao.findByEmail(ctx.pathParam("email"))
        if (user != null) {
            ctx.json(user)
        }
    }

    fun deleteUser(ctx: Context) {
        userDao.delete(ctx.pathParam("user-id").toInt())
    }

    fun updateUser(ctx: Context) {
        val userId = ctx.pathParam("user-id").toInt()
        val mapper = jacksonObjectMapper()
        val updatedUser = mapper.readValue<User>(ctx.body())
        userDao.update(userId, updatedUser)
        ctx.json(updatedUser)
    }

    //--------------------------------------------------------------
    // ActivityDAOI specifics
    //-------------------------------------------------------------

    fun getAllActivities(ctx: Context) {
        //mapper handles the deserialization of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        ctx.json(mapper.writeValueAsString(activityDAO.getAll()))
    }



}

