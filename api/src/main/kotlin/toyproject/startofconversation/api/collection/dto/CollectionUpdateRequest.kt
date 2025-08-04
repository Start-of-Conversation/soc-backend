package toyproject.startofconversation.api.collection.dto

import jakarta.validation.constraints.NotBlank

data class CollectionUpdateRequest(@field:NotBlank val newName: String)
