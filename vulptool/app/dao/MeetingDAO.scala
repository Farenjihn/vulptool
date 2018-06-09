package dao

import java.sql.{Date, Timestamp}

import javax.inject.{Inject, Singleton}
import models.{Event, Meeting}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

trait MeetingsComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  class MeetingsTable(tag: Tag) extends Table[Meeting](tag, "meeting") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def time_begin = column[Timestamp]("time_begin")

    def time_end = column[Timestamp]("time_end")

    def isDeleted = column[Boolean]("is_deleted")

    def * = (id.?, time_begin, time_end) <> (Meeting.tupled, Meeting.unapply)
  }
}

@Singleton
class MeetingDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends MeetingsComponent with EventsComponent with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  val meetings = TableQuery[MeetingsTable]
  val events = TableQuery[EventsTable]

  def list(): Future[Seq[Meeting]] = {
    val query = meetings.filter(!_.isDeleted).sortBy(_.time_begin)
    db.run(query.result)
  }

  def listFromDates(start: Date, end: Date): Future[Seq[Meeting]] =
    listFromDates(Timestamp.from(start.toInstant), Timestamp.from(end.toInstant))

  def listFromDates(start: String, end: String): Future[Seq[Meeting]] =
    listFromDates(Timestamp.valueOf(start), Timestamp.valueOf(end))

  def listFromDates(start: Timestamp, end: Timestamp): Future[Seq[Meeting]] = {
    val query = for {
      m <- meetings if m.time_begin > start && m.time_end < end
    } yield m

    db.run(query.result)
  }

  def getEventFromMeeting(id: Int): Future[Option[Event]] =
    db.run(events.filter(_.id === id).result.headOption)

  def findById(id: Int): Future[Option[Meeting]] =
    db.run(meetings.filter(_.id === id).result.headOption)

  def insert(meeting: Meeting): Future[Meeting] = {
    val insertQuery = meetings returning meetings.map(_.id) into ((meeting, id) => meeting.copy(Some(id)))
    db.run(insertQuery += meeting)
  }

  def update(id: Int, meeting: Meeting): Future[Int] = {
    val meetingToUpdate: Meeting = meeting.copy(Some(id))
    db.run(meetings.filter(_.id === id).update(meetingToUpdate))
  }

  def delete(id: Int): Future[Int] =
    db.run(meetings.filter(_.id === id).map(_.isDeleted).update(true))
}