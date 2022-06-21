package com.first_java_app.k_login_signup.model

class UserHelperClassGadget {
    var btnID: String? = null
    var btnValue: String? = null
    var btnName: String? = null
    var widType: String? = null
    var gestureT: String? = null
    var espPin: String? = null

    constructor() {}
    constructor(
        BtnID: String?,
        btnName: String?,
        btnValue: String?,
        widType: String?,
        gestureT: String?,
        espPin: String?
    ) {
        btnID = BtnID
        this.btnValue = btnValue
        this.btnName = btnName
        this.widType = widType
        this.gestureT = gestureT
        this.espPin = espPin
    }

//    fun getbtnValue(): String? {
//        return btnValue
//    }

//    fun setbtnValue(btnValue: String?) {
//        this.btnValue = btnValue
//    }

//    fun getbtnName(): String? {
//        return btnName
//    }

//    fun setbtnName(btnName: String?) {
//        this.btnName = btnName
//    }
}