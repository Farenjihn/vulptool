# Vulptool
## Description
Vulptool is a Raider Planner Tool to assist a World of Warcraft guild in its raid planning. It allows an administrator to create and manage events and rosters to share a calendar to the member of the guild.

## Trello
To see what is "left" to do, checkout our [Trello](https://trello.com/b/zlPaDtdk/vulptool). 

## Deployment

### Data Base
2. In terminal `$ docker-compose -f docker-compose-dev.yml up`. Might take some time on first launch
### Backend
1. Open project ***vulptool*** run with ***sbt*** `$ sbt`.
2. `$ run -Dhttps.port=9443 -Dhttp.port=disabled` to launch server in ***https*** (requiered for [OAuth](https://dev.battle.net/docs/read/oauth))

### Frontend
1. Start Backend.
2. Navigate to ***package.json*** in ***frontend*** folder.
3. Run `$ npm install` and `npm start`.
4. Home page should display in your browser.

## Authors
[Aurélie Lévy](https://github.com/AurelieLevy)
[Valentin Finini](https://github.com/Farenjihn)
[Miguel Pombo Dias](https://github.com/Ardgevald)
[Lawrence Stadler](https://github.com/Bykow)
