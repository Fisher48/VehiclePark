package ru.fisher.VehiclePark.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.fisher.VehiclePark.exceptions.AccessDeniedException;
import ru.fisher.VehiclePark.models.Manager;
import ru.fisher.VehiclePark.security.ManagerDetails;

@Service
public class AuthContextService {

    private final EnterpriseService enterpriseService;

    @Autowired
    public AuthContextService(EnterpriseService enterpriseService) {
        this.enterpriseService = enterpriseService;
    }

    // Получаем текущего менеджера
    public Manager getCurrentManager() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ManagerDetails managerDetails = (ManagerDetails) authentication.getPrincipal();
        return managerDetails.getManager();
    }

    public Long getCurrentManagerId() {
        return getCurrentManager().getId();
    }

    public void validateManagerAccessToEnterprise(Long enterpriseId) {
        Long managerId = getCurrentManagerId();
        if (!enterpriseService.isEnterpriseManagedByManager(enterpriseId, managerId)) {
            throw new AccessDeniedException("Доступ запрещен");
        }
    }

    // Проверяем, соответствует ли id менеджера
    public void checkManager(Long id) {
        if (!getCurrentManagerId().equals(id)) {
            throw new AccessDeniedException("Доступ запрещен");
        }
    }
}
