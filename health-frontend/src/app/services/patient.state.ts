import { Injectable, signal, computed } from '@angular/core';
import { Patient } from '../models/patient.model';
import { PatientService } from './patient.service';

@Injectable({
  providedIn: 'root'
})
export class PatientStateService {
  // Signals for state management
  private patientsSignal = signal<Patient[]>([]);
  private loadingSignal = signal<boolean>(false);
  private errorSignal = signal<string | null>(null);

  // Computed signals that components can read
  readonly patients = computed(() => this.patientsSignal());
  readonly loading = computed(() => this.loadingSignal());
  readonly error = computed(() => this.errorSignal());
  readonly patientCount = computed(() => this.patientsSignal().length);

  constructor(private patientService: PatientService) {}

  // Actions
  loadPatients(): void {
    this.loadingSignal.set(true);
    this.errorSignal.set(null);
    
    this.patientService.getPatients().subscribe({
      next: (data) => {
        this.patientsSignal.set(data);
        this.loadingSignal.set(false);
      },
      error: (err) => {
        this.errorSignal.set('Hastalar yüklenirken bir hata oluştu.');
        this.loadingSignal.set(false);
        console.error(err);
      }
    });
  }

  addPatient(patient: Patient): void {
    const currentPatients = this.patientsSignal();
    this.patientsSignal.set([...currentPatients, patient]);
  }
}
