package com.project.twiliospring.service.impl

import com.project.twiliospring.service.F2AService
import com.twilio.rest.verify.v2.Service
import com.twilio.rest.verify.v2.service.Verification
import com.twilio.rest.verify.v2.service.VerificationCheck
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Profile

@Profile("twilio")
@org.springframework.stereotype.Service
class TwilioF2AServiceImpl(
    @Qualifier("twilioVerifyService") private val verificationService : Service,
) : F2AService {
    override fun createRecord(number: String) {
        Verification.creator(verificationService.sid, number, "whatsapp").create()
    }

    override fun checkCode(number: String, code: String): Boolean {
        val verificationCheck = VerificationCheck.creator(verificationService.sid)
            .setTo(number)
            .setCode(code)
            .create()

        return verificationCheck.status == "approved"
    }
}