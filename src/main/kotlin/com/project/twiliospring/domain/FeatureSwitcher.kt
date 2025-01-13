package com.project.twiliospring.domain

import java.util.logging.Logger

class FeatureSwitcher<T>(
    val name: String,
    private var flag: Boolean = false,
    private val default: T,
    private val alternative: T,
) {
    fun getInstance(): T {
        return if (flag) alternative else default
    }

    fun switch() {
        flag = !flag
        Logger.getLogger(name).info("Feature ${name} toggled to $flag")
    }

    fun getStateAsText(): String {
        return "State of feature $name is $flag (using ${default!!::class.java.simpleName})"
    }

    fun getState(): Boolean {
        return flag
    }
}