package toyproject.startofconversation.api.image

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import toyproject.startofconversation.api.image.dto.ImageUploadResponse
import toyproject.startofconversation.common.base.dto.ResponseData
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.file.Files

@Service
@Profile("prod")
class ImageService(
    private val s3Client: S3Client
) {

    @Value("\${aws.s3.bucket-name}")
    private lateinit var bucketName: String

    fun uploadImage(
        domain: String, id: String, file: MultipartFile
    ): ResponseData<ImageUploadResponse> = ResponseData.to(
        "Image uploaded successfully",
        ImageUploadResponse("/api/img/${storeFile(file, id, domain)}")
    )

    fun loadImageAsResource(domain: String, filename: String): ByteArray {
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(bucketName)
            .key("$domain/$filename")
            .build()

        val s3Object = s3Client.getObject(getObjectRequest)

        val byteArrayOutputStream = ByteArrayOutputStream()
        s3Object.use { inputStream: InputStream ->
            inputStream.copyTo(byteArrayOutputStream)
        }

        return byteArrayOutputStream.toByteArray()
    }

    private fun storeFile(file: MultipartFile, id: String, domain: String): String {
        val tempFile = Files.createTempFile("upload-", file.originalFilename)
        file.transferTo(tempFile.toFile())

        val extension = file.originalFilename?.substringAfterLast(".", "jpg")
        val s3Key = "$domain/$id.$extension"

        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(s3Key)
            .build()

        s3Client.putObject(putObjectRequest, tempFile)
        Files.delete(tempFile)

        return s3Key
    }
}