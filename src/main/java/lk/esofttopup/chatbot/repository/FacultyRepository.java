package lk.esofttopup.chatbot.repository;

import lk.esofttopup.chatbot.entity.course.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty,Long> {
}
