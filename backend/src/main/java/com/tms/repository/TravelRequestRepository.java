package com.tms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tms.model.TravelRequest;
import com.tms.model.enums.TravelStatus;

@Repository
public interface TravelRequestRepository extends JpaRepository<TravelRequest, Long> {

    List<TravelRequest> findByEmployeeIdOrderByCreatedAtDesc(Long employeeId);

    List<TravelRequest> findByStatusOrderByCreatedAtDesc(TravelStatus status);

    List<TravelRequest> findByStatusInOrderByCreatedAtDesc(List<TravelStatus> statuses);

    @Query("SELECT tr FROM TravelRequest tr WHERE tr.employee.department.id = :deptId ORDER BY tr.createdAt DESC")
    List<TravelRequest> findByDepartmentId(@Param("deptId") Long deptId);

    @Query("SELECT SUM(tr.estimatedBudget) FROM TravelRequest tr " +
           "WHERE tr.employee.department.id = :deptId AND tr.status = 'FINANCE_APPROVED'")
    Double sumBudgetByDepartment(@Param("deptId") Long deptId);

    @Query("SELECT SUM(e.amount) FROM Expense e " +
           "WHERE e.travelRequest.employee.department.id = :deptId")
    Double sumExpensesByDepartment(@Param("deptId") Long deptId);
}