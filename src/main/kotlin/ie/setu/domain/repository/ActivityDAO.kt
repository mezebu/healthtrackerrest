package ie.setu.domain.repository


import ie.setu.domain.Activity
import ie.setu.domain.db.Activities
import ie.setu.utils.mapToActivity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class ActivityDAO {

    fun getAll() : ArrayList<Activity> {
        val activityList: ArrayList<Activity> = arrayListOf()
        transaction {
            Activities.selectAll().map {
                activityList.add(mapToActivity(it)) }
        }
        return activityList
    }

}
