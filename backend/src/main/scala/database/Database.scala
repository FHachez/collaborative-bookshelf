package database

import graphql.types.BookTable
import scalikejdbc.config._

object Database {

  def init: Unit = {
    DBs.setupAll()
    createTables
  }
  def stop: Unit = {
    DBs.closeAll()
  }

  private def createTables: Unit = {
    BookTable.createTable
  }

}

