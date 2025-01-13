package com.project.twiliospring.service.impl

import com.project.twiliospring.exception.FeatureNotFoundException
import com.project.twiliospring.domain.FeatureSwitcher
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FeatureServiceImplTest {

    private val feature1 = mock(FeatureSwitcher::class.java)
    private val feature2 = mock(FeatureSwitcher::class.java)

    init {
        whenever(feature1.name).thenReturn("feature1")
        whenever(feature1.getState()).thenReturn(true)
        whenever(feature2.name).thenReturn("feature2")
        whenever(feature2.getState()).thenReturn(false)
    }

    private val service = FeatureServiceImpl(listOf(feature1, feature2))

    @Test
    fun `getAll returns all features with their states`() {
        val allFeatures = service.getAll()
        assertEquals(mapOf("feature1" to true, "feature2" to false), allFeatures)
    }

    @Test
    fun `getByName returns correct feature`() {
        val feature = service.getByName("feature1")
        assertEquals(feature1, feature)
    }

    @Test
    fun `getByName throws exception when feature is not found`() {
        assertThrows<FeatureNotFoundException> {
            service.getByName("nonExistingFeature")
        }
    }
}