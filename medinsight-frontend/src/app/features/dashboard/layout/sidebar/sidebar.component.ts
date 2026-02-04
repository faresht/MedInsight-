import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule, MatListModule, MatIconModule],
  template: `
    <div class="logo-container">
      <div class="logo-icon-bg">
        <mat-icon class="logo-icon">local_hospital</mat-icon>
      </div>
      <span class="logo-text">MedInsight<span class="plus">+</span></span>
    </div>
    <mat-nav-list class="nav-list">
      <h3 matSubheader class="nav-subheader">MAIN</h3>
      <a mat-list-item routerLink="/dashboard" routerLinkActive="active-link" [routerLinkActiveOptions]="{exact: true}">
        <mat-icon matListItemIcon>dashboard</mat-icon>
        <span matListItemTitle>Dashboard</span>
      </a>
      <a mat-list-item routerLink="/dashboard/diagnosis" routerLinkActive="active-link" *ngIf="isDoctor">
        <mat-icon matListItemIcon>medical_services</mat-icon>
        <span matListItemTitle>AI Diagnosis</span>
      </a>
      
      <h3 matSubheader class="nav-subheader" *ngIf="isDoctor || isAdmin">MANAGEMENT</h3>
      <a mat-list-item routerLink="/dashboard/patients" routerLinkActive="active-link" *ngIf="isDoctor || isAdmin">
        <mat-icon matListItemIcon>people</mat-icon>
        <span matListItemTitle>Patients</span>
      </a>
      <a mat-list-item routerLink="/dashboard/appointments" routerLinkActive="active-link">
        <mat-icon matListItemIcon>calendar_today</mat-icon>
        <span matListItemTitle>Appointments</span>
      </a>

      <h3 matSubheader class="nav-subheader">ANALYTICS</h3>
      <a mat-list-item routerLink="/dashboard/reports" routerLinkActive="active-link">
        <mat-icon matListItemIcon>assessment</mat-icon>
        <span matListItemTitle>Reports</span>
      </a>
      <a mat-list-item routerLink="/dashboard/admin" routerLinkActive="active-link" *ngIf="isAdmin">
        <mat-icon matListItemIcon>admin_panel_settings</mat-icon>
        <span matListItemTitle>Admin</span>
      </a>
    </mat-nav-list>
  `,
  styles: [`
    :host {
      display: block;
      height: 100%;
      background: white;
      border-right: 1px solid rgba(0,0,0,0.05);
    }
    .logo-container {
      height: 64px;
      display: flex;
      align-items: center;
      padding: 0 24px;
      background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
      border-bottom: 1px solid rgba(0,0,0,0.05);
    }
    .logo-icon-bg {
      width: 32px;
      height: 32px;
      background: linear-gradient(135deg, #009688, #4db6ac);
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 12px;
      box-shadow: 0 4px 10px rgba(0, 150, 136, 0.2);
    }
    .logo-icon { color: white; font-size: 20px; width: 20px; height: 20px; }
    .logo-text { font-size: 20px; font-weight: 700; color: #37474f; letter-spacing: -0.5px; }
    .plus { color: #009688; }
    
    .nav-list { padding-top: 24px; }
    .nav-subheader { 
        font-size: 11px; 
        font-weight: 700; 
        color: #90a4ae; 
        letter-spacing: 1px; 
        padding: 0 24px 8px;
        margin-top: 24px;
    }
    
    .mat-mdc-list-item { 
        margin: 4px 12px; 
        border-radius: 8px; 
        color: #546e7a;
    }
    .mat-mdc-list-item:hover { background: #f1f8e9; color: #00695c; }
    .active-link { 
        background: #e0f2f1 !important; 
        color: #00897b !important;
        font-weight: 500;
    }
    .active-link mat-icon { color: #00897b; }
  `]
})
export class SidebarComponent {
  isDoctor = false;
  isAdmin = false;

  constructor(private keycloak: KeycloakService) {
    this.isDoctor = this.keycloak.isUserInRole('MEDECIN');
    this.isAdmin = this.keycloak.isUserInRole('ADMIN');
  }
}
