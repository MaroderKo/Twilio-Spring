package com.project.twiliospring.service

import com.project.twiliospring.domain.FeatureSwitcher

interface FeatureService {
    fun getAll(): Map<String, Boolean>
    fun getByName(name: String): FeatureSwitcher<*>
}