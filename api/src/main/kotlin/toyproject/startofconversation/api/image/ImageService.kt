package toyproject.startofconversation.api.image

import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import toyproject.startofconversation.api.image.config.ResourceProperties
import toyproject.startofconversation.api.image.dto.ImageUploadResponse
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.exception.SOCNotFoundException
import toyproject.startofconversation.common.exception.SOCUploadException
import toyproject.startofconversation.common.support.UUIDUtil
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Service
class ImageService(
    private val resourceProperties: ResourceProperties
) {

    fun uploadImage(domain: String, file: MultipartFile): ResponseData<ImageUploadResponse> = ResponseData.to(
        "Image uploaded successfully",
        ImageUploadResponse("/api/img/$domain/${storeFile(file, domain)}")
    )

    fun loadImageAsResource(domain: String, filename: String): Resource {
        val resource = UrlResource(getFilePath(domain, filename).toUri())

        if (!resource.exists() || !resource.isReadable) {
            throw SOCNotFoundException("Image not found: $filename")
        }

        return resource
    }

    private fun storeFile(file: MultipartFile, domain: String): String {
        val uploadDir = Paths.get(resourceProperties.img).resolve(domain).normalize()
        Files.createDirectories(uploadDir)

        val filename = generateFilename(file.originalFilename)
        val targetPath = uploadDir.resolve(filename)

        runCatching {
            file.transferTo(targetPath)
        }.getOrElse {
            throw SOCUploadException("파일 저장 실패: ${it.message}")
        }

        return filename
    }

    private fun generateFilename(filename: String?): String = buildString {
        append(UUIDUtil.getRandomUUID())
        filename?.let { append("_${it.replace(" ", "_")}") }
    }

    private fun getFilePath(domain: String, filename: String): Path = Paths.get(resourceProperties.img)
        .resolve(domain)
        .resolve(filename)
        .normalize()

}