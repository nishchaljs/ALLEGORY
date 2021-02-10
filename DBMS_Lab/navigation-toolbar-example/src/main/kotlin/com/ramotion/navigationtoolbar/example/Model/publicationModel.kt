package com.ramotion.navigationtoolbar.example.Model

import android.view.View

class publicationModel (var id: String, var name: String, var author: String, var type: String,
                        var genre: String, var desc: String, var img: String, var email:String,
                        var time: String, var date: String, var reads: String){

    var requestBtnClickListener: View.OnClickListener? = null
    constructor() : this("", "", "", "", "", "", "", "", "", "", "")
}