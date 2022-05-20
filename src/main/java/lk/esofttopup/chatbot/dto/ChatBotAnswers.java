package lk.esofttopup.chatbot.dto;

import lk.esofttopup.chatbot.common.ResponseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatBotAnswers {
    private ResponseType type;
    private String text;
    private String listType;
    private String inputId;
    private String commonText;
}
