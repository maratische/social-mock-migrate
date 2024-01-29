package com.examples.uidcreator.util

class UserUtils {

    companion object {
        private const val APPLE_SOCIAL_UID = "000123.cd12a05534514929abbf54c4b4e36fda.0123"

        const val fName = "John"
        const val lName = "Doe"

        val usersMap: MutableMap<String, User> = hashMapOf(
            "aaa@test.com" to User(
                "aaa@test.com",
                "Abraham",
                "Simpson",
                Utils.md5("aaa@test.com"),
                "[\"${Gender.Male.str}\"]",
                "[\"${Country.Argentina}\", \"${Country.Finland}\"]",
                "2002-02-23",
                "+77472558790",
                "text",
                "[\"${State.California}\"]",
                "True",
                "29"
            ),
            "bbb@test.com" to User(
                "bbb@test.com",
                "Jane",
                "Simpson",
                Utils.md5("bbb@test.com"),
                "[\"${Gender.Female.str}\"]",
                "[\"${Country.Germany}\", \"${Country.Finland}\"]",
                "1945-05-09",
                "+497482558790",
                "Test message from burgher",
                "[]",
                "False",
                "72"
            ),
            "ccc@test.com" to User(
                "ccc@test.com",
                "Abraham",
                "Simpson",
                Utils.md5("ccc@test.com"),
                "[\"${Gender.Female.str}\"]",
                "[\"${Country.France}\", \"${Country.Finland}\"]",
                "1979-12-11",
                "+334225587490",
                "Test message from Paris",
                "[]",
                "False",
                "25"
            ),
            "ddd@test.com" to User(
                "ddd@test.com",
                "Abraham",
                "Simpson",
                Utils.md5("ddd@test.com"),
                "[\"${Gender.Female.str}\"]",
                "[\"${Country.Estonia}\",\"${Country.Austria}\"]",
                "2001-07-09",
                "+15552558790",
                "Test message from LA",
                "[\"${State.California}\"]",
                "False",
                "19"
            ),
            "eee@test.com" to User(
                "eee@test.com",
                "Jane",
                lName,
                Utils.md5("eee@test.com"),
                "[\"${Gender.Other.str}\"]",
                "[\"${Country.Brazil}\",\"${Country.Germany}\"]",
                "2002-06-30",
                "+557472558790",
                "Brasil campeão mundial",
                "[\"${State.Texas}\"]",
                "False",
                "33"
            ),
            "test@test.com" to User(
                "test@test.com",
                fName,
                lName,
                Utils.md5("test@test.com"),
                "[\"${Gender.Male.str}\"]",
                "[\"${Country.Austria}\", \"${Country.Finland}\"]",
                "2010-01-29",
                "+537472558790",
                "Test message from austrian burgher",
                "[\"${State.Utah}\"]",
                "False",
                "51"
            ),
            "apple@test.com" to User(
                "apple@test.com",
                fName,
                lName,
                APPLE_SOCIAL_UID,
                "[\"${Gender.Male.str}\"]",
                "[\"${Country.Italy}\", \"${Country.Finland}\"]",
                "2006-07-09",
                "+497472558790",
                "Forza Italia!",
                "[\"${State.Virginia}\"]",
                "False",
                "35"
            ),
            Utils.EMAIL_QA to User(
                Utils.EMAIL_QA,
                Utils.FULL_NAME.split(" ")[0],
                Utils.FULL_NAME.split(" ")[1],
                Utils.md5(Utils.EMAIL_QA),
                "[\"${Gender.Male.str}\"]",
                "[\"${Country.Armenia}\", \"${Country.Finland}\"]",
                "2014-05-09",
                "+497472558790",
                "Test message from tester",
                "[\"${State.Nebraska}\"]",
                "True",
                "23"
            ),
            "eee@test.com" to User(
                "eee@test.com",
                "Jane",
                lName,
                Utils.md5("eee@test.com"),
                "[\"${Gender.NonBinary.str}\"]",
                "[\"${Country.Brazil}\",\"${Country.Germany}\"]",
                "2002-06-30",
                "+557472558790",
                "Brasil campeão mundial",
                "[\"${State.Texas}\"]",
                "False",
                "33"
            )
        )
    }

    data class User(
        var email: String,
        var givenName: String,
        var familyName: String,
        var socUid: String,
        var gender: String? = null, // select from Gender
        var countries: String? = null, // multiselect from Country
        var birthDay: String? = null, // date
        var phone: String? = null,
        var message: String? = null, // any text
        var state: String? = null, // single select State
        var boolean: String? = null, // typical checkbox
        var counter: String? = null // number field
    ) {
//        constructor(email: String, fName: String, lName: String, socUid: String, gender: String) : this()

        fun asMap() : Map<String, String?> {
            val map:MutableMap<String, String?> = mutableMapOf()
            map["email"] = email
            map["fName"] = givenName
            map["lName"] = familyName
            map["socUid"] = socUid
            map["gender"] = gender
            map["state"] = state
            map["countries"] = countries
            map["birthDay"] = birthDay
            map["phone"] = phone
            map["message"] = message
            map["boolean"] = boolean
            map["counter"] = counter
            return map
        }
    }

    enum class Gender(val str: String) {
        Male("Male"), Female("Female"), NonBinary("Non-binary"), Other("Other")
    }
    enum class Country {
        Armenia, Argentina, Austria, France, Germany, Finland, Estonia, Brazil, Italy
    }
    enum class State {
        Utah, Virginia, Nebraska, Texas, California
    }

}