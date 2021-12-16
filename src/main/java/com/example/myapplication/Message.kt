package com.example.myapplication

class Message {
    var message: String? = null
    var senderID: String? = null

    //Empty Constructor
    constructor() {}

    constructor(message: String?, senderId: String?) {
        this.message = message
        this.senderID = senderId
    }

}