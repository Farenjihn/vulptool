package models

import java.text.SimpleDateFormat

import models.RaidDifficulty.RaidDifficulty
import models.WoWClasses.WoWClasses


object RaidDifficulty extends Enumeration {
  type RaidDifficulty = Value
  val RaidFinder, Normal, Heroic, Mythic = Value
}

object WoWClasses extends Enumeration {
  type WoWClasses = Value
  val Warrior, Paladin, DeathKnight, Hunter, Shaman, Rogue, Druid, Monk, DemonHunter, Priest, Mage, Warlock = Value
}

case class Player(mainPseudo: String, token: String)
case class Meeting(id: Int, date: String, time: String)
case class Event(id: Int, name: String, etype: String, meetingId: Int, raidId:Int)
case class Raid(id: Int, name: String, nb_boss: Int, difficulty: RaidDifficulty)
case class Roster(id: Int, name: String)
case class Figure(id: Int, name: String, fclasse: WoWClasses, lvl: Int, ilvl: Double, playerId: Int) // class : code word in scala: not permited -> fclass
case class Template(id: Int, event: Event, roster: Roster)
case class FigureRoster(figureId: Int, rosterId: Int)
