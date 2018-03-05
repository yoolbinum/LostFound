package com.example.demo.controller;

import com.example.demo.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @Autowired
    ItemRepository itemRepository;

    @RequestMapping("/")
    public String home() {
        return "homepage";
    }

}
