package lk.esofttopup.chatbot.entity.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name="faculty")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGen")
    @SequenceGenerator(name = "seqGen", sequenceName = "seq", initialValue = 1)
    private Long id;
    private String name;
    @OneToMany( cascade = CascadeType.ALL)
    private List<Course> courseList = new java.util.ArrayList<>();


    public Faculty(String facultyName) {
        this.name=facultyName;
    }
}
