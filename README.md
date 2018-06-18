# Vulptool
## Description
Le projet Vulptool a été fait dans le cadre du cours Scala 2018 de la HEIG-VD. Le but est de créer une application permettant la gestion de raids d'une guilde World of Warcraft. Cela implique de pouvoir gérer les joueurs de la guilde, les rencontres, les raids, etc.
## Technologies
Pour réaliser ce projet nous avons utilisé :

1. [Scala Play](https://www.playframework.com/) pour le backend 
2. [Slick](http://slick.lightbend.com/) et [MariaDB](https://mariadb.org/) pour la base de données
3. [JavaScript](https://www.javascript.com/), [React](https://reactjs.org/) et [Ant Design](https://ant.design/) pour le frontend
4. [OAuth](https://dev.battle.net/docs/read/oauth) (fournit par Blizzard) afin d’accéder aux données sensibles qui requièrent l’autorisation de l’utilisateur à qui elles appartiennent (données “privées”).
5. [Docker](https://www.docker.com/) avec Docker-compose pour déployer le tout

## Deploiement
### Backend
1. Se placer au niveau du fichier ***docker-compose-dev.yml***.
2. lancer la commande `docker-compose -f docker-compose-dev.yml up`. Attention, cela peut prendre un certain temps...
3. Ouvrir le projet nommé ***vulptool*** et lancer l'application avec Play (fichier ***build.sbt***).
### Frontend
1. Démarrer le Backend.
2. Se placer au niveau du fichier ***package.json***.
3. Lancer les commandes `npm install` puis `npm start`.
4. La page d'accueil de l'application devrait s'ouvrir automatiquement dans votre browser.

## Auteurs
Les auteurs sont: [Aurélie Lévy](https://github.com/AurelieLevy), [Valentin Finini](https://github.com/Farenjihn), [Miguel Pombo Dias](https://github.com/Ardgevald) et [Lawrence Stadler](https://github.com/Bykow)
