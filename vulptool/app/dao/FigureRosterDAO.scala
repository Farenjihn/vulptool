package dao

import javax.inject.{Inject, Singleton}
import models.FigureRoster
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

trait FiguresRosterComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  class FiguresRosterTable(tag: Tag) extends Table[FigureRoster](tag, "figure_roster") {

    def figure_id = column[Int]("figure_id")

    def roster_id = column[Int]("roster_id")

    def isDeleted = column[Boolean]("is_deleted")

    def * = (figure_id, roster_id) <> (FigureRoster.tupled, FigureRoster.unapply)
  }

}

@Singleton
class FigureRosterDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] with FiguresRosterComponent {

  import profile.api._

  val FiguresRoster = TableQuery[FiguresRosterTable]

  def list(): Future[Seq[FigureRoster]] = {
    val query = FiguresRoster.filter(!_.isDeleted)
    db.run(query.result)
  }
}