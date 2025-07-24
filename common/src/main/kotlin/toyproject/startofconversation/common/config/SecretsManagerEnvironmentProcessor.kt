package toyproject.startofconversation.common.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.boot.SpringApplication
import org.springframework.boot.env.EnvironmentPostProcessor
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.stereotype.Component
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest

@Component
class SecretsManagerEnvironmentProcessor : EnvironmentPostProcessor {

    override fun postProcessEnvironment(environment: ConfigurableEnvironment, application: SpringApplication) {
        if (!environment.activeProfiles.contains("prod")) {
            return
        }

        val secretName = environment.getProperty("aws.secrets-manager.secret-name")
            ?: throw IllegalStateException("Secret name not configured")
        val regionName = environment.getProperty("aws.secrets-manager.region") ?: "ap-northeast-2"

        val client = SecretsManagerClient.builder()
            .region(Region.of(regionName))
            .build()

        val request = GetSecretValueRequest.builder()
            .secretId(secretName)
            .build()

        val secretJson = client.getSecretValue(request).secretString()

        val mapper = jacksonObjectMapper()
        val secretMap: Map<String, String> = mapper.readValue(secretJson)

        val username = secretMap["username"]
        val password = secretMap["password"]

        environment.systemProperties["spring.datasource.username"] = username
        environment.systemProperties["spring.datasource.password"] = password
    }

}