package ie.setu.config

import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.name

class DbConfig {

    private val logger = KotlinLogging.logger {  }
    fun getDbConnection() : Database{

        logger.info { "Staring DB Connection..." }

        val pgHOST = "flora.db.elephantsql.com"
        val pgPORT = "5432"
        val pgUSER = "lotdpcws"
        val pgPASSWORD = "gXq4ccgF95y05dp_N9K_ioX4qZJG9hLp"
        val pgDATABASE = "lotdpcws"

        //url format should be jdbc:postgresql://host:port/database
        val dbUrl = "jdbc:postgresql://$pgHOST:$pgPORT/$pgDATABASE"
        val dbConfig = Database.connect(
            url = dbUrl,
            driver="org.postgresql.Driver",
            user = pgUSER,
            password = pgPASSWORD
        )

        logger.info { "DbConfig name = " + dbConfig.name}
        logger.info { "DbConfig url = " + dbConfig.url }
        return dbConfig
    }
}