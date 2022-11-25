package com.easychat.sse.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @ModelAttribute
    public void entrance(String id, Model model) {
        model.addAttribute("name", id);
    }

    @GetMapping("/getName")
    public String getName(@ModelAttribute(value = "name") String name) {
        return name;
    }
}
