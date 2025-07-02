package toyproject.startofconversation.notification.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import toyproject.startofconversation.notification.domain.entity.Device

@Repository
interface DeviceRepository : JpaRepository<Device, String> {
}