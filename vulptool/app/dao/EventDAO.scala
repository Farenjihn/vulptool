package dao

import java.sql.Timestamp

import javax.inject.{Inject, Singleton}
import models.Event
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

trait EventsComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  class EventsTable(tag: Tag) extends Table[Event](tag, "event") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def description = column[String]("description")

    def meetingId = column[Int]("meeting_id")

    def raidId = column[Int]("raid_id")

    def rosterId = column[Int]("roster_id")

    def isDeleted = column[Boolean]("is_deleted")

    def * = (id.?, name, description, meetingId, raidId, rosterId) <> (Event.tupled, Event.unapply)

  }

}

@Singleton
class EventDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends EventsComponent with MeetingsComponent
    with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  val events = TableQuery[EventsTable]
  val meetings = TableQuery[MeetingsTable]

  def list(): Future[Seq[Event]] = {
    val query = events.filter(!_.isDeleted).sortBy(_.description)
    db.run(query.result)
  }

  def listFromDates(start: Timestamp, end: Timestamp): Future[Seq[Event]] = {
    val query = for {
      m <- meetings.sortBy(_.timeBegin) if m.timeBegin >= start && m.timeEnd <= end
      e <- events if e.id === m.id
    } yield e

    db.run(query.result)
  }

  def findById(id: Int): Future[Option[Event]] =
    db.run(events.filter(_.id === id).result.headOption)

  def insert(event: Event): Future[Event] = {
    val insertQuery = events returning events.map(_.id) into ((event, id) => event.copy(Some(id)))
    db.run(insertQuery += event)
  }

  def update(id: Int, event: Event): Future[Int] = {
    val eventToUpdate: Event = event.copy(Some(id))
    db.run(events.filter(_.id === id).update(eventToUpdate))
  }

  def delete(id: Int): Future[Int] =
    db.run(events.filter(_.id === id).map(_.isDeleted).update(true))
}