package com.project.twiliospring.service

import com.project.twiliospring.util.FeatureSwitcher

interface FeatureService {
    fun getAll(): Map<String, Boolean>
    fun getByName(name: String): FeatureSwitcher<*>
}