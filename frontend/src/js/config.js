export const baseURL = "https://localhost:9443";

export const DeathKnight  =	"#C41F3B";
export const DemonHunter  = "#A330C9";
export const Druid        = "#FF7D0A";
export const Hunter       = "#ABD473";
export const Mage         = "#69CCF0";
export const Monk         = "#00FF96";
export const Paladin      = "#F58CBA";
export const Priest 	    = "#FFFFFF";
export const Rogue 	      = "#FFF569";
export const Shaman 	    = "#0070DE";
export const Warlock      = "#9482C9";
export const Warrior 	    =	"#C79C6E";

export function getColorForClass(fclass) {
  let color;
  switch (fclass) {
    case "DeathKnight":
      color = DeathKnight;
      break;
    case "DemonHunter":
      color = DemonHunter;
      break;
    case "Druid":
      color = Druid;
      break;
    case "Hunter":
      color = Hunter;
      break;
    case "Mage":
      color = Mage;
      break;
    case "Monk":
      color = Monk;
      break;
    case "Paladin":
      color = Paladin;
      break;
    case "Priest":
      color = Priest;
      break;
    case "Rogue":
      color = Rogue;
      break;
    case "Shaman":
      color = Shaman;
      break;
    case "Warlock":
      color = Warlock;
      break;
    case "Warrior":
      color = Warrior;
      break;
    default:
      color = "#000000";
  }
  return color;
}

export function getImgForClass(fclass) {
  let imgPath;
  switch (fclass) {
    case "DeathKnight":
      imgPath = require("../img/classes_deathknight.png");
      break;
    case "DemonHunter":
      imgPath = require("../img/classes_demonhunter.png");
      break;
    case "Druid":
      imgPath = require("../img/classes_druid.png");
      break;
    case "Hunter":
      imgPath = require("../img/classes_hunter.png");
      break;
    case "Mage":
      imgPath = require("../img/classes_mage.png");
      break;
    case "Monk":
      imgPath = require("../img/classes_monk.png");
      break;
    case "Paladin":
      imgPath = require("../img/classes_paladin.png");
      break;
    case "Priest":
      imgPath = require("../img/classes_priest.png");
      break;
    case "Rogue":
      imgPath = require("../img/classes_rogue.png");
      break;
    case "Shaman":
      imgPath = require("../img/classes_shaman.png");
      break;
    case "Warlock":
      imgPath = require("../img/classes_warlock.png");
      break;
    case "Warrior":
      imgPath = require("../img/classes_warrior.png");
      break;
    default:
      imgPath = "";
  }
  return imgPath;
}














