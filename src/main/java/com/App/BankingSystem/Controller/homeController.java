package com.App.BankingSystem.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class homeController {

@GetMapping("/hi")
public String get(){
    return "hollla amigos";
}

}
