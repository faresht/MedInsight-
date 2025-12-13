package com.medinsight.doctor.service;

import com.medinsight.commons.exception.ResourceNotFoundException;
import com.medinsight.doctor.entity.Consultation;
import com.medinsight.doctor.repository.ConsultationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ConsultationService {

    private final ConsultationRepository consultationRepository;

    public Consultation createConsultation(Consultation consultation) {
        return consultationRepository.save(consultation);
    }

    public Consultation getConsultationById(Long id) {
        return consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation", "id", id));
    }

    public Consultation getConsultationByAppointmentId(Long appointmentId) {
        return consultationRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation", "appointmentId", appointmentId));
    }

    public List<Consultation> getConsultationsByPatientId(Long patientId) {
        return consultationRepository.findByPatientId(patientId);
    }

    public List<Consultation> getConsultationsByDoctorId(Long doctorId) {
        return consultationRepository.findByDoctorId(doctorId);
    }

    public List<Consultation> getPatientPortalConsultations(Long patientId) {
        return consultationRepository.findByPatientIdAndVisibleInPortal(patientId, true);
    }

    public Consultation updateConsultation(Long id, Consultation consultationDetails) {
        Consultation consultation = getConsultationById(id);

        consultation.setChiefComplaint(consultationDetails.getChiefComplaint());
        consultation.setDiagnosis(consultationDetails.getDiagnosis());
        consultation.setPrescription(consultationDetails.getPrescription());
        consultation.setNotes(consultationDetails.getNotes());
        consultation.setFollowUpInstructions(consultationDetails.getFollowUpInstructions());
        consultation.setVisibleInPortal(consultationDetails.isVisibleInPortal());

        return consultationRepository.save(consultation);
    }
}
