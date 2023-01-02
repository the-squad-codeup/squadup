package pro.squadup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.squadup.models.Stray;


@Repository
public interface StrayRepository extends JpaRepository <Stray, Long> {

}
