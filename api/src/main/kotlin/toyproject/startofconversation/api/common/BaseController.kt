package toyproject.startofconversation.api.common

import toyproject.startofconversation.auth.support.SecurityUtil

abstract class BaseController {

    protected fun getUserId(): String = SecurityUtil.getCurrentUserId()

}