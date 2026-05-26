-- V1__create_sla_tables.sql
-- SLA Policies Table
CREATE TABLE sla_policies (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    priority_level VARCHAR(10) NOT NULL,
    sla_type VARCHAR(20) NOT NULL,
    sla_minutes INTEGER NOT NULL,
    include_weekends BOOLEAN NOT NULL DEFAULT TRUE,
    include_business_hours_only BOOLEAN NOT NULL DEFAULT FALSE,
    business_hours_start VARCHAR(5) NOT NULL DEFAULT '09:00',
    business_hours_end VARCHAR(5) NOT NULL DEFAULT '18:00',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_project_priority_type UNIQUE(project_id, priority_level, sla_type)
);

CREATE INDEX idx_sla_policies_project_id ON sla_policies(project_id);
CREATE INDEX idx_sla_policies_priority_type ON sla_policies(priority_level, sla_type);
CREATE INDEX idx_sla_policies_is_active ON sla_policies(is_active);

-- SLA Tracking Table
CREATE TABLE sla_tracking (
    id BIGSERIAL PRIMARY KEY,
    ticket_id BIGINT NOT NULL REFERENCES tickets(id) ON DELETE CASCADE,
    sla_type VARCHAR(20) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    remaining_minutes BIGINT,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    is_breached BOOLEAN NOT NULL DEFAULT FALSE,
    breach_timestamp TIMESTAMP,
    paused_at TIMESTAMP,
    total_paused_duration_minutes BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_ticket_sla_type UNIQUE(ticket_id, sla_type)
);

CREATE INDEX idx_sla_tracking_ticket_id ON sla_tracking(ticket_id);
CREATE INDEX idx_sla_tracking_sla_type ON sla_tracking(sla_type);
CREATE INDEX idx_sla_tracking_status ON sla_tracking(status);
CREATE INDEX idx_sla_tracking_end_time ON sla_tracking(end_time);
CREATE INDEX idx_sla_tracking_is_breached ON sla_tracking(is_breached);

-- SLA Breaches Table
CREATE TABLE sla_breaches (
    id BIGSERIAL PRIMARY KEY,
    ticket_id BIGINT NOT NULL REFERENCES tickets(id) ON DELETE CASCADE,
    sla_type VARCHAR(20) NOT NULL,
    breach_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expected_time TIMESTAMP,
    breach_minutes BIGINT NOT NULL,
    is_acknowledged BOOLEAN NOT NULL DEFAULT FALSE,
    acknowledged_by BIGINT REFERENCES users(id) ON DELETE SET NULL,
    acknowledged_at TIMESTAMP,
    acknowledgment_notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_sla_breaches_ticket_id ON sla_breaches(ticket_id);
CREATE INDEX idx_sla_breaches_sla_type ON sla_breaches(sla_type);
CREATE INDEX idx_sla_breaches_breach_timestamp ON sla_breaches(breach_timestamp);
CREATE INDEX idx_sla_breaches_is_acknowledged ON sla_breaches(is_acknowledged);

-- Audit Logs Table
CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    ticket_id BIGINT NOT NULL REFERENCES tickets(id) ON DELETE CASCADE,
    action VARCHAR(100) NOT NULL,
    description TEXT,
    performed_by BIGINT REFERENCES users(id) ON DELETE SET NULL,
    entity_type VARCHAR(50),
    entity_id BIGINT,
    old_value TEXT,
    new_value TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_audit_logs_ticket_id ON audit_logs(ticket_id);
CREATE INDEX idx_audit_logs_action ON audit_logs(action);
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at);
CREATE INDEX idx_audit_logs_entity ON audit_logs(entity_type, entity_id);
