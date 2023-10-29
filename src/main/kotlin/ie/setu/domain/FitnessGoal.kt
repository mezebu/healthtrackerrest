package ie.setu.domain

import org.joda.time.DateTime

data class FitnessGoal (
    var id: Int,
    var goalType:String,
    var target: String,
    var duration: String,
    var initialUserStatus: String,
    var startDate: DateTime,
    var endDate: DateTime,
    var userId: Int
)