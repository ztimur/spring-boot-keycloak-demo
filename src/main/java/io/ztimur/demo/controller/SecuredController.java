package io.ztimur.demo.controller;

import io.ztimur.demo.util.SecurityContextUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/secured")
public class SecuredController {
    @GetMapping(path = "/echo/{id}")
    public ResponseEntity<?> echo(@PathVariable("id") String id) {
        return ResponseEntity.ok(String.format("id=%s, username=%s", id, SecurityContextUtil.getCurrentUsername()));
    }
}
