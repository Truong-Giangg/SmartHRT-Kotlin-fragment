package com.first_java_app.k_login_signup

import com.first_java_app.k_login_signup.model.Restaurant
import com.first_java_app.k_login_signup.model.User


class DataStore private constructor(var fullName : String, var email: String, var password: String){
    companion object{
        private var instance : User? = null
        operator fun invoke(fullName : String, email: String, password: String): User = synchronized(this){
            if(instance == null)
                instance = User(fullName,email,password)
            return instance as User
        }
    }
}

object RestaurantStore{
    private var arraylist : ArrayList<Restaurant>? = null
    fun getDataset(): ArrayList<Restaurant>{
        if(arraylist == null){
            arraylist = arrayListOf(
                Restaurant(33760,"Du Miên Garden Cafe","7 Phan Văn Trị, P. 10","https://images.foody.vn/res/g4/33760/prof/s180x180/foody-mobile-du-mien-garden-mb-jp-309-635785114907746480.jpg"),
                Restaurant(978,"Country House Cafe","18C Phan Văn Trị, P. 10","https://images.foody.vn/res/g1/978/prof/s180x180/foody-mobile-countryhouse-jpg-246-635683259648435945.jpg"),
                Restaurant(82801,"Hẻm Spaghetti","212/22 Nguyễn Oanh, P. 17","https://images.foody.vn/res/g9/82801/prof/s180x180/foody-upload-api-foody-mobile-gi4-jpg-181001160804.jpg"),
                Restaurant(149154,"Lava Coffee","61 Quang Trung, P. 10","https://images.foody.vn/res/g15/149154/prof/s180x180/foody-upload-api-foody-mobile-4-190111100956.jpg"),
                Restaurant(194867,"Mì Cay Naga","224 Phạm Văn Đồng, P.1 ","https://images.foody.vn/res/g20/194867/prof/s180x180/foody-mobile-9zbk996o-jpg-112-636143635496125338.jpg"),
                Restaurant(25656, "City House Cafe - Sân Vườn Lãng Mạn","21 Huỳnh Khương An, P. 5","https://images.foody.vn/res/g3/25656/prof/s180x180/foody-mobile-city-house-jpg-354-636133373358973749.jpg"),
                Restaurant(66844,"Nhi Nhi Quán - Đặc Sản Phan Rang","125/48 Lê Đức Thọ, P. 17","https://images.foody.vn/res/g7/66844/prof/s180x180/foody-mobile-bpzy3ni3-jpg-841-636143721820688231.jpg"),
                Restaurant(33645,"Yuri Yuri - Ẩm Thực Hàn Quốc","358 Nguyễn Văn Nghi, P. 7","https://images.foody.vn/res/g4/33645/prof/s180x180/foody-mobile-bayef6n3-jpg-308-636023635570133301.jpg"),
                Restaurant(252442,"Trà Sữa Gong Cha - 貢茶","595 Phan Văn Trị, P. 5","https://images.foody.vn/res/g26/252442/prof/s180x180/foody-mobile-12-jpg-323-636148043123028599.jpg"),
                Restaurant(12513,"Oasis Cafe","26/14 Phạm Văn Đồng, P. 3","https://images.foody.vn/res/g2/12513/prof/s180x180/foody-mobile-oasis-cafe-tp-hcm.jpg"),
                Restaurant(199622,"Buzza Pizza","Tầng Trệt Emart Gò Vấp - 366 Phan Văn Trị, P. 5","https://images.foody.vn/res/g20/199622/prof/s180x180/201652411519-mobile-foody-logo-2-640x400.jpg"),
                Restaurant(651063,"Dallas Cakes & Coffee","6 Quang Trung, P. 10","https://images.foody.vn/res/g66/651063/prof/s180x180/foody-mobile-11090821_80492442289-251-636277684533992519.jpg"),
                Restaurant(60915,"Hot & Cold - Trà Sữa & Xiên Que","804 Quang Trung","https://images.foody.vn/res/g7/60915/prof/s180x180/foody-upload-api-foody-mobile-1-190605180036.jpg"),
                Restaurant(112585,"Papaxốt","458 Quang Trung","https://images.foody.vn/res/g12/112585/prof/s180x180/foody-upload-api-foody-mobile-7-190109171633.jpg"),
                Restaurant(91979,"SaiGon Chic Cafe","82 Đường Số 27, P. 6","https://images.foody.vn/res/g10/91979/prof/s180x180/foody-mobile-kjuxujry-jpg-982-635838930416211667.jpg"),
                Restaurant(28883,"Pizza Hut","283 Quang Trung","https://images.foody.vn/res/g3/28883/prof/s180x180/foody-mobile-p-jpg-800-635757703358128351.jpg"),
                Restaurant(129725,"Bánh Mì Chảo & Món Ngon Hoa Việt - Quán Cô 3 Hậu","36 Đường Số 18, P. 8","https://images.foody.vn/res/g13/129725/prof/s180x180/foody-mobile-foody-quan-co-3-hau--960-635652896252263911.jpg"),
                Restaurant(46668,"Kichi Kichi Lẩu Băng Chuyền","1 Quang Trung","https://images.foody.vn/res/g5/46668/prof/s180x180/foody-mobile-rfokfbsk-jpg-859-635796372049634356.jpg"),
                Restaurant(158187,"The Coffee House","293 Quang Trung","https://images.foody.vn/res/g16/158187/prof/s180x180/foody-mobile-coffeehousequangtrun-822-635754530645782536.jpg"),
                Restaurant(30102,"Cánh Đồng Quán - Lẩu Nướng Tại Bàn","310/14 Dương Quảng Hàm, P. 5","https://images.foody.vn/res/g4/30102/prof/s180x180/foody-mobile-shwadjuj-jpg-413-636204369735412311.jpg"),
                Restaurant(83857,"Chú Tèo Buffet Nướng 99K","N01 Chung Cư K26 Dương Quảng Hàm, P. 7","https://images.foody.vn/res/g9/83857/prof/s180x180/201722393753-avatar1.jpg")
            )
        }
        return arraylist as ArrayList<Restaurant>
    }
}