package toyproject.startofconversation.api.image.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client

@Profile("prod")
@Configuration
class S3Config {

    @Value("\${cloud.aws.region.static}")
    private lateinit var region: String

    @Bean
    fun s3Client(): S3Client = S3Client.builder()
        .region(Region.of(region))
        .build()

}