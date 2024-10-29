package com.project.twiliospring.config

import com.twilio.Twilio
import com.twilio.rest.verify.v2.Service
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
class TwilioConfiguration(
    @Value("\${twilio.sid}") val twilioSID: String,
    @Value("\${twilio.auth-token}") val twilioToken: String

) {

    @PostConstruct
    fun initTwilio() {
        Twilio.init(twilioSID, twilioToken)
    }

    @Profile("twilio")
    @Bean(name = ["twilioVerifyService"])
    fun verifyService(): Service {
        val service: Service = Service.creator("Dex verification service").create()
        return service
    }
}