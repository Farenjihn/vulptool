package models

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

case class Event(id: Int, name: String, etype: String, meetingId: Int, raidId: Int, rosterId: Int)

case class Figure(id: Int, name: String, fclass: WoWClass, lvl: Int, ilvl: Double, playerId: Int)

case class Meeting(id: Int, date: String, time: String)

case class Player(mainPseudo: String, token: String)

case class Raid(id: Int, name: String, nb_boss: Int, difficulty: RaidDifficulty)

case class Roster(id: Int, name: String)

case class Template(id: Int, event: Event, rosterId: Int)

case class FigureRoster(figureId: Int, rosterId: Int)

