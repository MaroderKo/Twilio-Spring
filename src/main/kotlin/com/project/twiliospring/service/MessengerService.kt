package com.project.twiliospring.service


fun interface MessengerService {
    fun sendMessage(text: String, number: String)
}
