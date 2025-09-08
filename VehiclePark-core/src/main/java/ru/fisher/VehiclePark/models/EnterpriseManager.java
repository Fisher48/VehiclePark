package ru.fisher.VehiclePark.models;

import jakarta.persistence.*;

@Entity
@Table(schema = "autopark", name = "enterprise_manager")
public class EnterpriseManager {

    @Id
    @ManyToOne
    @JoinColumn(name = "enterprise_id")
    private Enterprise enterprise;


    @Id
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

}
