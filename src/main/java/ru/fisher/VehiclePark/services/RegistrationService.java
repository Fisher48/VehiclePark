package ru.fisher.VehiclePark.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fisher.VehiclePark.models.Manager;
import ru.fisher.VehiclePark.models.Person;
import ru.fisher.VehiclePark.repositories.ManagerRepository;
import ru.fisher.VehiclePark.repositories.PeopleRepository;

@Service
public class RegistrationService {

    private final PeopleRepository peopleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

//    @Transactional
//    public void register(Manager manager) {
//        manager.setPassword(passwordEncoder.encode(manager.getPassword()));
//        managerRepository.save(manager);
//    }

    @Transactional
    public void register(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        peopleRepository.save(person);
    }
}
