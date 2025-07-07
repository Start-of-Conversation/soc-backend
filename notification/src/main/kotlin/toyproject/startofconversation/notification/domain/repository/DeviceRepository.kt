package toyproject.startofconversation.notification.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.notification.domain.entity.Device

@Repository
interface DeviceRepository : JpaRepository<Device, String> {

    fun findByDeviceId(deviceId: String): Device?

    fun findAllByUserId(userId: String): List<Device>

    fun findAllByUser(user: Users): List<Device>
}