package ie.setu.controllers

import ie.setu.domain.FitnessGoal
import ie.setu.domain.repository.FitnessGoalDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object FitnessGoalController {

    private val userDao = UserDAO()
    private val fitnessGoalDao = FitnessGoalDAO()

    fun getAllFitnessGoals(ctx: Context) {
        val fitnessGoals = fitnessGoalDao.getAll()
        if (fitnessGoals.size != 0) {
            ctx.status(200)
        } else {
            ctx.status(404)
        }
        ctx.json(fitnessGoals)
    }

    fun getFitnessGoalsByUserId(ctx: Context) {
        val userId = ctx.pathParam("user-id").toInt()

        if(userDao.findById(userId)!= null) {
            val fitnessGoals = fitnessGoalDao.findByUserId(ctx.pathParam("user-id").toInt())
            if (fitnessGoals.isNotEmpty()) {
                ctx.json(fitnessGoals)
                ctx.status(200)
            } else {
                ctx.status(404)
            }
        } else {
            ctx.status(404)
        }
    }

    fun getFitnessGoalsByFitnessGoalId(ctx: Context) {
        val goalId = ctx.pathParam("goal-id").toInt()
        val fitnessGaol = fitnessGoalDao.findByFitnessGoalId(goalId)

        if (fitnessGaol != null) {
            ctx.json(fitnessGaol)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun addFitnessGoal(ctx: Context) {
        val fitnessGoal: FitnessGoal = jsonToObject(ctx.body())
        val userId = userDao.findById(fitnessGoal.userId)
        if (userId != null) {
            val goalId = fitnessGoalDao.save(fitnessGoal)
            fitnessGoal.id = goalId
            ctx.json(fitnessGoal)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun deleteFitnessGoalByFitnessGoalId(ctx: Context) {
        val goalId = ctx.pathParam("goal-id").toInt()

        if (fitnessGoalDao.deleteByFitnessGoalId(goalId) != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    fun deleteFitnessGoalByUserId(ctx: Context) {
        val userId = ctx.pathParam("user-id").toInt()

        if (fitnessGoalDao.deleteByUserId(userId) != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    fun updateFitnessGoal(ctx: Context) {
        val fitnessGoal: FitnessGoal = jsonToObject(ctx.body())
        val goalId = ctx.pathParam("goal-id").toInt()

        if (fitnessGoalDao.updateByFitnessGoalId(goalId, goalToUpdate = fitnessGoal) != 0) {
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }
    
}