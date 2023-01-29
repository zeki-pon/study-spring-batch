package com.example.studyspringbatch.batchprocessing

class Person {
    var lastName: String? = null
    var firstName: String? = null

    constructor() {}
    constructor(lastName: String?, firstName: String?) {
        this.lastName = lastName
        this.firstName = firstName
    }

    override fun toString(): String {
        return "firstName: $firstName , lastName: $lastName"
    }
}