package com.examples.uidcreator.controller

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.examples.uidcreator.service.UidCreatorService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/uidcreator")
class UidCreatorController(var uidCreatorService: UidCreatorService) {

    protected val log: Logger = LoggerFactory.getLogger(UidCreatorController::class.java)

    @GetMapping()
    @ResponseBody
    fun getAuthForm(@RequestParam("data") data: String) = uidCreatorService.getAuthForm(data)
    @GetMapping("key")
    @ResponseBody
    fun privateKey(@RequestParam("privateKey") privateKey: String) = uidCreatorService.privateKey(privateKey)

    @GetMapping("requests")
    @ResponseBody
    fun requests() = uidCreatorService.requests()

}


data class ExternalHttpUidRequest @JsonCreator constructor(@JsonProperty("reason") val reason: ExternalHttpUidRequestReason,
                                                           @JsonProperty("email") val email: String,
                                                           @JsonProperty("first_name") val firstName: String?,
                                                           @JsonProperty("last_name") val lastName: String?,
                                                           @JsonProperty("social_linking") val socialLinking: SocialLinkingUIdRequest?) {
}

data class SocialLinkingUIdRequest @JsonCreator constructor(@JsonProperty("type") val type: String,
                                                            @JsonProperty("id") val id: String) {
}

data class ResponseHttpUidRequest @JsonCreator constructor(@JsonProperty("uid") val uid: String,
                                                           @JsonProperty("registration") val registration: Boolean?,
                                                           @JsonProperty("passwordless") val passwordless: Boolean?) {
}

enum class ExternalHttpUidRequestReason {
    SWG, GRANT_ACCESS, CREATE_USER, PASSWORDLESS, FRICTIONLESS
}
