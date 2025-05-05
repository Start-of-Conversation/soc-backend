package toyproject.startofconversation.auth.service

import toyproject.startofconversation.auth.controller.dto.OAuthParameter

interface OAuthService {
    fun getParameters(): OAuthParameter
}