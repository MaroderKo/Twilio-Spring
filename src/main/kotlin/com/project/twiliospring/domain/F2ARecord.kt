package com.project.twiliospring.domain

data class F2ARecord(
    val code: String,
) {
    fun isCodeValid(code: String): Boolean {
        return this.code == code
    }
}