package toyproject.startofconversation.common.domain.device.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import toyproject.startofconversation.common.domain.device.entity.Device

@Repository
interface DeviceRepository : JpaRepository<Device, String> {
}