package lk.esofttopup.chatbot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping("/")
    @CrossOrigin
    public String showHome() {
        return "chatbot2";
    }
}
