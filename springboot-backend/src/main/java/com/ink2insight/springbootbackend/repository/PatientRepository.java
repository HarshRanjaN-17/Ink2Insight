package com.ink2insight.springbootbackend.repository;

import com.ink2insight.springbootbackend.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
