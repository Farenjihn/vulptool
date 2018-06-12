package models

import java.sql.Timestamp

import models.RaidDifficulty.RaidDifficulty
import models.WoWClass.WoWClass

object WoWClass extends Enumeration {
  type WoWClass = Value
  val Warrior, Paladin, DeathKnight, Hunter, Shaman, Rogue, Druid, Monk, DemonHunter, Priest, Mage, Warlock = Value
}

object RaidDifficulty extends Enumeration {
  type RaidDifficulty = Value
  val RaidFinder, Normal, Heroic, Mythic = Value
}

case class Event(id: Option[Int], name: String, description: String, meetingId: Int, raidId: Int, rosterId: Int)
case class EventFull(id: Option[Int], name: String, description: String, meeting: Meeting, raid: Raid, roster: Roster)

case class Figure(id: Option[Int], name: String, fclass: WoWClass, lvl: Int, ilvl: Double, playerId: Int)

case class FigureRoster(figureId: Int, rosterId: Int)

case class Meeting(id: Option[Int], timeBegin: Timestamp, timeEnd: Timestamp)

case class Player(id: Option[Int], mainPseudo: String, authCode: String, accessCode: String)

case class Raid(id: Option[Int], name: String, nbBoss: Int, difficulty: RaidDifficulty)

case class Roster(id: Option[Int], name: String)
case class RosterFull(id: Option[Int], name: String, figures: List[Figure])

case class Template(id: Option[Int], eventId: Int, rosterId: Int)
