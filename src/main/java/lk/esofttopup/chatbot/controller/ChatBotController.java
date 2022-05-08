package lk.esofttopup.chatbot.controller;

import lk.esofttopup.chatbot.dto.UserInputRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("chatbot/api/v1")
public class ChatBotController {

    @CrossOrigin
    @PostMapping("response")
    ResponseEntity<?> getBotResponse(@RequestBody UserInputRequest userInputRequest){
        return ResponseEntity.accepted().body("response message");
    }
}
