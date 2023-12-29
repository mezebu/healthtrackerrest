package ie.setu.config

import ie.setu.controllers.ActivityController
import ie.setu.controllers.FitnessGoalController
import ie.setu.controllers.ImageInfoController
import ie.setu.utils.jsonObjectMapper
import ie.setu.controllers.UserController
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.json.JavalinJackson
import io.javalin.vue.VueComponent

class JavalinConfig {

    fun startJavalinService(): Javalin {

        val app = Javalin.create{
            //added this jsonMapper for our integration tests - serialise objects to json
            it.jsonMapper(JavalinJackson(jsonObjectMapper()))
            it.staticFiles.enableWebjars()
            it.vue.vueAppName = "app" // only required for Vue 3, is defined in layout.html
        }.apply {
            exception(Exception::class.java) { e, _ -> e.printStackTrace() }
            error(404) { ctx -> ctx.json("404 : Not Found") }
        }.start(getRemoteAssignedPort())

        registerRoutes(app)
        return app
    }

    private fun getRemoteAssignedPort(): Int {
        val remotePort = System.getenv("PORT")
        return if (remotePort != null){
            Integer.parseInt(remotePort)
        } else 7000
    }

    private fun registerRoutes(app: Javalin) {
        app.routes {
            path("/api/users") {
                get(UserController::getAllUsers)
                post(UserController::addUser)
                path("{user-id}"){
                    get(UserController::getUserByUserId)
                    delete(UserController::deleteUser)
                    patch(UserController::updateUser)
                    path("activities"){
                        get(ActivityController::getActivitiesByUserId)
                        delete(ActivityController::deleteActivityByUserId)
                    }
                    path("goals") {
                        get(FitnessGoalController::getFitnessGoalsByUserId)
                        delete(FitnessGoalController::deleteFitnessGoalByUserId)
                    }
                    path("images"){
                        get(ImageInfoController::getImageInfosByUserId)
                        delete(ImageInfoController::deleteImageInfoByUserId)
                    }
                }
                path("/email/{email}"){
                    get(UserController::getUserByEmail)
                }
            }
            path("/api/activities") {
                get(ActivityController::getAllActivities)
                post(ActivityController::addActivity)
                path("{activity-id}"){
                    get(ActivityController::getActivitiesByActivityId)
                    delete(ActivityController::deleteActivityByActivityId)
                    patch(ActivityController::updateActivity)
                }
            }
            path("/api/goals") {
                get(FitnessGoalController::getAllFitnessGoals)
                post(FitnessGoalController::addFitnessGoal)
                path("{goal-id}"){
                    get(FitnessGoalController::getFitnessGoalsByFitnessGoalId)
                    delete(FitnessGoalController::deleteFitnessGoalByFitnessGoalId)
                    patch(FitnessGoalController::updateFitnessGoal)
                }
            }
            path("/api/images"){
                get(ImageInfoController::getAllImageInfos)
                post(ImageInfoController::addImageInfo)
                path("{image-id}"){
                    get(ImageInfoController::getImagesByImageInfoId)
                    delete(ImageInfoController::deleteImageInfoByImageId)
                    patch(ImageInfoController::updateImageInfo)
                }
            }

            // The @routeComponent that we added in layout.html earlier will be replaced
            // by the String inside the VueComponent. This means a call to / will load
            // the layout and display our <home-page> component.
            get("/", VueComponent("<home-page></home-page>"))
            get("/users", VueComponent("<user-overview></user-overview>"))
            get("/users/{user-id}", VueComponent("<user-profile></user-profile>"))
            get("/users/{user-id}/activities", VueComponent("<user-activity-overview></user-activity-overview>"))
            get("/users/{user-id}/goals", VueComponent("<user-goal-overview></user-goal-overview>"))
            get("/activities", VueComponent("<activity-overview></activity-overview>"))
            get("/activities/{activity-id}", VueComponent("<activity-profile></activity-profile>"))
            get("/goals", VueComponent("<goal-overview></goal-overview>"))
            get("/goals/{goal-id}", VueComponent("<goal-profile></goal-profile>"))
            get("/images", VueComponent("<image-overview></image-overview>"))
        }
    }
}