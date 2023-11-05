package ie.setu.repository

import ie.setu.domain.ImageInfo
import ie.setu.domain.db.ImageInfos
import ie.setu.domain.repository.ImageInfoDAO
import ie.setu.helpers.imageInfos
import ie.setu.helpers.populateImageInfoTable
import ie.setu.helpers.populateUserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


private val imageInfo1 = imageInfos[0]
private val imageInfo2 = imageInfos[1]
private val imageInfo3 = imageInfos[2]
class ImageInfoDAOTest {

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateImageInfos {
        @Test
        fun `multiple image infos added to table can be retrieved successfully`() {
            transaction {
                //Arrange - create and populate tables with three users and three images
                val userDAO = populateUserTable()
                val imageInfoDao = populateImageInfoTable()

                //Act & Assert
                assertEquals(3, imageInfoDao.getAll().size)
                assertEquals(imageInfo1, imageInfoDao.findByImageInfoId(imageInfo1.id))
                assertEquals(imageInfo2, imageInfoDao.findByImageInfoId(imageInfo2.id))
                assertEquals(imageInfo3, imageInfoDao.findByImageInfoId(imageInfo3.id))
            }
        }
    }

    @Nested
    inner class ReadImageInfos {
        @Test
        fun `getting all image infos from a populated table returns all rows`() {
            transaction {
                //Arrange - create and populate tables with three users and three image infos
                val userDAO = populateUserTable()
                val imageInfoDao = populateImageInfoTable()

                //Act & Assert
                assertEquals(3, imageInfoDao.getAll().size)
            }
        }

        @Test
        fun `get image info by user id that has no images, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three image information
                val userDAO = populateUserTable()
                val imageInfoDao = populateImageInfoTable()

                //Act & Assert
                assertEquals(0, imageInfoDao.findInfoByUserId(3).size)
            }
        }

        @Test
        fun `get image info by user id that exists, results in a correct image information returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three image information
                val userDAO = populateUserTable()
                val imageInfoDao = populateImageInfoTable()

                //Act & Assert
                assertEquals(imageInfo1, imageInfoDao.findInfoByUserId(1)[0])
                assertEquals(imageInfo2, imageInfoDao.findInfoByUserId(1)[1])
                assertEquals(imageInfo3, imageInfoDao.findInfoByUserId(2)[0])

            }
        }

        @Test
        fun `get all image infos over empty table returns none`() {
            transaction {

                //Arrange - create and setup imageInfoDAO object
                SchemaUtils.create(ImageInfos)
                val imageInfoDao = ImageInfoDAO()

                //Act & Assert
                assertEquals(0, imageInfoDao.getAll().size)
            }
        }

        @Test
        fun `get image info by image info id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three image information
                val userDAO = populateUserTable()
                val imageInfoDao = populateImageInfoTable()
                //Act & Assert
                assertEquals(null, imageInfoDao.findByImageInfoId(4))
            }
        }

        @Test
        fun `get image info by image info id that exists, results in a correct image info returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three image information
                val userDAO = populateUserTable()
                val imageInfoDao = populateImageInfoTable()
                //Act & Assert
                assertEquals(imageInfo1, imageInfoDao.findByImageInfoId(1))
                assertEquals(imageInfo3, imageInfoDao.findByImageInfoId(3))
            }
        }
    }

    @Nested
    inner class UpdateImageInfos {

        @Test
        fun `updating existing image info in table results in successful update`() {
            transaction {
                //Arrange - create and populate tables with three users and three images
                val userDAO = populateUserTable()
                val imageInfoDao = populateImageInfoTable()

                //Act & Assert
                val imageInfo3Updated = ImageInfo(id = 3, imagePath = "/home/example.jpeg",
                    imageDescription = "test image", uploadTime = DateTime.now(), userId = 2)
                imageInfoDao.updateByImageInfoId(imageInfo3Updated.id,  imageInfo3Updated)
                assertEquals(imageInfo3Updated, imageInfoDao.findByImageInfoId(3))
            }
        }

        @Test
        fun `updating non-existant image info in table results in no updates`() {
            transaction {

                //Arrange - create and populate tables with three users and three images
                val userDAO = populateUserTable()
                val imageInfoDao = populateImageInfoTable()

                //Act & Assert
                val imageInfo4Updated = ImageInfo(id = 4, imagePath = "/home/example.jpeg",
                    imageDescription = "test image", uploadTime = DateTime.now(), userId = 2)
                imageInfoDao.updateByImageInfoId(4,  imageInfo4Updated)
                assertEquals(null, imageInfoDao.findByImageInfoId(4))
                assertEquals(3, imageInfoDao.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteImageInfos {

        @Test
        fun `deleting a non-existant image info (by id) in table results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three image information
                val userDAO = populateUserTable()
                val imageInfoDao = populateImageInfoTable()

                //Act & Assert
                assertEquals(3, imageInfoDao.getAll().size)
                imageInfoDao.deleteByImageId(4)
                assertEquals(3, imageInfoDao.getAll().size)
            }
        }

        @Test
        fun `deleting an existing activity (by id) in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate tables with three users and three image information
                val userDAO = populateUserTable()
                val imageInfoDao = populateImageInfoTable()

                //Act & Assert
                assertEquals(3, imageInfoDao.getAll().size)
                imageInfoDao.deleteByImageId(imageInfo3.id)
                assertEquals(2, imageInfoDao.getAll().size)
            }
        }

        @Test
        fun `deleting images when none exist for user id results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three image information
                val userDAO = populateUserTable()
                val imageInfoDao = populateImageInfoTable()


                //Act & Assert
                assertEquals(3, imageInfoDao.getAll().size)
                imageInfoDao.deleteInfoByUserId(3)
                assertEquals(3, imageInfoDao.getAll().size)
            }
        }

        @Test
        fun `deleting image infos when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three image information
                val userDAO = populateUserTable()
                val imageInfoDao = populateImageInfoTable()

                //Act & Assert
                assertEquals(3, imageInfoDao.getAll().size)
                imageInfoDao.deleteInfoByUserId(1)
                assertEquals(1, imageInfoDao.getAll().size)
            }
        }


    }
}