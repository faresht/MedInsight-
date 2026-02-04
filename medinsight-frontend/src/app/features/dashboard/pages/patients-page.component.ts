import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatChipsModule } from '@angular/material/chips';

export interface PatientData {
  id: string;
  name: string;
  age: number;
  gender: string;
  lastVisit: string;
  status: 'Healthy' | 'Risk' | 'Critical';
  insurance: string;
}

const ELEMENT_DATA: PatientData[] = [
  { id: 'P001', name: 'John Doe', age: 45, gender: 'Male', lastVisit: '2023-11-20', status: 'Healthy', insurance: 'BlueCross' },
  { id: 'P002', name: 'Jane Smith', age: 34, gender: 'Female', lastVisit: '2023-11-18', status: 'Risk', insurance: 'Aetna' },
  { id: 'P003', name: 'Robert Johnson', age: 67, gender: 'Male', lastVisit: '2023-11-21', status: 'Critical', insurance: 'Medicare' },
  { id: 'P004', name: 'Emily Davis', age: 29, gender: 'Female', lastVisit: '2023-11-15', status: 'Healthy', insurance: 'BlueCross' },
  { id: 'P005', name: 'Michael Wilson', age: 52, gender: 'Male', lastVisit: '2023-11-10', status: 'Risk', insurance: 'UnitedHealth' },
];

