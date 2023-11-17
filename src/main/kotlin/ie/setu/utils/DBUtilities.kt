package ie.setu.utils

import ie.setu.domain.Activity
import ie.setu.domain.FitnessGoal
import ie.setu.domain.ImageInfo
import ie.setu.domain.User
import ie.setu.domain.db.Activities
import ie.setu.domain.db.FitnessGoals
import ie.setu.domain.db.ImageInfos
import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.ResultRow

fun mapToUser(it: ResultRow) = User(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email],
    age = it[Users.age],
    imagePath = it[Users.imagePath]
)

fun mapToActivity(it: ResultRow) = Activity(
    id = it[Activities.id],
    description = it[Activities.description],
    duration = it[Activities.duration],
    started = it[Activities.started],
    calories = it[Activities.calories],
    userId = it[Activities.userId]
)

fun mapToFitnessGoal(it: ResultRow) = FitnessGoal(
    id = it[FitnessGoals.id],
    goalType = it[FitnessGoals.goalType],
    target = it[FitnessGoals.target],
    duration = it[FitnessGoals.duration],
    initialUserStatus = it[FitnessGoals.initialUserStatus],
    startDate = it[FitnessGoals.startDate],
    endDate = it[FitnessGoals.endDate],
    userId = it[FitnessGoals.userId]
)

fun mapToImageInfo(it: ResultRow) = ImageInfo(
    id = it[ImageInfos.id],
    imageDescription = it[ImageInfos.imageDescription],
    imagePath = it[ImageInfos.imagePath],
    uploadTime = it[ImageInfos.uploadTime],
    userId = it[ImageInfos.userId],
)