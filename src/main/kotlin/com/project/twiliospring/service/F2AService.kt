package com.project.twiliospring.service

interface F2AService {
    fun createRecord(number: String)
    fun isCodeValid(number: String, code: String): Boolean
}