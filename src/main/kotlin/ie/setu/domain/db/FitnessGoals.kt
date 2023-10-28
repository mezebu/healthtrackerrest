package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage one Fitness Goal.
//       Database wise, this is the table object.

object FitnessGoals: Table("goals") {
    val id = integer("id").autoIncrement().primaryKey()
    val goalType = varchar("goal_type", 100)
    val target = double("target")
    val calories = integer("calories")
    val startDate = datetime("start_date")
    val endDate = datetime("end_date")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}