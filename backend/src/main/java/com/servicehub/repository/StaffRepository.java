package com.servicehub.repository;

import com.servicehub.entity.Staff;
import com.servicehub.enums.Department;
import com.servicehub.enums.StaffStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Staff> findByStatus(StaffStatus status);
    List<Staff> findByDepartment(Department department);
}
