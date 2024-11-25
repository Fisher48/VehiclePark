package ru.fisher.VehiclePark.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fisher.VehiclePark.models.Person;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByUsername(String username);

}
