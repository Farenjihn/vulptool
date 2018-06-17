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
      imgPath = "https://d1u5p3l4wpay3k.cloudfront.net/wowpedia/e/e5/Ui-charactercreate-classes_deathknight.png?version=352e19693f9ee71c3d111c2e1206cf83";
      break;
    case "DemonHunter":
      imgPath = "https://d1u5p3l4wpay3k.cloudfront.net/wowpedia/c/c9/Ui-charactercreate-classes_demonhunter.png?version=c8d49e3137508a879c7bcd8337d5f8b5";
      break;
    case "Druid":
      imgPath = "https://d1u5p3l4wpay3k.cloudfront.net/wowpedia/6/6f/Ui-charactercreate-classes_druid.png?version=89da324cd8f843e2967f05971f140b95";
      break;
    case "Hunter":
      imgPath = "https://d1u5p3l4wpay3k.cloudfront.net/wowpedia/4/4e/Ui-charactercreate-classes_hunter.png?version=f4bba4b664997085ca8dc3f9679652de";
      break;
    case "Mage":
      imgPath = "https://d1u5p3l4wpay3k.cloudfront.net/wowpedia/5/56/Ui-charactercreate-classes_mage.png?version=74e8909f0d8ba3e10fce4f1afcc639cc";
      break;
    case "Monk":
      imgPath = "https://d1u5p3l4wpay3k.cloudfront.net/wowpedia/2/24/Ui-charactercreate-classes_monk.png?version=649da46ade703377dff5f8b7c023148e";
      break;
    case "Paladin":
      imgPath = "https://d1u5p3l4wpay3k.cloudfront.net/wowpedia/8/80/Ui-charactercreate-classes_paladin.png?version=b81e1945ede522b46fc5fd30ecb61286";
      break;
    case "Priest":
      imgPath = "https://d1u5p3l4wpay3k.cloudfront.net/wowpedia/0/0f/Ui-charactercreate-classes_priest.png?version=ae26ea9d4c5ddb34dfc0ef0fd3ee8f6f";
      break;
    case "Rogue":
      imgPath = "https://d1u5p3l4wpay3k.cloudfront.net/wowpedia/b/b1/Ui-charactercreate-classes_rogue.png?version=6405176e156523f72ec7be6159ca4fa2";
      break;
    case "Shaman":
      imgPath = "https://d1u5p3l4wpay3k.cloudfront.net/wowpedia/3/3e/Ui-charactercreate-classes_shaman.png?version=0882a3d11da75140ae7d151caed18e1c";
      break;
    case "Warlock":
      imgPath = "https://d1u5p3l4wpay3k.cloudfront.net/wowpedia/c/cf/Ui-charactercreate-classes_warlock.png?version=16acb40f7db7c0b7040dfbabdce21d8d";
      break;
    case "Warrior":
      imgPath = "https://d1u5p3l4wpay3k.cloudfront.net/wowpedia/3/37/Ui-charactercreate-classes_warrior.png?version=aa88de39ff7ac3a553ec38e3ed5d4e43";
      break;
    default:
      imgPath = "";
  }
  return imgPath;
}














