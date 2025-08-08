package toyproject.startofconversation.api.collection.dto

import jakarta.validation.constraints.NotBlank

data class CollectionCreateRequest(@field:NotBlank val name: String)