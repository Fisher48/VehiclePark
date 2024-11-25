package ru.fisher.VehiclePark.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.fisher.VehiclePark.models.Manager;
import ru.fisher.VehiclePark.repositories.ManagerRepository;
import ru.fisher.VehiclePark.security.ManagerDetails;

import java.util.Optional;

@Service
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

        return new ManagerDetails(manager.get());
    }

}
