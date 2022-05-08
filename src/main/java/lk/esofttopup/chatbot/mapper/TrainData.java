package lk.esofttopup.chatbot.mapper;

import lk.esofttopup.chatbot.common.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * {
 *    "category":"GREETING",
 *    "questions":[
 *       "Hi",
 *       "Hello",
 *       "Are you there?"
 *    ],
 *    "answers":[
 *       "How can i help you",
 *       "Need any help?"
 *    ]
 * }
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainData {
    private String category;
    private List<String> questions; // list of questions specific category eg: Hi,Hello,etc
    private List<String> answers; // list of answers , we will able to pick random eg: Hi,how can i help you


}
