package lk.esofttopup.chatbot.repository;

import lk.esofttopup.chatbot.entity.CategoryData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryData, UUID> {
}