package com.project.twiliospring.repository.impl

import com.project.twiliospring.domain.F2ARecord
import com.project.twiliospring.domain.User
import com.project.twiliospring.repository.F2ARepository
import org.springframework.stereotype.Component

@Component
class F2ARepositoryImpl : F2ARepository {
    val storage = LinkedHashMap<User, F2ARecord>()
    override fun getRecord(user: User): F2ARecord? {
        return storage.get(user);
    }

    override fun setRecord(user: User, record: F2ARecord) {
        storage.put(user, record);
    }

    override fun deleteRecord(user: User) {
        storage.remove(user);
    }


}