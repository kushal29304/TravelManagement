package com.tms.dto.response;

public class AuthResponse {
    private String token;
    private String email;
    private String name;
    private String role;
    private Long userId;
    private Long departmentId;
    private String departmentName;

    public AuthResponse() {}

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final AuthResponse r = new AuthResponse();
        public Builder token(String v) { r.token = v; return this; }
        public Builder email(String v) { r.email = v; return this; }
        public Builder name(String v) { r.name = v; return this; }
        public Builder role(String v) { r.role = v; return this; }
        public Builder userId(Long v) { r.userId = v; return this; }
        public Builder departmentId(Long v) { r.departmentId = v; return this; }
        public Builder departmentName(String v) { r.departmentName = v; return this; }
        public AuthResponse build() { return r; }
    }
}