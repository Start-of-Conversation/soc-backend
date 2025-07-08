package toyproject.startofconversation.notification.device.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.notification.device.domain.entity.Device

@Repository
interface DeviceRepository : JpaRepository<Device, String> {

    @Query("SELECT d FROM Device d JOIN FETCH d.user WHERE d.deviceId = :deviceId")
    fun findByDeviceId(deviceId: String): Device?

    fun findAllByUserId(userId: String): List<Device>

    fun findAllByUser(user: Users): List<Device>
}