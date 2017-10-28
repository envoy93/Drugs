package com.shashov.drugs.features.drugs.data.local

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "drugs")
class Drugs {
    @PrimaryKey
    var _id: Int = 0
    var name: String? = ""
    var price: Int = -1
    var company: String? = "--"
    var substance: String? = "--"
    var form: String? = "--"
    var count: String = "--"
}

class Substance : ISearchItem {
    @PrimaryKey
    var _id: Int = 0
    var substance: String? = ""
}

class Drug : ISearchItem {
    @PrimaryKey
    var _id: Int = 0
    var name: String? = ""
    var substance: String? = ""
}

interface ISearchItem