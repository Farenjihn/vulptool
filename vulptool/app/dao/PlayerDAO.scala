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

    def * = (mainPseudo, token) <> (Player.tupled, Player.unapply)
  }

}

@Singleton
class PlayerDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends PlayersComponent with HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._
  val players = TableQuery[PlayersTable]

  def list(): Future[Seq[Player]] = {
    val query = players.filter(!_.isDeleted).sortBy(s => s.mainPseudo)
    db.run(query.result)
  }

  def findById(token: String): Future[Option[Player]] =
    db.run(players.filter(_.token === token).result.headOption)

  def insert(player: Player): Future[Player] = {
    val insertQuery = players returning players.map(_.token) into ((player, token) => player.copy(token))
    db.run(insertQuery += player)
  }

  def update(token: String, player: Player): Future[Int] = {
    val playerToUpdate: Player = player.copy(token)
    db.run(players.filter(_.token === token).update(playerToUpdate))
  }

  def delete(token: String): Future[Int] =
    db.run(players.filter(_.token === token).delete)
}