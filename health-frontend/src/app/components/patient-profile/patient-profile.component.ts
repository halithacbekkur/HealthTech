import { Component, OnInit, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PatientStateService } from '../../services/patient.state';
import { Patient } from '../../models/patient.model';

@Component({
  selector: 'app-patient-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './patient-profile.component.html',
  styleUrls: ['./patient-profile.component.css']
})
export class PatientProfileComponent implements OnInit {
  searchTerm = '';
  selectedPatient: Patient | null = null;

  // Çıkarımlı (Computed) Signal ile Arama İşlemi
  filteredPatients = computed(() => {
    const term = this.searchTerm.toLowerCase();
    const patients = this.patientState.patients();
    
    if (!term) return patients;
    
    return patients.filter(p => 
      p.firstName.toLowerCase().includes(term) ||
      p.lastName.toLowerCase().includes(term) ||
      p.identityNumber.includes(term) ||
      p.phone.includes(term)
    );
  });

  constructor(public patientState: PatientStateService) {}

  ngOnInit(): void {
    if (this.patientState.patients().length === 0) {
      this.patientState.loadPatients();
    }
  }

  viewDetails(patient: Patient): void {
    this.selectedPatient = patient;
  }

  closeDetails(): void {
    this.selectedPatient = null;
  }
}
