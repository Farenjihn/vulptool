package dao

import javax.inject.{Inject, Singleton}
import models.Player
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}


trait PlayersComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  class PlayersTable(tag: Tag) extends Table[Player](tag, "player") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def pseudo = column[String]("main_pseudo")

    def authCode = column[String]("auth_code")

    def accessCode = column[String]("access_code")

    def isDeleted = column[Boolean]("is_deleted")

    def * = (id.?, pseudo) <> (Player.tupled, Player.unapply)
  }

}

@Singleton
class PlayerDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends PlayersComponent with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  val players = TableQuery[PlayersTable]

  def list(): Future[Seq[Player]] = {
    val query = players.filter(!_.isDeleted).sortBy(_.pseudo)
    db.run(query.result)
  }

  def findById(id: Int): Future[Option[Player]] =
    db.run(players.filter(_.id === id).result.headOption)

  def insert(player: Player): Future[Player] = {
    val insertQuery = players returning players.map(_.id) into ((player, id) => player.copy(Some(id)))
    db.run(insertQuery += player)
  }

  def update(id: Int, player: Player): Future[Int] = {
    val playerToUpdate: Player = player.copy(Some(id))
    db.run(players.filter(_.id === id).update(playerToUpdate))
  }

  def delete(id: Int): Future[Int] =
    db.run(players.filter(_.id === id).delete)
}