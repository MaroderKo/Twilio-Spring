package com.project.twiliospring.service.impl

import com.project.twiliospring.service.F2AService
import com.twilio.rest.verify.v2.Service
import com.twilio.rest.verify.v2.service.Verification
import com.twilio.rest.verify.v2.service.VerificationCheck
import jakarta.annotation.PreDestroy

@org.springframework.stereotype.Service
class TwilioF2AServiceImpl(
    private val verificationService: Service = Service.creator("Dex verification service").create(),
) : F2AService {

    @PreDestroy
    protected fun serviceRemover() {
        Service.deleter(verificationService.sid).delete()
    }

    override fun createRecord(number: String) {
        Verification.creator(verificationService.sid, number, "whatsapp").create()
    }

    override fun isCodeValid(number: String, code: String): Boolean {
        val verificationCheck = VerificationCheck.creator(verificationService.sid)
            .setTo(number)
            .setCode(code)
            .create()

        return verificationCheck.status == "approved"
    }
}