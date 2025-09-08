package ru.fisher.VehiclePark.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.fisher.VehiclePark.models.Manager;
import ru.fisher.VehiclePark.repositories.jpa.ManagerRepository;
import ru.fisher.VehiclePark.security.ManagerDetails;

import java.util.Optional;

@Service
@Slf4j
public class ManagerDetailsService implements UserDetailsService {

    private final ManagerRepository managerRepository;

    @Autowired
    public ManagerDetailsService(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Manager> manager = managerRepository.findByUsername(username);

        if (manager.isEmpty()) {
            throw new UsernameNotFoundException("Manager not found");
        }

        log.info("User found: {}", manager.get().getUsername());
        log.info("User roles: {}", manager.get().getRole());

        return new ManagerDetails(manager.get());
    }

}
