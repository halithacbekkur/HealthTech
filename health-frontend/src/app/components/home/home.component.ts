import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { PatientStateService } from '../../services/patient.state';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  
  constructor(public patientState: PatientStateService) {}

  ngOnInit(): void {
    // Sadece ilk açılışta güncel veriyi çek
    this.patientState.loadPatients();
  }
}
