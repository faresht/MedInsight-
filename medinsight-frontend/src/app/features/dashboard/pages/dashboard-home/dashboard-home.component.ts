import { Component, OnInit, inject } from '@angular/core';
import { PatientService } from '../../../../core/services/patient.service';
import { Patient } from '../../../../core/models/patient.model';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-dashboard-home',
  template: `
    <div class="page-container dashboard-container">
      <header class="dashboard-header mb-4">
        <div>
          <h1 class="mat-h1 m-0">Welcome back, {{ firstName }}</h1>
          <p class="text-secondary">Here's what's happening in your clinic today.</p>
        </div>
        <button mat-flat-button color="primary" class="header-action-btn">
          <mat-icon>add</mat-icon> New Patient
        </button>
      </header>
      
      <!-- Stats Cards -->
      <div class="stats-grid">
        <mat-card class="stat-card primary-card">
          <div class="stat-content">
            <div class="stat-value">{{ totalPatients }}</div>
            <div class="stat-label">Total Patients</div>
          </div>
          <mat-icon class="stat-icon">people</mat-icon>
        </mat-card>

        <mat-card class="stat-card success-card">
          <div class="stat-content">
            <div class="stat-value">8</div>
            <div class="stat-label">Appointments Today</div>
          </div>
          <mat-icon class="stat-icon">calendar_today</mat-icon>
        </mat-card>

        <mat-card class="stat-card warn-card">
          <div class="stat-content">
            <div class="stat-value">3</div>
            <div class="stat-label">Critical Alerts</div>
          </div>
          <mat-icon class="stat-icon">warning</mat-icon>
        </mat-card>

        <mat-card class="stat-card info-card">
          <div class="stat-content">
            <div class="stat-value">12</div>
            <div class="stat-label">Pending Reports</div>
          </div>
          <mat-icon class="stat-icon">assignment</mat-icon>
        </mat-card>
      </div>

      <!-- Main Content Area -->
      <div class="dashboard-content">
        <!-- Left Column -->
        <div class="section-left">
          
          <!-- Quick Diagnosis Action -->
          <mat-card class="action-card mb-4">
            <div class="action-card-content">
              <div class="action-text">
                <h3>AI Breast Cancer Diagnosis</h3>
                <p>Start a new multimodal analysis using the latest Agentic AI system.</p>
              </div>
              <button mat-raised-button color="accent" routerLink="/dashboard/diagnosis">
                <mat-icon>medical_services</mat-icon> START DIAGNOSIS
              </button>
            </div>
          </mat-card>

          <mat-card class="card-dashboard">
            <mat-card-header>
              <mat-card-title>Upcoming Appointments</mat-card-title>
              <button mat-icon-button class="more-btn"><mat-icon>more_horiz</mat-icon></button>
            </mat-card-header>
            <mat-card-content>
              <mat-list>
                <mat-list-item class="appointment-item">
                  <div matListItemIcon class="avatar-placeholder bg-blue">JD</div>
                  <div matListItemTitle>John Doe</div>
                  <div matListItemLine>09:00 AM - General Checkup</div>
                  <div matListItemMeta><span class="status-badge status-confirmed">Confirmed</span></div>
                </mat-list-item>
                <mat-divider></mat-divider>
                <mat-list-item class="appointment-item">
                  <div matListItemIcon class="avatar-placeholder bg-purple">JS</div>
                  <div matListItemTitle>Jane Smith</div>
                  <div matListItemLine>10:30 AM - Follow-up</div>
                   <div matListItemMeta><span class="status-badge status-pending">Pending</span></div>
                </mat-list-item>
                <mat-divider></mat-divider>
                <mat-list-item class="appointment-item">
                  <div matListItemIcon class="avatar-placeholder bg-green">RJ</div>
                  <div matListItemTitle>Robert Johnson</div>
                  <div matListItemLine>02:00 PM - Cardiology</div>
                   <div matListItemMeta><span class="status-badge status-confirmed">Confirmed</span></div>
                </mat-list-item>
              </mat-list>
            </mat-card-content>
            <mat-card-actions align="end">
              <button mat-button color="primary">VIEW ALL APPOINTMENTS</button>
            </mat-card-actions>
          </mat-card>
        </div>

        <!-- Right Column -->
        <div class="section-right">
          <mat-card class="card-dashboard">
            <mat-card-header class="mb-2">
              <mat-card-title>Quick Actions</mat-card-title>
            </mat-card-header>
            <mat-card-content class="actions-grid">
              <button mat-stroked-button class="quick-action-btn">
                <mat-icon class="text-primary">person_add</mat-icon> <span>New Patient</span>
              </button>
               <button mat-stroked-button class="quick-action-btn">
                <mat-icon class="text-success">event</mat-icon> <span>Schedule Visit</span>
              </button>
               <button mat-stroked-button class="quick-action-btn">
                <mat-icon class="text-warn">emergency</mat-icon> <span>Emergency</span>
              </button>
            </mat-card-content>
          </mat-card>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .dashboard-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    .text-secondary { color: var(--text-secondary); }
    
    /* Stats Cards */
    .stats-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
      gap: 24px;
      margin-bottom: 32px;
    }
    .stat-card {
      padding: 24px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      position: relative;
      overflow: hidden;
    }
    .stat-content { z-index: 1; }
    .stat-value {
      font-size: 32px;
      font-weight: 700;
      line-height: 1.2;
      margin-bottom: 4px;
    }
    .stat-label {
      font-size: 14px;
      font-weight: 500;
      opacity: 0.8;
    }
    .stat-icon {
      transform: scale(2.5);
      opacity: 0.2;
      position: absolute;
      right: 20px;
      bottom: 20px;
    }
    
    /* Card Variants */
    .primary-card { background: linear-gradient(135deg, #009688, #4db6ac); color: white; }
    .primary-card .stat-icon { color: white; }
    
    .success-card { background: linear-gradient(135deg, #43a047, #66bb6a); color: white; }
    .success-card .stat-icon { color: white; }
    
    .warn-card { background: linear-gradient(135deg, #e53935, #ef5350); color: white; }
    .warn-card .stat-icon { color: white; }
    
    .info-card { background: linear-gradient(135deg, #1e88e5, #42a5f5); color: white; }
    .info-card .stat-icon { color: white; }

    /* Action Card */
    .action-card {
      background: linear-gradient(135deg, #263238, #37474f);
      color: white;
    }
    .action-card-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 16px;
    }
    .action-text h3 { color: white; margin: 0 0 4px 0; }
    .action-text p { margin: 0; opacity: 0.8; font-size: 14px; }
    
    /* Layout */
    .dashboard-content {
      display: grid;
      grid-template-columns: 2fr 1fr;
      gap: 24px;
    }
    .appointment-item {
      padding: 12px 0;
    }
    .avatar-placeholder {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      color: white;
      font-weight: 600;
      font-size: 14px;
      margin-right: 16px;
    }
    .bg-blue { background-color: #1e88e5; }
    .bg-purple { background-color: #8e24aa; }
    .bg-green { background-color: #43a047; }
    
    .status-badge {
      padding: 4px 12px;
      border-radius: 16px;
      font-size: 12px;
      font-weight: 500;
    }
    .status-confirmed { background-color: #e8f5e9; color: #2e7d32; }
    .status-pending { background-color: #fff3e0; color: #ef6c00; }
    
    /* Quick Actions */
    .actions-grid {
      display: flex;
      flex-direction: column;
      gap: 12px;
    }
    .quick-action-btn {
      height: 56px;
      justify-content: flex-start;
      font-size: 16px;
      border: 1px solid #eceff1;
    }
    .quick-action-btn mat-icon { margin-right: 16px; }
    .text-primary { color: var(--primary-color); }
    .text-success { color: #43a047; }
    .text-warn { color: #e53935; }

    @media (max-width: 960px) {
      .dashboard-content { grid-template-columns: 1fr; }
      .action-card-content { flex-direction: column; align-items: flex-start; gap: 16px; }
    }
  `]
})
export class DashboardHomeComponent implements OnInit {
  private patientService = inject(PatientService);
  private keycloak = inject(KeycloakService);

  totalPatients = 0;
  userName = 'User';
  firstName = '';

  async ngOnInit() {
    // Load User Profile
    if (await this.keycloak.isLoggedIn()) {
      try {
        const profile = await this.keycloak.loadUserProfile();
        this.firstName = profile.firstName || 'User';
        this.userName = `${profile.firstName} ${profile.lastName}`;
      } catch (e) {
        console.error('Failed to load profile', e);
      }
    }

    // Load Data
    this.patientService.getPatients().subscribe({
      next: (patients: Patient[]) => {
        this.totalPatients = patients.length;
      },
      error: (err: any) => {
        console.error('Error fetching patients', err);
      }
    });
  }
}
