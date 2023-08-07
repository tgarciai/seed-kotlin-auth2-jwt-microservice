package com.cleansoftware.seed.infra.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SecuredController {
    @RestController
    @RequestMapping("/user")
    class SecuredUserController {
        @GetMapping("/bar")
        fun bar(): ResponseEntity<*> {
            val model: MutableMap<Any, Any> = HashMap()
            model["foo"] = "bar"
            return ResponseEntity.ok<Map<Any, Any>>(model)
        }
    }


    @RestController
    @RequestMapping("/admin")
    class SecuredAdminController {
        @GetMapping("/bar")
        fun bar(): ResponseEntity<*> {
            val model: MutableMap<Any, Any> = HashMap()
            model["foo"] = "bar"
            return ResponseEntity.ok<Map<Any, Any>>(model)
        }
    }

}
