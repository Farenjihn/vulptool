package dao

import java.sql.Date
import java.text.SimpleDateFormat

import javax.inject.{Inject, Singleton}
import models.Meeting
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

trait MeetingsComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  val dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

  class MeetingsTable(tag: Tag) extends Table[Meeting](tag, "meeting") {

    def id = column[Int]("id", O.PrimaryKey)

    def time = column[Date]("time")

    def isDeleted = column[Boolean]("is_deleted")

    def * = (id.?, dateFormat.format(time)) <> (Meeting.tupled, Meeting.unapply)
  }

}

@Singleton
class MeetingDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends MeetingsComponent with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  val meetings = TableQuery[MeetingsTable]

  def list(): Future[Seq[Meeting]] = {
    val query = meetings.filter(!_.isDeleted).sortBy(_.time)
    db.run(query.result)
  }

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
    db.run(meetings.filter(_.id === id).delete)
}