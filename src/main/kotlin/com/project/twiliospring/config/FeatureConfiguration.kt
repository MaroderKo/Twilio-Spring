package com.project.twiliospring.config

import com.project.twiliospring.service.F2AService
import com.project.twiliospring.service.impl.F2AServiceImpl
import com.project.twiliospring.service.impl.TwilioF2AServiceImpl
import com.project.twiliospring.util.FeatureSwitcher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeatureConfiguration {

    @Bean
    fun f2aServiceFeature(
        f2AServiceImpl: F2AServiceImpl,
        twilioF2AServiceImpl: TwilioF2AServiceImpl
    ): FeatureSwitcher<F2AService> {
        return FeatureSwitcher("TwilioF2AService", false, f2AServiceImpl, twilioF2AServiceImpl)
    }


}