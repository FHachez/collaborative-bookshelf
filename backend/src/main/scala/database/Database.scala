package database

import scalikejdbc.config._

object Database {

  def init: Unit = {
    DBs.setupAll()
  }
  def stop: Unit = {
    DBs.closeAll()
  }

}

