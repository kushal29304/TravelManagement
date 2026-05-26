package com.tms.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.tms.model.Department;
import com.tms.model.User;
import com.tms.model.enums.Role;
import com.tms.repository.DepartmentRepository;
import com.tms.repository.UserRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      DepartmentRepository departmentRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) return;

        Department engineering = departmentRepository.save(
            Department.builder().name("Engineering").budgetLimit(50000.0).allowedClass("ECONOMY").build());
        Department finance = departmentRepository.save(
            Department.builder().name("Finance").budgetLimit(30000.0).allowedClass("BUSINESS").build());
        Department hr = departmentRepository.save(
            Department.builder().name("Human Resources").budgetLimit(20000.0).allowedClass("ECONOMY").build());

        userRepository.save(User.builder()
            .name("Admin User").email("admin@tms.com")
            .passwordHash(passwordEncoder.encode("admin123"))
            .role(Role.ADMIN).department(engineering).build());

        User manager1 = userRepository.save(User.builder()
            .name("Rajesh Kumar").email("manager@tms.com")
            .passwordHash(passwordEncoder.encode("manager123"))
            .role(Role.MANAGER).department(engineering).build());

        userRepository.save(User.builder()
            .name("Priya Shah").email("finance@tms.com")
            .passwordHash(passwordEncoder.encode("finance123"))
            .role(Role.FINANCE).department(finance).build());

        userRepository.save(User.builder()
            .name("Amit Patel").email("employee@tms.com")
            .passwordHash(passwordEncoder.encode("employee123"))
            .role(Role.EMPLOYEE).department(engineering).manager(manager1).build());

        userRepository.save(User.builder()
            .name("Sneha Joshi").email("sneha@tms.com")
            .passwordHash(passwordEncoder.encode("employee123"))
            .role(Role.EMPLOYEE).department(engineering).manager(manager1).build());

        System.out.println("Demo users seeded successfully.");
    }
}