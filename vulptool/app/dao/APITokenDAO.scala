package dao

import java.sql.Timestamp

import javax.inject.{Inject, Singleton}
import models.{APIToken, Player}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

trait APITokenComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  class APITokenTable(tag: Tag) extends Table[APIToken](tag, "apitoken") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def playerId = column[Int]("player_id")

    def value = column[String]("value")

    def timeCreated = column[Timestamp]("time_created")

    def * = (id.?, playerId, value, timeCreated) <> (APIToken.tupled, APIToken.unapply)
  }

}

@Singleton
class APITokenDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends APITokenComponent with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  val apitokens = TableQuery[APITokenTable]

  def findByPlayerId(id: Int): Future[Option[APIToken]] =
    db.run(apitokens.filter(_.playerId === id).result.headOption)

  def insert(token: APIToken): Future[APIToken] = {
    val insertQuery = apitokens returning apitokens.map(_.id) into ((token, id) => token.copy(Some(id)))
    db.run(insertQuery += token)
  }
}