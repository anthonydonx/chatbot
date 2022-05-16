package lk.esofttopup.chatbot.entity.course;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Table(name = "course")
@Entity
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String duration;
    private String courseFee;
    private String universityFee;
    @OneToMany(cascade = CascadeType.ALL)
    List<CourseContent> contentList = new java.util.ArrayList<>();

}