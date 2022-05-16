package lk.esofttopup.chatbot.entity.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "course_content")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseContent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //    @ManyToOne
//    @JoinColumn(name = "course_id")
//    private Course course;
    private String module;

    public CourseContent(String module) {
        this.module=module;
    }
}
