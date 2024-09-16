package com.project.twiliospring.service

import com.project.twiliospring.domain.User


interface MessengerService {
    fun sendMessage(text: String, user: User)
}
