package pro.squadup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.squadup.models.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    Language findByLanguage(String language);
}
