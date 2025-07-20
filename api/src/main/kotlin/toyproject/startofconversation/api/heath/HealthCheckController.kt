package toyproject.startofconversation.api.heath

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController {

    @RequestMapping("/health")
    fun healthCheck(): ResponseEntity<String> {
        return ResponseEntity.ok("OK");
    }

}