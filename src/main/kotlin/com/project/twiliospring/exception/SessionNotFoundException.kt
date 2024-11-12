package com.project.twiliospring.exception

class SessionNotFoundException : LoginException("Session not found, please reload your page") {
}