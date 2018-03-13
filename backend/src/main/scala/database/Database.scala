package database

import java.util.Properties

import com.typesafe.config.ConfigFactory
import graphql.types.{BookTable, UserTable}
import java.sql.DriverManager

object Database {

  private val url = ConfigFactory.load().getString("db.default.url")
  private val props = new Properties()
  props.setProperty("driver", ConfigFactory.load().getString("db.default.driver"))
  props.setProperty("user", ConfigFactory.load().getString("db.default.user"))
  props.setProperty("password", ConfigFactory.load().getString("db.default.password"))

  implicit val conn = DriverManager.getConnection(url, props)

  def init: Unit = {
    createTables
  }
  def stop: Unit = {
    conn.close()
  }

  private def createTables: Unit = {
    BookTable.createTable
    UserTable.createTable
  }

}

