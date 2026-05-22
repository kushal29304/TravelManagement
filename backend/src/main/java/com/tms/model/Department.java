package com.tms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "budget_limit")
    private Double budgetLimit;

    @Column(name = "allowed_class")
    private String allowedClass;

    public Department() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getBudgetLimit() { return budgetLimit; }
    public void setBudgetLimit(Double budgetLimit) { this.budgetLimit = budgetLimit; }
    public String getAllowedClass() { return allowedClass; }
    public void setAllowedClass(String allowedClass) { this.allowedClass = allowedClass; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final Department d = new Department();
        public Builder name(String v) { d.name = v; return this; }
        public Builder budgetLimit(Double v) { d.budgetLimit = v; return this; }
        public Builder allowedClass(String v) { d.allowedClass = v; return this; }
        public Department build() { return d; }
    }
}