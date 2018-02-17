package security.types

import scalikejdbc._

case class Account(username: String, password: String, salt: String)

object Account extends SQLSyntaxSupport[Account] {
  override lazy val tableName = "accounts"
  val a = this.syntax("a")

  override lazy val columns = autoColumns[Account]()

  def apply(rs: WrappedResultSet): Account = {
    autoConstruct(rs, a.resultName)
  }
}
