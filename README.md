# LePetitBot ✨
![[discord](https://discord.gg/invite/FXS9zMm)](https://badgen.net/badge/discord/invite/yellow?icon=discord)

LePetitBot est un bot discord utilisant les librairies suivantes :
- [JDA][jda]
- [Jetty][jetty]
- [Spotify -Web-Api-Java][spotify-api]
- [Lavaplayer][lavaplayer]

il permet la lecture de vidéos provenant de Youtube, mais aussi un système de commandes adaptées à la nouvelle API de Discord (/), ainsi qu'un système d'action row
permettant des intéractions utilisateurs plus fluide.

# Comment utiliser le bot ?

Deux API sont nécessaires au bon fonctionnement du bot, d'abord, un token de bot discord, en suite, une clé d'application spotify, (id & secret).
Il faudra ensuite créer un fichier **"credentials.json"** dans le dossier **"resources"**

Exemple de credentials.json :
```json
{
  "spotify": "idApplicationSpotify",
  "spotifySecret": "secretApplicationSpotify",
  "discordToken": "tokenDiscordBlablabla"
}
```
en suite, il faudra modifier les différentes variables intéragissant avec sois un serveur spécifique (guild), sois un channel spécifique.


# Comment rajouter une commande ?

Pour créer une commande il suffit de créer une nouvelle class qui doit implémenter **"ITCommand"**, puis l'enregistrer dans l'arraylist **"commands"**
située dans **"CommandManager"**


[jda]: https://github.com/DV8FromTheWorld/JDA/
[jetty]: https://github.com/eclipse/jetty.project
[spotify-api]: https://github.com/spotify-web-api-java/spotify-web-api-java
[lavaplayer]: https://github.com/sedmelluq/lavaplayer

# Crédits

Copyright Fyz - 2022
Sous license MIT

Vous pouvez utiliser, modifier, et partager le code de ce repo avec la simple condition de me créditer en cas d'utilisation.
