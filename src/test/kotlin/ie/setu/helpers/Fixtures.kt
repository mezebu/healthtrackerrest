package ie.setu.helpers

import ie.setu.domain.Activity
import ie.setu.domain.FitnessGoal
import ie.setu.domain.ImageInfo
import ie.setu.domain.User
import ie.setu.domain.db.Activities
import ie.setu.domain.db.FitnessGoals
import ie.setu.domain.db.ImageInfos
import ie.setu.domain.db.Users
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.FitnessGoalDAO
import ie.setu.domain.repository.ImageInfoDAO
import ie.setu.domain.repository.UserDAO
import org.jetbrains.exposed.sql.SchemaUtils
import org.joda.time.DateTime

 val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
 val validName = "Test User 1"
 val validEmail = "testuser1@test.com"

 val updatedName = "Updated Name"
 val updatedEmail = "Updated Email"

val updatedDescription = "Updated Description"
val updatedDuration = 30.0
val updatedCalories = 945
val updatedStarted = DateTime.parse("2020-06-11T05:59:27.258Z")

val updatedGoalType = "Dancing"
val updatedTarget = "50kg"
val updatedFitnessDuration = "20 hours"
val updatedInitialUserStatus = "100"
val updatedStartDate = DateTime.parse("2020-06-11T05:59:27.258Z")
val updatedEndDate = DateTime.parse("2020-06-11T05:59:27.258Z")

val users = arrayListOf(
    User(name = "Alice Wonderland", email = "alice@wonderland.com", id = 1),
    User(name = "Bob Cat", email = "bob@cat.ie", id = 2),
    User(name = "Mary Contrary", email = "mary@contrary.com", id = 3),
    User(name = "Carol Singer", email = "carol@singer.com", id = 4)
)


val activities = arrayListOf(
    Activity(id = 1, description = "Running", duration = 22.0, calories = 230, started = DateTime.now(), userId = 1),
    Activity(id = 2, description = "Hopping", duration = 10.5, calories = 80, started = DateTime.now(), userId = 1),
    Activity(id = 3, description = "Walking", duration = 12.0, calories = 120, started = DateTime.now(), userId = 2)
)

val fitnessGoals = arrayListOf(
    FitnessGoal(id = 1, goalType = "Weight Loss", target = "70", duration = "5", initialUserStatus = "100", startDate = DateTime.now(), endDate = DateTime.now(), userId = 1),
    FitnessGoal(id = 2, goalType = "Weight Gain", target = "170", duration = "5", initialUserStatus = "100", startDate = DateTime.now(), endDate = DateTime.now(), userId = 1),
    FitnessGoal(id = 3, goalType = "Running", target = "20", duration = "5", initialUserStatus = "0", startDate = DateTime.now(), endDate = DateTime.now(), userId = 2),
    )

val imageInfos = arrayListOf(
    ImageInfo(id = 1, imagePath = "/home/mick.jpg", imageDescription = "An image of mick", uploadTime = DateTime.now(), userId = 1),
    ImageInfo(id = 2, imagePath = "/home/luke.jpg", imageDescription = "An image of luke", uploadTime = DateTime.now(), userId = 1),
    ImageInfo(id = 3, imagePath = "/home/jon.jpg", imageDescription = "An image of jon", uploadTime = DateTime.now(), userId = 2)

)

fun populateUserTable(): UserDAO {
    SchemaUtils.create(Users)
    val userDAO = UserDAO()
    userDAO.save(users[0])
    userDAO.save(users[1])
    userDAO.save(users[2])
    return userDAO
}
fun populateActivityTable(): ActivityDAO {
    SchemaUtils.create(Activities)
    val activityDAO = ActivityDAO()
    activityDAO.save(activities[0])
    activityDAO.save(activities[1])
    activityDAO.save(activities[2])
    return activityDAO
}

fun populateFitnessGoalTable(): FitnessGoalDAO {
    SchemaUtils.create(FitnessGoals)
    val fitnessGoalDAO = FitnessGoalDAO()
    fitnessGoalDAO.save(fitnessGoals[0])
    fitnessGoalDAO.save(fitnessGoals[1])
    fitnessGoalDAO.save(fitnessGoals[2])
    return fitnessGoalDAO
}

fun populateImageInfoTable(): ImageInfoDAO {
    SchemaUtils.create(ImageInfos)
    val imageInfoDAO = ImageInfoDAO()
    imageInfoDAO.save(imageInfos[0])
    imageInfoDAO.save(imageInfos[1])
    imageInfoDAO.save(imageInfos[2])
    return imageInfoDAO
}