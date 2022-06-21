package com.first_java_app.k_login_signup

class UserHelperClass {
    var name: String? = null
    var username: String? = null
    var email: String? = null
    var phoneNo: String? = null
    var password: String? = null

    constructor() {}
    constructor(
        name: String?,
        username: String?,
        email: String?,
        phoneNo: String?,
        password: String?
    ) {
        this.name = name
        this.username = username
        this.email = email
        this.phoneNo = phoneNo
        this.password = password
    }
}