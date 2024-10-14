package com.project.twiliospring.domain

import org.springframework.http.HttpStatus

enum class LoginState {

    AUTHENTICATED {
        override fun getResponseStatus(): HttpStatus {
            return HttpStatus.OK
        }
    },

    F2A_CODE_PENDING {
        override fun getResponseStatus(): HttpStatus {
            return HttpStatus.ACCEPTED
        }
    },

    NOT_AUTHENTICATED {
        override fun getResponseStatus(): HttpStatus {
            return HttpStatus.BAD_REQUEST
        }
    };

    abstract fun getResponseStatus(): HttpStatus
}