package dao

import javax.inject.{Inject, Singleton}
import models.RaidDifficulty.RaidDifficulty
import models.{Raid, RaidDifficulty}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

trait RaidsComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  implicit lazy val mapper = MappedColumnType.base[RaidDifficulty.Value, String](
    e => e.toString,
    s => RaidDifficulty.withName(s)
  )

  class RaidsTable(tag: Tag) extends Table[Raid](tag, "raid") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def nbBoss = column[Int]("nb_boss")

    def difficulty = column[RaidDifficulty]("difficulty")

    def isDeleted = column[Boolean]("is_deleted")

    def * = (id.?, name, nbBoss, difficulty) <> (Raid.tupled, Raid.unapply)
  }

}

@Singleton
class RaidDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends RaidsComponent with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  val raids = TableQuery[RaidsTable]

  def list(): Future[Seq[Raid]] = {
    val query = raids.filter(!_.isDeleted).sortBy(_.name)
    db.run(query.result)
  }

  def findById(id: Int): Future[Option[Raid]] =
    db.run(raids.filter(_.id === id).result.headOption)

  def insertIfNotExists(raid: Raid): Future[Raid] = {
    val a = {
      raids.filter(_.nbBoss === raid.nbBoss)
        .filter(_.name === raid.name)
        .filter(_.difficulty === raid.difficulty)
        .result.headOption.flatMap {
        case Some(raid) =>
          DBIO.successful(raid) // no-op
        case None =>
          val raidId =
            (raids returning raids.map(_.id)) += Raid(
              raid.id,
              raid.name,
              raid.nbBoss,
              raid.difficulty
            )

          val newRaid = raidId.map { id =>
            Raid(
              Some(id),
              raid.name,
              raid.nbBoss,
              raid.difficulty
            )
          }
          newRaid

      }
    }
    db.run(a.transactionally)
  }

  def update(id: Int, raid: Raid): Future[Int] = {
    val raidToUpdate: Raid = raid.copy(Some(id))
    db.run(raids.filter(_.id === id).update(raidToUpdate))
  }

  def delete(id: Int): Future[Int] =
    db.run(raids.filter(_.id === id).map(_.isDeleted).update(true))
}