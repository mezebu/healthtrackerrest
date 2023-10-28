package ie.setu.domain

import org.joda.time.DateTime

data class FitnessGoal (
    var id: Int,
    var goalType:String,
    var target: Double,
    var calories: Int,
    var startDate: DateTime,
    var endDate: DateTime,
    var userId: Int
)