package com.healthtech.service;

import com.healthtech.entity.Patient;
import com.healthtech.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public Patient savePatient(Patient patient) {
        if (patientRepository.existsByIdentityNumber(patient.getIdentityNumber())) {
            throw new IllegalArgumentException("Bu TC Kimlik Numarası ile kayıtlı bir hasta zaten mevcut.");
        }
        return patientRepository.save(patient);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }
}
