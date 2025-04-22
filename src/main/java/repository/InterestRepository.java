package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.Interest;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {
}
