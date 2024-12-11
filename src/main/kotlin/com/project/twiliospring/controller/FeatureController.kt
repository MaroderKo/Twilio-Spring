package com.project.twiliospring.controller

import com.project.twiliospring.service.FeatureService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/feature")
class FeatureController(
    private val featureService: FeatureService
) {
    @GetMapping("/getAll")
    fun getAll(): ResponseEntity<Map<String, Boolean>> {
        return ResponseEntity.ok(featureService.getAll())
    }

    @GetMapping("/{name}/state")
    fun getF2aFeatureState(@PathVariable name: String): ResponseEntity<String> {
        return ResponseEntity.ok(featureService.getByName(name).getStateAsText())
    }

    @PostMapping("/{name}/switch")
    fun switchF2AFeature(@PathVariable name: String): ResponseEntity<String> {
        featureService.getByName(name).switch()
        return ResponseEntity.ok(featureService.getByName(name).getStateAsText())
    }
}