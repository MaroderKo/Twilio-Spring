package com.project.twiliospring.service.impl

import com.project.twiliospring.domain.User
import com.project.twiliospring.service.MessengerService
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service

class TwilioMessengerService(
    @Value("\${twilio.phone-number}") val twilioPhone: String
) : MessengerService {
    val logger: Logger = Logger.getLogger(MessengerService::class.java.name)
    override fun sendMessage(text: String, user: User) {
        val message = Message.creator(
            PhoneNumber(user.phoneNumber),
            PhoneNumber(twilioPhone),
            text
        )
            .create()
        logger.info("Sent message with price ${Message.fetcher(message.sid).fetch().price}")
    }
}