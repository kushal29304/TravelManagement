package com.tms.service;

import org.springframework.stereotype.Service;

import com.tms.model.User;

@Service
public class PolicyService {

    public PolicyResult check(User employee, Double estimatedBudget) {
        if (employee.getDepartment() == null) {
            return new PolicyResult(false, null);
        }
        Double limit = employee.getDepartment().getBudgetLimit();
        if (limit != null && estimatedBudget > limit) {
            return new PolicyResult(true,
                    String.format("Estimated budget %.2f exceeds department limit of %.2f",
                            estimatedBudget, limit));
        }
        return new PolicyResult(false, null);
    }

    public static class PolicyResult {
        private final boolean violated;
        private final String reason;

        public PolicyResult(boolean violated, String reason) {
            this.violated = violated;
            this.reason = reason;
        }

        public boolean isViolated() { return violated; }
        public String getReason() { return reason; }
    }
}