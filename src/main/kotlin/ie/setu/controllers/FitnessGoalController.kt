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
}