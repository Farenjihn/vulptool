package dao

import models.Player
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

trait PlayersComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  class PlayersTable(tag: Tag) extends Table[Player](tag, "PLAYERS") {
    def mainPseudo = column[String]("main_pseudo")

    def token = column[String]("token", O.PrimaryKey)

    def isDeleted = column[Boolean]("is_deleted")

    def * = (mainPseudo, token, isDeleted) <> (Player.tupled, Player.unapply)
  }

}

@Singleton
class StudendsDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends PlayersComponent with HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._
  val players = TableQuery[PlayersTable]

  def list(): Future[Seq[Player]] = {
    val query = players.sortBy(s => s.mainPseudo)
    db.run(query.result)
  }
}