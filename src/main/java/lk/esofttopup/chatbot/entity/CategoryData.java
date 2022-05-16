package lk.esofttopup.chatbot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "category_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryData implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long categoryId;
    private String categoryName;
    @OneToMany(cascade =CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Question> questions = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Answer> answers = new ArrayList<>();

}
