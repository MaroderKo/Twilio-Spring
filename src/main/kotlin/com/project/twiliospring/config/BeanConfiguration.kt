package com.project.twiliospring.config

import com.twilio.Twilio
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class BeanConfiguration(
    @Value("\${twilio.sid}") val twilioSID: String,
    @Value("\${twilio.auth-token}") val twilioToken: String

) {

    @PostConstruct
    fun initTwilio() {
        Twilio.init(twilioSID, twilioToken)
    }
}