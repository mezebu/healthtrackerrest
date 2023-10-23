package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.Activity
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonObjectMapper
import io.javalin.http.Context

object ActivityController {

    private val userDao = UserDAO()
    private val activityDAO = ActivityDAO()

    fun getAllActivities(ctx: Context) {
        try {
            val mapper = jsonObjectMapper()
            val activities = mapper.writeValueAsString(activityDAO.getAll())
            ctx.json(activities)
        } catch (e: Exception) {
            ctx.status(500).json("Error occurred during serialization.")
        }
    }
    fun getActivityById(ctx: Context) {
        val activityId = activityDAO.findByActivityId(ctx.pathParam("activity-id").toInt())

        if(activityId != null) {
            val mapper = jacksonObjectMapper()
                .registerModule(JodaModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            ctx.json(mapper.writeValueAsString(activityId))
        }
    }

    fun getActivitiesByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val activities = activityDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (activities.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                ctx.json(mapper.writeValueAsString(activities))
            }
        }
    }

    fun addActivity(ctx: Context) {
        //mapper handles the serialisation of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val activity = mapper.readValue<Activity>(ctx.body())
        activityDAO.save(activity)
        ctx.json(activity)
    }

    fun updateActivity(ctx: Context) {
        val activityId = ctx.pathParam("activity-id").toInt()
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val updatedActivity = mapper.readValue<Activity>(ctx.body())
        activityDAO.updateSpecificActivity(activityId, updatedActivity)
    }

    fun deleteAllUserActivities(ctx: Context) {
        val userId = ctx.pathParam("user-id").toInt()

        val user = userDao.findById(userId)
        if (user != null) {
           activityDAO.deleteUserActivities(userId)
        }
    }

    fun deleteActivity(ctx: Context) {
        val activityId = ctx.pathParam("activity-id").toInt()

        val activity = activityDAO.findByActivityId(activityId)
        if(activity != null) {
            activityDAO.deleteSpecificActivity(activityId)
        }
    }

}