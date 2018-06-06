package dao

import models.{Figure, WoWClasses, Player}
import models.WoWClasses._
import slick.jdbc.JdbcProfile
import java.text.SimpleDateFormat

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

trait FiguresComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  implicit val classesMapper = MappedColumnType.base[WoWClasses, String](
    e => e.toString,
    s => WoWClasses.withName(s)
  )

  class FiguresTable(tag: Tag) extends Table[Figure](tag, "FIGURES") {

    def id = column[Int]("id", O.PrimaryKey)
    def name = column[String]("figure_name")
    def fclasse = column[String]("classe")
    def lvl = column[Int]("lvl")
    def ilvl = column[Double]("ilvl")
    def playerId = column[Int]("player")
    def isDeleted = column[Boolean]("is_deleted")

    def * = (id, fclasse, lvl, ilvl, playerId) <> (Figure.tupled, Figure.unapply)
  }

}

@Singleton
class FigureDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends FiguresComponent with PlayersComponent with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  val figures = TableQuery[FiguresTable]

  def list(): Future[Seq[Figure]] = {
    val query = figures.filter(!_.isDeleted).sortBy(s => s.name)
    db.run(query.result)
  }

  def findById(id: Int): Future[Option[Figure]] =
    db.run(figures.filter(_.id === id).result.headOption)

  def insert(figure: Figure): Future[Figure] = {
    val insertQuery = figures returning figures.map(_.id) into ((figure, id) => figure.copy(id))
    db.run(insertQuery += figure)
  }

  def update(id: Int, figure: Figure): Future[Int] = {
    val figureToUpdate: Figure = figure.copy(id)
    db.run(figures.filter(_.id === id).update(figureToUpdate))
  }

  def delete(id: Int): Future[Int] =
    db.run(figures.filter(_.id === id).delete)
}