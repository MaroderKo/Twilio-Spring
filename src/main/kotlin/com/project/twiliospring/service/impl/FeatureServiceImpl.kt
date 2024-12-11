package com.project.twiliospring.service.impl

import com.project.twiliospring.exception.FeatureNotFoundException
import com.project.twiliospring.service.FeatureService
import com.project.twiliospring.util.FeatureSwitcher
import org.springframework.stereotype.Service

@Service
class FeatureServiceImpl(
    featureList: List<FeatureSwitcher<*>>,
) : FeatureService {

    private val features: Map<String, FeatureSwitcher<*>> = featureList.associateBy { it.name }

    override fun getAll(): Map<String, Boolean> {
        return features.mapValues { (_, value) -> value.getState() }
    }

    override fun getByName(name: String): FeatureSwitcher<*> {
        return features[name] ?: throw FeatureNotFoundException()
    }
}