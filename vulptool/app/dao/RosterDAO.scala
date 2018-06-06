package dao

import javax.inject.{Inject, Singleton}
import models.Roster
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}


trait RostersComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  class RostersTable(tag: Tag) extends Table[Roster](tag, "ROSTERS") {

    def id = column[Int]("id", O.PrimaryKey)

    def name = column[String]("name")

    def isDeleted = column[Boolean]("is_deleted")

    def * = (id.?, name) <> (Roster.tupled, Roster.unapply)
  }

}

@Singleton
class RosterDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends RostersComponent with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  val rosters = TableQuery[RostersTable]

  def list(): Future[Seq[Roster]] = {
    val query = rosters.filter(!_.isDeleted).sortBy(s => s.name)
    db.run(query.result)
  }

  def findById(id: Int): Future[Option[Roster]] =
    db.run(rosters.filter(_.id === id).result.headOption)

  def insert(roster: Roster): Future[Roster] = {
    val insertQuery = rosters returning rosters.map(_.id) into ((roster, id) => roster.copy(Some(id)))
    db.run(insertQuery += roster)
  }

  def update(id: Int, roster: Roster): Future[Int] = {
    val rosterToUpdate: Roster = roster.copy(Some(id))
    db.run(rosters.filter(_.id === id).update(rosterToUpdate))
  }

  def delete(id: Int): Future[Int] =
    db.run(rosters.filter(_.id === id).delete)
}