package toyproject.startofconversation.common.base.controller

import toyproject.startofconversation.common.security.SecurityUtil

abstract class BaseController {

    protected fun getUserId(): String = SecurityUtil.getCurrentUserId()

}