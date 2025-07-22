package toyproject.startofconversation.api.image

import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import toyproject.startofconversation.api.image.dto.ImageUploadResponse
import toyproject.startofconversation.common.base.dto.ResponseData

@RestController
@RequestMapping("/api/img")
class ImageController(
    private val imageService: ImageService
) {

    @GetMapping("/public/{domain}/{filename:.+}")
    fun loadImage(
        @PathVariable domain: String, @PathVariable filename: String
    ): ResponseEntity<Resource> = ResponseEntity.ok()
        .contentType(MediaType.IMAGE_JPEG)
        .body(imageService.loadImageAsResource(domain, filename))

    @PostMapping("/{domain}/upload")
    fun uploadImage(
        @PathVariable domain: String,
        @RequestPart("file") file: MultipartFile
    ): ResponseData<ImageUploadResponse> = imageService.uploadImage(domain, file)
}