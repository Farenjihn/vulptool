package dao

import models.Event
import slick.jdbc.JdbcProfile
import java.text.SimpleDateFormat

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

trait EventsComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  class EventsTable(tag: Tag) extends Table[Event](tag, "EVENTS") {

    def id = column[Int]("id", O.PrimaryKey)
    def name = column[String]("event_name")
    def eType = column[Int]("event_type")
    def meetingId = column[Int]("meetingFK_id")
    def raidId = column[Int]("raidFK_id")
    def isDeleted = column[Boolean]("is_deleted")

    def * = (id, name, eType, meetingId, raidId) <> (Event.tupled, Event.unapply)
  }

}

@Singleton
class EventDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends EventsComponent with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  val events = TableQuery[EventsTable]

  def list(): Future[Seq[Event]] = {
    val query = events.filter(!_.isDeleted).sortBy(s => s.eType)
    db.run(query.result)
  }

  def findById(id: Int): Future[Option[Event]] =
    db.run(events.filter(_.id === id).result.headOption)

  def insert(event: Event): Future[Event] = {
    val insertQuery = events returning events.map(_.id) into ((event, id) => event.copy(id))
    db.run(insertQuery += event)
  }

  def update(id: Int, event: Event): Future[Int] = {
    val eventToUpdate: Event = event.copy(id)
    db.run(events.filter(_.id === id).update(eventToUpdate))
  }

  def delete(id: Int): Future[Int] =
    db.run(events.filter(_.id === id).delete)
}