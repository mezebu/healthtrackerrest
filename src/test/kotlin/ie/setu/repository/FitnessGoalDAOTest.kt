package ie.setu.repository

import ie.setu.domain.FitnessGoal
import ie.setu.domain.db.FitnessGoals
import ie.setu.domain.repository.FitnessGoalDAO
import ie.setu.helpers.fitnessGoals
import ie.setu.helpers.populateFitnessGoalTable
import ie.setu.helpers.populateUserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


//retrieving test data from Fixtures
private val fitness1 = fitnessGoals[0]
private val fitness2 = fitnessGoals[1]
private val fitness3 = fitnessGoals[2]
class FitnessGoalDAOTest {

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateFitnessGoals {
        @Test
        fun `multiple goals added to table can be retrieved successfully`() {
            transaction {
                val userDAO = populateUserTable()
                val fitnessDAO = populateFitnessGoalTable()

                assertEquals(3, fitnessDAO.getAll().size)
                assertEquals(fitness1, fitnessDAO.findByFitnessGoalId(fitness1.id))
                assertEquals(fitness2, fitnessDAO.findByFitnessGoalId(fitness2.id))
                assertEquals(fitness3, fitnessDAO.findByFitnessGoalId(fitness3.id))
            }
        }
    }

    @Nested
    inner class ReadFitnessGoals {
        @Test
        fun `getting all goals from a populated table returns all rows`() {
            transaction {
                //Arrange - create and populate tables with three users and three fitness goals
                val userDAO = populateUserTable()
                val fitnessDAO = populateFitnessGoalTable()

                //Act & Assert
                assertEquals(3, fitnessDAO.getAll().size)
            }
        }

        @Test
        fun `get fitness goal by user id that has no fitness goals, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three fitness goals
                val userDAO = populateUserTable()
                val fitnessDAO = populateFitnessGoalTable()

                //Act & Assert
                assertEquals(0, fitnessDAO.findByUserId(3).size)
            }
        }

        @Test
        fun `get fitness goal by user id that exists, results in a correct fitness goals returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three goals
                val userDAO = populateUserTable()
                val fitnessDAO = populateFitnessGoalTable()

                //Act & Assert
                assertEquals(fitness1, fitnessDAO.findByUserId(1)[0])
                assertEquals(fitness2, fitnessDAO.findByUserId(1)[1])
                assertEquals(fitness3, fitnessDAO.findByUserId(2)[0])
            }
        }

        @Test
        fun `get all fitness goals over empty table returns none`() {
            transaction {
                //Arrange - create and setup fitnessGoalDAO object
                SchemaUtils.create(FitnessGoals)
                val fitnessGoalDAO = FitnessGoalDAO()

                //Act & Assert
                assertEquals(0, fitnessGoalDAO.getAll().size)
            }
        }

        @Test
        fun `get fitness goal by fitness goal id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three goals
                val userDAO = populateUserTable()
                val fitnessDAO = populateFitnessGoalTable()

                //Act & Assert
                assertEquals(null, fitnessDAO.findByFitnessGoalId(4))
            }
        }

        @Test
        fun `get fitness goal by fitness goal id that exists, results in a correct fitness goal returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three goals
                val userDAO = populateUserTable()
                val fitnessDAO = populateFitnessGoalTable()

                //Act & Assert
                assertEquals(fitness1, fitnessDAO.findByFitnessGoalId(1))
                assertEquals(fitness3, fitnessDAO.findByFitnessGoalId(3))

            }
        }
    }

    @Nested
    inner class UpdateFitnessGoals {

        @Test
        fun `updating existing fitness goal in table results in successful update`() {
            transaction {

                //Arrange - create and populate tables with three users and three goals
                val userDAO = populateUserTable()
                val fitnessDAO = populateFitnessGoalTable()

                //Act & Assert
                val fitnessGoal3updated = FitnessGoal(id = 3, goalType = "Yoga", target = "5 hours", duration = "4 hours",
                    initialUserStatus = "0", startDate = DateTime.now(), endDate = DateTime.now(), userId = 2)
                fitnessDAO.updateByFitnessGoalId(fitnessGoal3updated.id, fitnessGoal3updated)
                assertEquals(fitnessGoal3updated, fitnessDAO.findByFitnessGoalId(3))
            }
        }

        @Test
        fun `updating non-existant fitness goal in table results in no updates`() {
            transaction {
                //Arrange - create and populate tables with three users and three goals
                val userDAO = populateUserTable()
                val fitnessDAO = populateFitnessGoalTable()

                val fitnessGoal4updated = FitnessGoal(id = 4, goalType = "Yoga", target = "5 hours", duration = "4 hours",
                    initialUserStatus = "0", startDate = DateTime.now(), endDate = DateTime.now(), userId = 2)
                fitnessDAO.updateByFitnessGoalId(4, fitnessGoal4updated)
                assertEquals(null, fitnessDAO.findByFitnessGoalId(4))
                assertEquals(3, fitnessDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteFitnessGoals {
        @Test
        fun `deleting a non-existant goal (by id) in table results in no deletion`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val fitnessDAO = populateFitnessGoalTable()

                //Act & Assert
                assertEquals(3, fitnessDAO.getAll().size)
                fitnessDAO.deleteByFitnessGoalId(4)
                assertEquals(3, fitnessDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing goal (by id) in table results in record being deleted`() {
            transaction {
                //Arrange - create and populate tables with three users and three goals
                val userDAO = populateUserTable()
                val fitnessDAO = populateFitnessGoalTable()

                //Act & Assert
                assertEquals(3, fitnessDAO.getAll().size)
                fitnessDAO.deleteByFitnessGoalId(fitness3.id)
                assertEquals(2, fitnessDAO.getAll().size)
            }
        }

        @Test
        fun `deleting fitness goals when 1 or more exist for user id results in deletion`() {
            transaction {
                //Arrange - create and populate tables with three users and three goals
                val userDAO = populateUserTable()
                val fitnessDAO = populateFitnessGoalTable()

                //Act & Assert
                assertEquals(3,fitnessDAO.getAll().size)
                fitnessDAO.deleteByUserId(1)
                assertEquals(1, fitnessDAO.getAll().size)
            }
        }
    }
}
