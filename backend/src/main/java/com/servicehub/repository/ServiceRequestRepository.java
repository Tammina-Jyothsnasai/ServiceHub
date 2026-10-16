package com.servicehub.repository;

import com.servicehub.entity.ServiceRequest;
import com.servicehub.enums.Category;
import com.servicehub.enums.Priority;
import com.servicehub.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {

    Optional<ServiceRequest> findByTicketId(String ticketId);

    List<ServiceRequest> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<ServiceRequest> findAllByOrderByCreatedAtDesc();

    long countByStatus(RequestStatus status);

    long countByPriority(Priority priority);

    @Query("SELECT r.category, COUNT(r) FROM ServiceRequest r GROUP BY r.category")
    List<Object[]> countRequestsByCategory();

    @Query("SELECT r.status, COUNT(r) FROM ServiceRequest r GROUP BY r.status")
    List<Object[]> countRequestsByStatus();

    @Query("SELECT r.priority, COUNT(r) FROM ServiceRequest r GROUP BY r.priority")
    List<Object[]> countRequestsByPriority();

    List<ServiceRequest> findByStatus(RequestStatus status);

    List<ServiceRequest> findByCategory(Category category);

    List<ServiceRequest> findByPriority(Priority priority);
}
