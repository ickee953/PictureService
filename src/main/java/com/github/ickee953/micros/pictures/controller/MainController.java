package com.github.ickee953.micros.pictures.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping
public class MainController {

    @GetMapping
    public ModelAndView getMain() {
        return new ModelAndView("index");
    }

}
