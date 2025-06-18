package toyproject.startofconversation.api.image

import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/img")
class ImageController(
    private val imageService: ImageService
) {

    @GetMapping("/{domain}/{filename:.+}")
    fun loadImage(
        @PathVariable domain: String, @PathVariable filename: String
    ): ResponseEntity<Resource> = ResponseEntity.ok()
        .contentType(MediaType.IMAGE_JPEG)
        .body(imageService.loadImageAsResource(domain, filename))

}