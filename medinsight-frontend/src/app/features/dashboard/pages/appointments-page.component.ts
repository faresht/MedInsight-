import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTabsModule } from '@angular/material/tabs';

@Component({
  selector: 'app-appointments-page',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatIconModule, MatButtonModule, MatTabsModule],
  template: `
    <div class="page-container">
      <div class="page-header">
        <h1 class="mat-h1">Appointments</h1>
        <button mat-raised-button color="primary">
          <mat-icon>add</mat-icon> New Appointment
        </button>
      </div>

      <mat-tab-group animationDuration="0ms" class="tabs-container">
        <mat-tab label="Upcoming">
          <div class="appointments-list">
             <!-- Card 1 -->
            <mat-card class="appointment-card">
              <div class="time-col">
                <span class="time">09:00 AM</span>
                <span class="duration">30 min</span>
              </div>
              <div class="details-col">
                <h3>John Doe</h3>
                <p>General Checkup • Dr. House</p>
              </div>
              <div class="status-col">
                <span class="status-badge confirm">Confirmed</span>
              </div>
              <div class="actions-col">
                 <button mat-icon-button><mat-icon>more_vert</mat-icon></button>
              </div>
            </mat-card>

            <!-- Card 2 -->
             <mat-card class="appointment-card">
              <div class="time-col">
                <span class="time">10:30 AM</span>
                <span class="duration">45 min</span>
              </div>
              <div class="details-col">
                <h3>Jane Smith</h3>
                <p>Follow-up Visit • Dr. House</p>
              </div>
              <div class="status-col">
                <span class="status-badge pending">Pending</span>
              </div>
              <div class="actions-col">
                 <button mat-icon-button><mat-icon>more_vert</mat-icon></button>
              </div>
            </mat-card>

          </div>
        </mat-tab>
        <mat-tab label="Past">
          <div class="p-4 text-secondary">No past appointments found in this view.</div>
        </mat-tab>
      </mat-tab-group>
    </div>
  `,
  styles: [`
    .page-container {
      padding: 32px;
      max-width: 1000px;
      margin: 0 auto;
    }
    .page-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 32px;
    }
    .page-header h1 {
      font-size: 28px;
      font-weight: 700;
      color: #1f2937;
      letter-spacing: -0.5px;
    }

    .appointments-list {
      display: flex;
      flex-direction: column;
      gap: 20px;
    }
    
    .appointment-card {
      display: flex;
      flex-direction: row;
      align-items: center;
      padding: 24px;
      gap: 32px;
      border-radius: 16px;
      box-shadow: 0 4px 20px rgba(0,0,0,0.03);
      border: 1px solid rgba(0,0,0,0.02);
      transition: transform 0.2s, box-shadow 0.2s;
    }
    .appointment-card:hover {
      transform: translateY(-2px);
      box-shadow: 0 10px 30px rgba(0,0,0,0.06);
    }
    
    .time-col {
      display: flex;
      flex-direction: column;
      align-items: center;
      min-width: 100px;
      padding-right: 24px;
      border-right: 1px solid #f0f0f0;
    }
    .time { 
      font-weight: 700; 
      font-size: 20px; 
      color: #009688; 
    }
    .duration { 
      font-size: 13px; 
      color: #9ca3af; 
      font-weight: 500; 
      margin-top: 4px; 
    }
    
    .details-col { flex: 1; }
    .details-col h3 { 
      margin: 0 0 6px 0; 
      font-weight: 600; 
      font-size: 18px; 
      color: #111827; 
    }
    .details-col p { 
      margin: 0; 
      color: #6b7280; 
      font-size: 14px; 
    }

    .status-badge {
      padding: 6px 16px;
      border-radius: 20px;
      font-size: 12px;
      font-weight: 600;
      letter-spacing: 0.5px;
      text-transform: uppercase;
    }
    .confirm { background: #d1fae5; color: #059669; }
    .pending { background: #ffedd5; color: #ea580c; }
  `]
})
export class AppointmentsPageComponent { }
