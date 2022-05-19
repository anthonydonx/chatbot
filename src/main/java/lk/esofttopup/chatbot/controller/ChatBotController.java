package lk.esofttopup.chatbot.controller;

import lk.esofttopup.chatbot.dto.ChatBotResponse;
import lk.esofttopup.chatbot.dto.UserInputRequest;
import lk.esofttopup.chatbot.entity.CategoryData;
import lk.esofttopup.chatbot.service.ChatbotService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("chatbot/api/v1")
@AllArgsConstructor
public class ChatBotController {
    final ChatbotService chatbotService;

    @CrossOrigin
    @PostMapping("response")
    ResponseEntity<ChatBotResponse> getBotResponse(@RequestBody UserInputRequest userInputRequest) {
        ChatBotResponse chatBotResponse = chatbotService.buildResponse(userInputRequest);
        return ResponseEntity.accepted().body(chatBotResponse);
    }
    @CrossOrigin
    @PostMapping("/knowledge/update")
    ResponseEntity<ChatBotResponse> updateKB(@RequestBody CategoryData categoryData){
        return ResponseEntity.accepted().build();

    }
}