package dao

// This class convert the database's students table in a object-oriented entity: the Student model.
class LocationsTable(tag: Tag) extends Table[Location](tag, "LOCATIONS") {
  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc) // Primary key, auto-incremented
  def name = column[String]("NAME")

  // Map the attributes with the model; the ID is optional.
  def * = (id.?, firstName)
}

val locations = TableQuery[LocationsTable]

def List(): Future[Seq[Location]] = {
  val query = locations.sortBy(s => s.name)
  db.run(query.result)
}


