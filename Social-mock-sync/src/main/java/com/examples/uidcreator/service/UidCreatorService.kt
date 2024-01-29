package com.examples.uidcreator.service

import com.examples.uidcreator.controller.ExternalHttpUidRequest
import com.examples.uidcreator.controller.ResponseHttpUidRequest
import com.examples.uidcreator.controller.UidCreatorController
import com.examples.uidcreator.util.JXUtil
import com.examples.uidcreator.util.SecurityUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import java.util.*

@Service
class UidCreatorService {
    private val keys = ArrayList<String>()
    private val requests = ArrayList<ExternalHttpUidRequest>()
    protected val log: Logger = LoggerFactory.getLogger(UidCreatorController::class.java)

    @GetMapping()
    @ResponseBody
    fun getAuthForm(@RequestParam("data") data: String): ResponseHttpUidRequest {

//        var json: String? = null
//        val threads = keys.map {
//            val thread = Thread {
//                try {
//                    json = SecurityUtils.decrypt(it, data)
//                } catch (_: Exception) {
//                    ;
//                }
//            }
//            thread.start()
//            thread
//        }.toList()
//        threads.forEach(Thread::join)

        val json = keys.map {
            try {
                SecurityUtils.decrypt(it, data)
            } catch (e: Exception) {
                null
            }
        }.findLast { it != null }

        val request = try {
            JXUtil.fromJson<ExternalHttpUidRequest>(json, ExternalHttpUidRequest::class.java)
        } catch (e: Exception) {
            log.error("uid generator threw exception: " + e.message)
            throw RuntimeException()
        }

        requests.add(request)
        while (requests.size > 20) {
            requests.removeAt(0)
        }

        var uid: String? = null;
        if (request.email.indexOf("uuid") > -1) {
            uid = UUID.randomUUID().toString()
        } else if (request.email.indexOf("zero") > -1) {
            uid = request.email.substring(0, request.email.indexOf("@"))
        } else if (request.email.indexOf("wait") > -1) {
            Thread.sleep(10000)
        }
        uid = uid ?: request.email.replace(".", "").replace("@", "")
        log.info("generate $uid from $request")

        return ResponseHttpUidRequest(uid, false, false)
    }

    @GetMapping("key")
    @ResponseBody
    fun privateKey(@RequestParam("privateKey") privateKey: String): String {
        keys.add(privateKey)
        while (keys.size > 20) {
            keys.removeAt(0)
        }
        return privateKey
    }

    @GetMapping("requests")
    @ResponseBody
    fun requests() = requests
}
