package toyproject.startofconversation.api.image

import org.springframework.context.annotation.Profile
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

@Profile("prod")
@RestController
@RequestMapping("/api/img")
class ImageController(
    private val imageService: ImageService
) {

    @GetMapping("/public/{domain}/{filename:.+}")
    fun downloadImage(
        @PathVariable domain: String, @PathVariable filename: String
    ): ResponseEntity<ByteArray> = ResponseEntity.ok()
        .contentType(MediaType.IMAGE_JPEG)
        .body(imageService.loadImageAsResource(domain, filename))

    @PostMapping("/upload/{domain}/{id}")
    fun uploadImage(
        @PathVariable domain: String,
        @PathVariable id: String,
        @RequestPart("file") file: MultipartFile
    ): ResponseData<ImageUploadResponse> = imageService.uploadImage(domain, id, file)

}