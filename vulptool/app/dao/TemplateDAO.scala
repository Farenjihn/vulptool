package dao

import javax.inject.{Inject, Singleton}
import models.Template
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

trait TemplatesComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import profile.api._

  class TemplatesTable(tag: Tag) extends Table[Template](tag, "template") {

    def id = column[Int]("id", O.PrimaryKey)

    def eventId = column[Int]("saved_event_id")

    def rosterId = column[Int]("roster_id")

    def isDeleted = column[Boolean]("is_deleted")

    def * = (id.?, eventId, rosterId) <> (Template.tupled, Template.unapply)
  }

}

@Singleton
class TemplateDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends TemplatesComponent with HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  val templates = TableQuery[TemplatesTable]

  def list(): Future[Seq[Template]] = {
    val query = templates.filter(!_.isDeleted)
    db.run(query.result)
  }

  def findById(id: Int): Future[Option[Template]] =
    db.run(templates.filter(_.id === id).result.headOption)

  def insert(template: Template): Future[Template] = {
    val insertQuery = templates returning templates.map(_.id) into ((template, id) => template.copy(Some(id)))
    db.run(insertQuery += template)
  }

  def update(id: Int, template: Template): Future[Int] = {
    val templateToUpdate: Template = template.copy(Some(id))
    db.run(templates.filter(_.id === id).update(templateToUpdate))
  }

  def delete(id: Int): Future[Int] =
    db.run(templates.filter(_.id === id).delete)
}