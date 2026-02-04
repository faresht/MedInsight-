import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatListModule } from '@angular/material/list';

@Component({
  selector: 'app-reports-page',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatIconModule, MatButtonModule, MatListModule],
  template: `
    <div class="page-container">
       <div class="page-header">
        <h1 class="mat-h1">Medical Reports</h1>
        <button mat-raised-button color="primary">
          <mat-icon>upload</mat-icon> Upload Report
        </button>
      </div>

      <div class="reports-grid">
        <mat-card class="report-card">
          <mat-icon class="pdf-icon">picture_as_pdf</mat-icon>
          <div class="report-info">
             <h3>Blood Work - John Doe</h3>
             <p>Uploaded: Oct 24, 2023</p>
          </div>
          <button mat-icon-button color="primary"><mat-icon>download</mat-icon></button>
        </mat-card>
        
        <mat-card class="report-card">
          <mat-icon class="img-icon">image</mat-icon>
          <div class="report-info">
             <h3>X-Ray Chest - Jane Smith</h3>
             <p>Uploaded: Oct 20, 2023</p>
          </div>
          <button mat-icon-button color="primary"><mat-icon>download</mat-icon></button>
        </mat-card>

         <mat-card class="report-card">
          <mat-icon class="pdf-icon">picture_as_pdf</mat-icon>
          <div class="report-info">
             <h3>MRI Scan Analysis - P003</h3>
             <p>Uploaded: Oct 15, 2023</p>
          </div>
          <button mat-icon-button color="primary"><mat-icon>download</mat-icon></button>
        </mat-card>
      </div>
    </div>
  `,
  styles: [`
    .page-container {
      padding: 32px;
      max-width: 1200px;
      margin: 0 auto;
    }
    .page-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 40px;
    }
    .page-header h1 { font-size: 28px; font-weight: 700; color: #1f2937; }
    
    .reports-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
      gap: 24px;
    }
    
    .report-card {
      display: flex;
      flex-direction: row;
      align-items: center;
      padding: 24px;
      gap: 20px;
      border-radius: 16px;
      background: white;
      box-shadow: 0 4px 15px rgba(0,0,0,0.03);
      border: 1px solid rgba(0,0,0,0.02);
      transition: all 0.3s ease;
      cursor: pointer;
    }
    .report-card:hover {
      transform: translateY(-5px);
      box-shadow: 0 15px 30px rgba(0,0,0,0.08);
      border-color: #b2dfdb;
    }
    
    .report-info { flex: 1; }
    .report-info h3 { 
      margin: 0 0 6px 0; 
      font-size: 16px; 
      font-weight: 600; 
      color: #374151; 
    }
    .report-info p { 
      margin: 0; 
      color: #9ca3af; 
      font-size: 12px; 
    }
    
    .pdf-icon { color: #ef4444; transform: scale(1.8); }
    .img-icon { color: #3b82f6; transform: scale(1.8); }
    
    /* Button inside card */
    button.mat-icon-button {
        color: #009688;
        background: #e0f2f1;
    }
    button.mat-icon-button:hover { background: #b2dfdb; }
  `]
})
export class ReportsPageComponent { }
