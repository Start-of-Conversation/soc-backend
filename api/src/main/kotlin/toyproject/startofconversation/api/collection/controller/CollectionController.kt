package toyproject.startofconversation.api.collection.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import toyproject.startofconversation.api.collection.dto.CollectionCreateRequest
import toyproject.startofconversation.api.collection.dto.CollectionListResponse
import toyproject.startofconversation.api.collection.service.CollectionService
import toyproject.startofconversation.common.base.controller.BaseController
import toyproject.startofconversation.common.base.dto.ResponseData

@RestController
@RequestMapping("/api/collection")
class CollectionController(
    private val collectionService: CollectionService
) : BaseController() {

    @GetMapping
    fun getCollectionList(): ResponseData<List<CollectionListResponse>> =
        collectionService.findCollections(getUserId())

    @PostMapping
    fun createCollection(
        @RequestBody request: CollectionCreateRequest
    ): ResponseData<List<CollectionListResponse>> =
        collectionService.saveAndFindCollections(getUserId(), request)

}