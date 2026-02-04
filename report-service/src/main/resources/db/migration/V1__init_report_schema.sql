-- V1__init_report_schema.sql
-- Initial schema for Report Service

CREATE TABLE IF NOT EXISTS reports (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    report_type VARCHAR(50) NOT NULL,
    visible_to_patient BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_reports_patient_id ON reports(patient_id);
CREATE INDEX idx_reports_doctor_id ON reports(doctor_id);
CREATE INDEX idx_reports_type ON reports(report_type);

COMMENT ON TABLE reports IS 'Stores medical reports and documents';
