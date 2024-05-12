
# MCServer

This is the project for my Minecraft server. It's more like a personal server for me and my friends to play games and test stuff. It's based on Paper, MongoDB and Pterodactyl.

## Commands
#### There is pretty much just one command in the lobby:

/setvalue <spawn> - This sets the spawn location (Permission: lobby.setvalue)

#### For subservers:

/hub (also /lobby and /l) - Sends you to the lobby server (name must be lobby in Velocity config)

## Plugins
The project insists of 3 plugins: the lobby plugin, the subserver plugin and the velocity plugin. The lobby plugin responsible for almost everything. The subserver plugin runs the /hub (/lobby /l) command to return to the lobby server (name must be lobby in velocity) and the velocity plugin is responsible for registering created servers to the proxy.

## Features
- Server Navigator
    - Fully ingame customizable with a GUI (Permission: lobby.updatenavigator)
    - Sends player to subserver via plugin messaging
    - Shows the current status of the Server
    - Extra menu for private servers
- Server Creation 
    - Create a Server via GUI with the Admin item (Permission: lobby.adminitem)
    - Set name, port and specs
    - Server is automatically registered in Velocity (requires Velocity plugin installed)
- Server Management
    - Everything in a GUI with the Admin item (Permission: lobby.adminitem)
    - Delete the server
    - Set the server private/public
    - Allow players to join (if private)
    - Change name
    - Change specs
    - Execute commands
    - Install custom and predefined plugins (this plugin has support for BUtils by Banko if you set the license key in the config)
    - Delete plugins
    - Start/Restart/Stop server
    - See current utilization
- Standart listeners for block breaking etc. (Permissions: lobby.placeblocks, lobby.breakblocks)
- Prefix/Suffix rank system
    - Set priority, prefix and suffix for a specific permission in the ranks.yml file.

## Installation

Your servers need to be running in Pterodactyl containers.
You need:
- MongoDB set up on your server
- A Paper server 1.20.4 with the [lobby plugin](https://github.com/dqmme/mcserver/releases/latest/download/lobby.jar) installed.
- A Velocity server with the [Velocity plugin](https://github.com/dqmme/mcserver/releases/latest/download/velocity.jar) installed
- The [Pterodactyl egg](https://github.com/dqmme/mcserver/releases/latest/download/egg.json) set up.

Then you can fill in the values of the config files of the lobby plugin and you are good to go.

## Credits (thank you)
- [Emma for her Plugin template - very cool](https://github.com/emmaboecker/paper-kotlin-example)
- [jakobkmar for KSpigot - awesome utility library for delevoping kotlin plugins](https://github.com/jakobkmar/KSpigot)
- [Pterodactyl for managing the subservers](https://pterodactyl.io/)
- [Pterodactyl4J](https://github.com/mattmalec/Pterodactyl4J)
- [SignGUI](https://github.com/Rapha149/SignGUI)
- [KMongo](https://litote.org/kmongo/)
- [GSON](https://github.com/google/gson)
- [MinecraftHeads for the GUI Heads](https://minecraft-heads.com/)