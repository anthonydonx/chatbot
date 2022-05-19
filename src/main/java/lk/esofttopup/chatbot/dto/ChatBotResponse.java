package lk.esofttopup.chatbot.dto;

import lk.esofttopup.chatbot.common.ResponseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatBotResponse {
    List<ChatBotAnswers> answers;
}
