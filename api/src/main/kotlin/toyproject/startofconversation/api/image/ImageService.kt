package toyproject.startofconversation.api.image

import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import toyproject.startofconversation.api.image.config.ResourceProperties
import toyproject.startofconversation.common.exception.SOCNotFoundException
import java.nio.file.Path
import java.nio.file.Paths

@Service
class ImageService(
    private val resourceProperties: ResourceProperties
) {

    fun loadImageAsResource(domain: String, filename: String): Resource {
        val resource = UrlResource(getFilePath(domain, filename).toUri())

        if (!resource.exists() || !resource.isReadable) {
            throw SOCNotFoundException("Image not found: $filename")
        }

        return resource
    }

    private fun getFilePath(domain: String, filename: String): Path = Paths.get(resourceProperties.img)
        .resolve(domain)
        .resolve(filename)
        .normalize()

}