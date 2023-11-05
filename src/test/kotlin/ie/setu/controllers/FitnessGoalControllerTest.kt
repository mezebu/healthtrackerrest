package ie.setu.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import ie.setu.config.DbConfig
import ie.setu.domain.FitnessGoal
import ie.setu.domain.User
import ie.setu.helpers.*
import ie.setu.utils.jsonNodeToObject
import ie.setu.utils.jsonToObject
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FitnessGoalControllerTest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    @Nested
    inner class ReadFitnessGoals {
        @Test
        fun `get all fitness goals from the database returns 200 or 404 response`() {
            val res = retrieveAllFitnessGoals()
            if (res.status == 200) {
              val  retrievedGoals = jsonNodeToObject<Array<FitnessGoal>>(res)
                assertNotEquals(0, retrievedGoals.size)
            } else {
                assertEquals(404, res.status)
            }
        }

        @Test
        fun `get all fitness goals by user id when no user exists returns 404 response`() {
            //Arrange
            val userId = -1

            //Assert and Act - retrieve fitness goals by user id
            val res = retrieveGoalByGoalId(userId)
            assertEquals(404, res.status)
        }

        @Test
        fun `get goal by goal id when no goal exists returns 404 response`() {
            //Arrange
            val goalId = -1

            //Assert and Act - attempt to retrieve the goal by goal id
            val res = retrieveGoalByGoalId(goalId)
            assertEquals(404, res.status)
        }
    }

    @Nested
    inner class CreateFitnessGoals {

        @Test
        fun `add an goal when a user exists for it, returns a 201 response`() {

            //Arrange - add a user and an associated activity that we plan to do a deleted on
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())

            val addFitnessGoalResponse = addFitnessGoal(
                fitnessGoals[0].target, fitnessGoals[0].goalType, fitnessGoals[0].initialUserStatus,
                fitnessGoals[0].startDate, fitnessGoals[0].endDate, fitnessGoals[0].duration, addedUser.id
            )
            assertEquals(201, addFitnessGoalResponse.status)
            //After - delete the user (Fitness goal will cascade delete in the database)
            deleteUser(addedUser.id)
        }

        @Test
        fun `add an fitness goal when no user exists for it, returns a 404 response`() {
            //Arrange - check there is no user for -1 id
            val userId = -1
            assertEquals(404, retrieveUserById(userId).status)

            val addFitnessGoalResponse = addFitnessGoal(
                fitnessGoals[0].target, fitnessGoals[0].goalType, fitnessGoals[0].initialUserStatus,
                fitnessGoals[0].startDate, fitnessGoals[0].endDate, fitnessGoals[0].duration, userId
            )

            assertEquals(404, addFitnessGoalResponse.status)
        }

    }

    @Nested
    inner class UpdateFitnessGoals {
        @Test
        fun `updating a fitness goal by fitness goal id when it doesn't exist, returns a 404 response`() {
            val userId = -1
            val goalId = -1

            //Arrange - check there is no user for -1 id
            Assertions.assertEquals(404, retrieveUserById(userId).status)

            //Act & Assert - attempt to update the details of a fitness goal/user that doesn't exist
            assertEquals(
                404, updateFitnessGoal(goalId, updatedTarget, updatedGoalType, updatedInitialUserStatus,
                    updatedStartDate,  updatedEndDate, updatedFitnessDuration, userId).status
            )

        }

        @Test
        fun `updating a fitness goal by fitness goal id when it exists, returns 204 response`() {

            //Arrange - add a user and an associated fitness goal that we plan to do an update on
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addFitnessGoalResponse = addFitnessGoal(
                fitnessGoals[0].target, fitnessGoals[0].goalType, fitnessGoals[0].initialUserStatus,
                fitnessGoals[0].startDate, fitnessGoals[0].endDate, fitnessGoals[0].duration, addedUser.id
            )
            Assertions.assertEquals(201, addFitnessGoalResponse.status)
            val addedFitnessGoal = jsonNodeToObject<FitnessGoal>(addFitnessGoalResponse)

            //Act & Assert - update the added fitness goal and assert a 204 is returned
            val updatedFitnessGoalResponse = updateFitnessGoal(
                addedFitnessGoal.id, updatedTarget, updatedGoalType, updatedInitialUserStatus,
                updatedStarted, updatedEndDate, updatedFitnessDuration, addedUser.id
            )
            Assertions.assertEquals(204, updatedFitnessGoalResponse.status)

            //Assert that the individual fields were all updated as expected
            val retrievedFitnessGoalResponse = retrieveGoalByGoalId(addedFitnessGoal.id)
            val updatedGoal = jsonNodeToObject<FitnessGoal>(retrievedFitnessGoalResponse)
            Assertions.assertEquals(updatedTarget, updatedGoal.target)
            Assertions.assertEquals(updatedGoalType, updatedGoal.goalType)
            Assertions.assertEquals(updatedFitnessDuration, updatedGoal.duration)
            Assertions.assertEquals(updatedInitialUserStatus, updatedGoal.initialUserStatus)
            Assertions.assertEquals(updatedStartDate, updatedGoal.startDate)
            Assertions.assertEquals(updatedEndDate, updatedGoal.endDate)

            //After - delete the user
            deleteUser(addedUser.id)
        }
    }

    @Nested
    inner class DeleteFitnessGoals {
        @Test
        fun `deleting an goal by goal id when it doesn't exist, returns a 404 response`() {

            assertEquals(404, deleteGoalByGoalId(-1).status)
        }

        @Test
        fun `deleting fitness goals by user id when it doesn't exist, returns a 404 response`() {

            assertEquals(404, deleteGoalsByUserId(-1).status)
        }

        @Test
        fun `deleting an goal by id when it exists, returns a 204 response`() {
            //Arrange - add a user and an associated fitness goal that we plan to do a deleted on
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())

            val addFitnessGoalResponse = addFitnessGoal(
                fitnessGoals[0].target, fitnessGoals[0].goalType, fitnessGoals[0].initialUserStatus,
                fitnessGoals[0].startDate, fitnessGoals[0].endDate, fitnessGoals[0].duration, addedUser.id
            )

            //Act & Assert - delete the added fitness goal and assert a 204 is returned
            val addedFitnessGoal = jsonNodeToObject<FitnessGoal>(addFitnessGoalResponse)
            assertEquals(204, deleteGoalByGoalId(addedFitnessGoal.id).status)

        }

        @Test
        fun `deleting all fitness goals by userid when it exists, returns a 204 response`() {

            //Arrange - add a user and 3 associated activities that we plan to do a cascade delete
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addGoalResponse1 = addFitnessGoal(
                fitnessGoals[0].target, fitnessGoals[0].goalType, fitnessGoals[0].initialUserStatus,
                fitnessGoals[0].startDate, fitnessGoals[0].endDate, fitnessGoals[0].duration, addedUser.id
            )
            Assertions.assertEquals(201, addGoalResponse1.status)
            val addGoalResponse2 = addFitnessGoal(
                fitnessGoals[0].target, fitnessGoals[0].goalType, fitnessGoals[0].initialUserStatus,
                fitnessGoals[0].startDate, fitnessGoals[0].endDate, fitnessGoals[0].duration, addedUser.id
            )
            Assertions.assertEquals(201, addGoalResponse2.status)
            val addGoalResponse3 = addFitnessGoal(
                fitnessGoals[0].target, fitnessGoals[0].goalType, fitnessGoals[0].initialUserStatus,
                fitnessGoals[0].startDate, fitnessGoals[0].endDate, fitnessGoals[0].duration, addedUser.id
            )
            Assertions.assertEquals(201, addGoalResponse3.status)

            //Act & Assert - delete the added user and assert a 204 is returned
            Assertions.assertEquals(204, deleteUser(addedUser.id).status)

            //Act & Assert - attempt to retrieve the deleted fitness goals
            val addedFitnessGoal1 = jsonNodeToObject<FitnessGoal>(addGoalResponse1)
            val addedFitnessGoal2 = jsonNodeToObject<FitnessGoal>(addGoalResponse2)
            val addedFitnessGoal3 = jsonNodeToObject<FitnessGoal>(addGoalResponse3)
            Assertions.assertEquals(404, retrieveGoalByGoalId(addedFitnessGoal1.id).status)
            Assertions.assertEquals(404, retrieveGoalByGoalId(addedFitnessGoal2.id).status)
            Assertions.assertEquals(404, retrieveGoalByGoalId(addedFitnessGoal3.id).status)
        }


    }



    //helper function to delete a fitness goal by fitness goal id
    private fun deleteGoalByGoalId(id: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/goals/$id").asString()
    }

    //helper function to delete an fitness goal by user id
    private fun deleteGoalsByUserId(id: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/users/$id/goals").asString()

    }

    //helper function to delete a test user from the database
    private fun deleteUser (id: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/users/$id").asString()
    }


    //helper function to add a test user to the database
    private fun addUser (name: String, email: String): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/users")
            .body("{\"name\":\"$name\", \"email\":\"$email\"}")
            .asJson()
    }

    //helper function to retrieve all fitness goals
    private fun retrieveAllFitnessGoals(): HttpResponse<JsonNode> {
        return Unirest.get("$origin/api/goals").asJson()
    }

    //helper function to retrieve fitness goal by fitness goal id
    private fun retrieveGoalByGoalId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/goals/${id}").asJson()
    }

    //helper function to retrieve a test user from the database by id
    private fun retrieveUserById(id: Int) : HttpResponse<String> {
        return Unirest.get(origin + "/api/users/${id}").asString()
    }

    //helper function to add a fitness goal
    private fun addFitnessGoal(
        target: String,
        goalType: String,
        initialUserStatus: String,
        startDate: DateTime,
        endDate: DateTime,
        duration: String,
        userId: Int
    ): HttpResponse<JsonNode> {
        val objectMapper = ObjectMapper()

        val requestBody = objectMapper.createObjectNode()
        requestBody.put("target", target)
        requestBody.put("duration", duration)
        requestBody.put("initialUserStatus", initialUserStatus)
        requestBody.put("startDate", startDate.toString("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
        requestBody.put("endDate", endDate.toString("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
        requestBody.put("goalType", goalType)
        requestBody.put("userId", userId)

        val requestBodyString = objectMapper.writeValueAsString(requestBody)

        return Unirest.post("$origin/api/goals")
            .header("Content-Type", "application/json")
            .body(requestBodyString)
            .asJson()
    }

    //helper function to add an activity
    private fun updateFitnessGoal(
        id: Int,
        target: String,
        goalType: String,
        initialUserStatus: String,
        startDate: DateTime,
        endDate: DateTime,
        duration: String,
        userId: Int
    ): HttpResponse<JsonNode> {
        val objectMapper = ObjectMapper()

        val requestBody = objectMapper.createObjectNode()
        requestBody.put("target", target)
        requestBody.put("duration", duration)
        requestBody.put("initialUserStatus", initialUserStatus)
        requestBody.put("startDate", startDate.toString("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
        requestBody.put("endDate", endDate.toString("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
        requestBody.put("goalType", goalType)
        requestBody.put("userId", userId)

        val requestBodyString = objectMapper.writeValueAsString(requestBody)

        return Unirest.patch("$origin/api/goals/$id")
            .header("Content-Type", "application/json")
            .body(requestBodyString)
            .asJson()
    }


}