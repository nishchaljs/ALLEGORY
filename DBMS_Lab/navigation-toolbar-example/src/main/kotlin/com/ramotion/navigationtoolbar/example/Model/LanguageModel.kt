package com.ramotion.navigationtoolbar.example.Model

import java.io.Serializable

class LanguageModel(name: String, code: String) : Serializable {


        var name: String? = name
        var code: String? = code

        companion object {

            fun getLanguageList(): ArrayList<LanguageModel> {
                val list = ArrayList<LanguageModel>()
                list.add(LanguageModel("Select Language", "lang"))
                list.add(LanguageModel("English", "en"))
                list.add(LanguageModel("Hindi", "hi"))
                list.add(LanguageModel("Bengali", "bn"))
                list.add(LanguageModel("Nepali", "ne"))
                list.add(LanguageModel("Swedish", "sv"))
                return list
            }
        }
    }