@Component({
  selector: 'app-patients-page',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatIconModule,
    MatButtonModule,
    MatInputModule,
    MatFormFieldModule,
    MatChipsModule
  ],
  template: `
    <div class="page-container">
      <div class="page-header">
        <div>
          <h1 class="mat-h1">Patients Directory</h1>
          <p class="text-secondary">Manage and view all patient records</p>
        </div>
        <button mat-raised-button color="primary">
          <mat-icon>add</mat-icon> Add Patient
        </button>
      </div>

      <div class="filter-section">
        <mat-form-field appearance="outline" class="search-field">
          <mat-label>Search patients...</mat-label>
          <mat-icon matPrefix>search</mat-icon>
          <input matInput (keyup)="applyFilter($event)" placeholder="Ex. John">
        </mat-form-field>
      </div>

      <div class="table-container mat-elevation-z2">
        <table mat-table [dataSource]="dataSource" matSort>

          <!-- ID Column -->
          <ng-container matColumnDef="id">
            <th mat-header-cell *matHeaderCellDef mat-sort-header> ID </th>
            <td mat-cell *matCellDef="let row"> <span class="text-secondary">{{row.id}}</span> </td>
          </ng-container>

          <!-- Name Column -->
          <ng-container matColumnDef="name">
            <th mat-header-cell *matHeaderCellDef mat-sort-header> Name </th>
            <td mat-cell *matCellDef="let row"> 
              <div class="user-cell">
                <div class="avatar-circle">{{row.name.charAt(0)}}</div>
                <div class="user-info">
                  <span class="font-bold">{{row.name}}</span>
                  <span class="text-xs text-secondary">{{row.gender}}, {{row.age}} yrs</span>
                </div>
              </div>
            </td>
          </ng-container>

          <!-- Last Visit Column -->
          <ng-container matColumnDef="lastVisit">
            <th mat-header-cell *matHeaderCellDef mat-sort-header> Last Visit </th>
            <td mat-cell *matCellDef="let row"> {{row.lastVisit}} </td>
          </ng-container>

          <!-- Status Column -->
          <ng-container matColumnDef="status">
            <th mat-header-cell *matHeaderCellDef mat-sort-header> Status </th>
            <td mat-cell *matCellDef="let row">
              <span class="status-chip" [ngClass]="row.status.toLowerCase()">{{row.status}}</span>
            </td>
          </ng-container>
          
           <!-- Insurance Column -->
          <ng-container matColumnDef="insurance">
            <th mat-header-cell *matHeaderCellDef mat-sort-header> Insurance </th>
            <td mat-cell *matCellDef="let row"> {{row.insurance}} </td>
          </ng-container>

          <!-- Actions Column -->
          <ng-container matColumnDef="actions">
            <th mat-header-cell *matHeaderCellDef> Actions </th>
            <td mat-cell *matCellDef="let row">
              <button mat-icon-button color="primary"><mat-icon>edit</mat-icon></button>
              <button mat-icon-button color="warn"><mat-icon>delete</mat-icon></button>
            </td>
          </ng-container>

          <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
          <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>

          <!-- Row shown when there is no matching data. -->
          <tr class="mat-row" *matNoDataRow>
            <td class="mat-cell" colspan="4">No data matching the filter</td>
          </tr>
        </table>

        <mat-paginator [pageSizeOptions]="[5, 10, 25, 100]" aria-label="Select page of users"></mat-paginator>
      </div>
    </div>
  `,
  styles: [`
    :host {
      display: block;
      background-color: #f8f9fa;
      min-height: 100%;
    }
    .page-container {
      padding: 32px;
      max-width: 1400px;
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
      margin: 0;
      letter-spacing: -0.5px;
    }
    
    /* Search & Filter Bar */
    .filter-section {
      margin-bottom: 24px;
      background: white;
      padding: 16px 24px;
      border-radius: 16px;
      display: flex;
      align-items: center;
      box-shadow: 0 4px 20px rgba(0,0,0,0.02);
    }
    .search-field {
      width: 100%;
      max-width: 400px;
      font-size: 14px;
    }
    
    /* Table Styling */
    .table-container {
      background: white;
      border-radius: 20px;
      overflow: hidden;
      box-shadow: 0 10px 40px rgba(0,0,0,0.05); /* Premium Shadow */
      border: 1px solid rgba(0,0,0,0.02);
    }
    table {
      width: 100%;
    }
    th.mat-header-cell {
      background: #f9fafb;
      color: #6b7280;
      font-weight: 600;
      font-size: 12px;
      text-transform: uppercase;
      letter-spacing: 0.5px;
      padding: 20px 24px;
      border-bottom: 1px solid #edf2f7;
    }
    td.mat-cell {
      padding: 16px 24px;
      color: #374151;
      font-size: 14px;
      border-bottom: 1px solid #f3f4f6;
    }
    tr.mat-row:hover {
      background-color: #f8fafc;
    }
    
    /* User Cell Styling */
    .user-cell {
      display: flex;
      align-items: center;
      gap: 16px;
    }
    .avatar-circle {
      width: 42px;
      height: 42px;
      background: linear-gradient(135deg, #009688, #4db6ac);
      color: white;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: 600;
      font-size: 16px;
      box-shadow: 0 4px 10px rgba(0, 150, 136, 0.2);
    }
    .user-info {
      display: flex;
      flex-direction: column;
    }
    .font-bold { font-weight: 600; color: #111827; }
    .text-xs { font-size: 12px; color: #9ca3af; margin-top: 2px; }
    
    /* Status Chips */
    .status-chip {
      padding: 6px 14px;
      border-radius: 20px;
      font-size: 12px;
      font-weight: 600;
      text-transform: uppercase;
      letter-spacing: 0.5px;
    }
    .status-chip.healthy { background: #ecfdf5; color: #059669; }
    .status-chip.risk { background: #fff7ed; color: #ea580c; }
    .status-chip.critical { background: #fef2f2; color: #dc2626; }
    
    /* Text Colors */
    .text-secondary { color: #6b7280; font-weight: 500; }
    
    /* Buttons */
    .mat-mdc-icon-button { opacity: 0.7; }
    .mat-mdc-icon-button:hover { opacity: 1; background-color: #f3f4f6; }
  `]
})
export class PatientsPageComponent {
  displayedColumns: string[] = ['id', 'name', 'lastVisit', 'status', 'insurance', 'actions'];
  dataSource = ELEMENT_DATA;

  applyFilter(event: Event) {
    // Filter logic placeholder
  }
}
