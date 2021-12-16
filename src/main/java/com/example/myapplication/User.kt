package com.example.myapplication

//This is not simply a data class because FireBase needs an empty constructor to work with
class User {
    var name: String? = null
    var email: String? = null
    var uID: String? = null

    var rollNumber: Int? = null
    var studentIdNumber: Int? = null
    var department: String? = null
    var year: Int? = null

    var student: Boolean? = null
    var facultyMember: Boolean? = null


    //Set to -1, so it cannot be possible to reference another image
    val NO_IMAGE_PROVIDED = -1

    var mImageResourceID = NO_IMAGE_PROVIDED


    constructor() {

    }

    constructor(name: String?, email: String?, uID: String?) {
        this.name = name
        this.email = email
        this.uID = uID
    }

    //Use this constructor if there is an image available
    constructor(name: String?, email: String?, uID: String?, imageResourceID: Int) {
        this.name = name
        this.email = email
        this.uID = uID
        this.mImageResourceID = imageResourceID
    }

}