package ru.fisher.VehiclePark.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fisher.VehiclePark.models.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

}
