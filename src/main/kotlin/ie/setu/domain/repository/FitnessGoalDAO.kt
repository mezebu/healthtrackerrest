package ie.setu.domain.repository

import ie.setu.domain.FitnessGoal
import ie.setu.domain.db.FitnessGoals
import ie.setu.utils.mapToFitnessGoal
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class FitnessGoalDAO {

    //Get all fitness goals in the database regardless of user id
    fun getAll(): ArrayList<FitnessGoal> {
        val fitnessList: ArrayList<FitnessGoal> = arrayListOf()
        transaction {
            FitnessGoals.selectAll().map {
                fitnessList.add(mapToFitnessGoal(it))
            }
        }
        return fitnessList
    }

    //Find a specific fitness goal by fitness goal id
    fun findById(id: Int): FitnessGoal? {
        return transaction {
            FitnessGoals
                .select { FitnessGoals.id eq id }
                .map { mapToFitnessGoal(it) }
                .firstOrNull()
        }
    }

    //Find all fitness goals for a specific user id
    fun findByUserId(userId: Int): List<FitnessGoal> {
        return transaction {
            FitnessGoals
                .select { FitnessGoals.userId eq userId }
                .map { mapToFitnessGoal(it) }
        }
    }

    //Save a fitness goal to the database
    fun save(goal: FitnessGoal): Int {
        return transaction {
            FitnessGoals.insert{
                it[goalType] = goal.goalType
                it[target] = goal.target
                it[startDate] = goal.startDate
                it[endDate] = goal.endDate
                it[userId] = goal.userId
            }
        } get FitnessGoals.id
    }

    //Update a specific fitness goal in the database by id
    fun updateByFitnessGoalId(goalId: Int, goalToUpdate: FitnessGoal): Int {
        return transaction {
            FitnessGoals.update ({FitnessGoals.id eq goalId}) {
                it[goalType] = goalToUpdate.goalType
                it[target] = goalToUpdate.target
                it[startDate] = goalToUpdate.startDate
                it[endDate] = goalToUpdate.endDate
                it[userId] = goalToUpdate.userId
            }
        }
    }

    //delete a specific fitness goal in the database by id
    fun deleteFitnessGoalById(goalId: Int): Int {
        return transaction {
            FitnessGoals.deleteWhere { FitnessGoals.id eq goalId }
        }
    }

    //delete a specific fitness goal in the database by the user id
    fun deleteFitnessGoalByUserId(userId: Int): Int {
        return transaction {
            FitnessGoals.deleteWhere { FitnessGoals.userId eq userId }
        }
    }


}