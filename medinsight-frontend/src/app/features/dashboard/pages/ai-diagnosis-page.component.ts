import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatStepperModule } from '@angular/material/stepper';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { BaseChartDirective } from 'ng2-charts';
import { Chart, registerables, ChartConfiguration, ChartData } from 'chart.js';

Chart.register(...registerables);

@Component({
  selector: 'app-ai-diagnosis-page',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatStepperModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatCheckboxModule,
    BaseChartDirective
  ],
  template: `
    <div class="page-container">
      <div class="header-section text-center mb-5">
        <h1 class="mat-display-1 text-primary">AI Diagnostic Assistant</h1>
        <p class="subtitle">Multimodal Breast Cancer Risk Assessment</p>
      </div>

      <mat-stepper #stepper class="diagnosis-stepper" linear>
        
        <!-- Step 1: Patient Selection -->
        <mat-step label="Patient Select">
          <div class="step-content">
             <h3 class="step-title">Select Patient</h3>
             <mat-form-field appearance="outline" class="w-full">
               <mat-label>Search Patient ID or Name</mat-label>
               <input matInput placeholder="Ex. P12345">
               <mat-icon matSuffix>search</mat-icon>
             </mat-form-field>

             <div class="patient-preview-card p-4 border rounded mt-4" style="background: #f9f9f9">
               <div class="flex items-center gap-4">
                 <div class="avatar bg-teal-500 text-white rounded-full w-12 h-12 flex items-center justify-center font-bold">JS</div>
                 <div>
                   <h4 class="m-0">Jane Smith</h4>
                   <p class="m-0 text-sm text-gray-500">ID: P002 • 34 Years • Female</p>
                 </div>
               </div>
             </div>

             <div class="actions mt-6">
                <button mat-raised-button color="primary" matStepperNext>Next: Clinical Data</button>
             </div>
          </div>
        </mat-step>

        <!-- Step 2: Clinical Data -->
        <mat-step label="Clinical Data">
           <div class="step-content">
              <h3 class="step-title">Clinical Assessment</h3>
              <div class="grid-form">
                <mat-form-field appearance="outline">
                  <mat-label>Age</mat-label>
                  <input matInput type="number" value="34">
                </mat-form-field>
                <mat-form-field appearance="outline">
                  <mat-label>BMI</mat-label>
                  <input matInput type="number" value="24.5">
                </mat-form-field>
                <mat-form-field appearance="outline">
                  <mat-label>Menopause Status</mat-label>
                  <mat-select>
                    <mat-option value="pre">Pre-menopausal</mat-option>
                    <mat-option value="post">Post-menopausal</mat-option>
                  </mat-select>
                </mat-form-field>
              </div>
              <div class="checkbox-group">
                 <mat-checkbox>Family History of Breast Cancer</mat-checkbox>
                 <mat-checkbox>History of Radiation Therapy</mat-checkbox>
                 <mat-checkbox>Hormone Replacement Therapy</mat-checkbox>
              </div>
              <div class="actions mt-6 flex gap-4">
                <button mat-button matStepperPrevious>Back</button>
                <button mat-raised-button color="primary" matStepperNext>Next: Imaging</button>
             </div>
           </div>
        </mat-step>

        <!-- Step 3: Imaging -->
        <mat-step label="Imaging">
           <div class="step-content">
              <h3 class="step-title">Upload Medical Imaging</h3>
              
              <div class="upload-zone">
                <mat-icon class="upload-icon">cloud_upload</mat-icon>
                <p>Drag & Drop Mammography (DICOM/PNG) here</p>
                <button mat-stroked-button color="primary">Browse Files</button>
              </div>

               <div class="actions mt-6 flex gap-4">
                <button mat-button matStepperPrevious>Back</button>
                <button mat-raised-button color="primary" matStepperNext>Next: Genomic</button>
             </div>
           </div>
        </mat-step>

         <!-- Step 3: Genomic -->
        <mat-step label="Genomic">
           <div class="step-content">
              <h3 class="step-title">Genomic Markers</h3>
              <p class="text-secondary mb-4">Enter gene expression levels or upload genetic profile.</p>
              
               <div class="grid-form">
                <mat-form-field appearance="outline">
                  <mat-label>BRCA1 Expression</mat-label>
                  <input matInput type="number">
                </mat-form-field>
                <mat-form-field appearance="outline">
                  <mat-label>BRCA2 Expression</mat-label>
                  <input matInput type="number">
                </mat-form-field>
                 <mat-form-field appearance="outline">
                  <mat-label>TP53 Mutation Status</mat-label>
                  <mat-select>
                    <mat-option value="wildtype">Wildtype</mat-option>
                    <mat-option value="mutant">Mutant</mat-option>
                  </mat-select>
                </mat-form-field>
              </div>

               <div class="actions mt-6 flex gap-4">
                <button mat-button matStepperPrevious>Back</button>
                <button mat-raised-button color="warn" matStepperNext>Analyze Risk</button>
             </div>
           </div>
        </mat-step>


        <!-- Step 4: Results -->
        <mat-step label="Analysis">
           <div class="step-content text-center">
              <!-- Loading State -->
              <div *ngIf="isAnalyzing" class="analysis-placeholder">
                <div class="spinner"></div>
                <h3>Processing Multimodal Data...</h3>
                <p>Agents are analyzing Imaging, Genomic, and Clinical inputs.</p>
              </div>

              <!-- Initial State -->
              <div *ngIf="!isAnalyzing && !analysisComplete" class="analysis-placeholder">
                <mat-icon class="search-icon">analytics</mat-icon>
                <h3>Ready to Analyze</h3>
                <p>Please review all data inputs before running the Agentic AI model.</p>
                <button mat-raised-button color="accent" class="large-btn" (click)="runAnalysis()">
                  <mat-icon>play_arrow</mat-icon> RUN DIAGNOSIS
                </button>
              </div>

              <!-- Results visualization -->
              <div *ngIf="analysisComplete" class="results-container fade-in">
                <div class="result-header">
                    <h2>Diagnosis Complete</h2>
                    <div class="risk-badge" [class]="riskLevel">
                        {{ riskLevel | uppercase }} RISK
                    </div>
                </div>

                <div class="charts-grid">
                    <!-- Risk Score Doughnut -->
                    <div class="chart-card">
                        <h3>Overall Risk Score</h3>
                        <div class="chart-wrapper">
                            <canvas baseChart
                                [data]="riskData"
                                [type]="'doughnut'"
                                [options]="riskOptions">
                            </canvas>
                        </div>
                        <div class="score-display">{{ riskScore }}%</div>
                    </div>

                    <!-- Agent Confidence Bar -->
                    <div class="chart-card">
                        <h3>Agent Confidence Levels</h3>
                        <div class="chart-wrapper">
                            <canvas baseChart
                                [data]="confidenceData"
                                [type]="'bar'"
                                [options]="confidenceOptions">
                            </canvas>
                        </div>
                    </div>
                </div>

                <div class="recommendations">
                    <h3><mat-icon>medical_services</mat-icon> Recommendations</h3>
                    <ul>
                        <li>Schedule follow-up biopsy/MRI.</li>
                        <li>Genetic counseling recommended (BRCA1 mutation detected).</li>
                        <li>Review lifestyle factors (High BMI).</li>
                    </ul>
                </div>

                <button mat-stroked-button color="primary" class="mt-4" (click)="reset()">
                    <mat-icon>refresh</mat-icon> New Analysis
                </button>
              </div>
           </div>
        </mat-step>
      </mat-stepper>
    </div>
  `,
  styles: [`
    .page-container {
      max-width: 1100px;
      margin: 0 auto;
      padding: 40px 24px;
    }
    .header-section {
      text-align: center;
      margin-bottom: 48px;
    }
    .header-section h1 {
      font-size: 36px;
      font-weight: 800;
      color: #1f2937;
      letter-spacing: -1px;
      margin-bottom: 8px;
    }
    .subtitle {
      font-size: 18px;
      color: #6b7280;
      max-width: 600px;
      margin: 0 auto;
    }

    /* Stepper Styling Overrides */
    ::ng-deep .mat-step-header .mat-step-icon-selected,
    ::ng-deep .mat-step-header .mat-step-icon-state-done,
    ::ng-deep .mat-step-header .mat-step-icon-state-edit {
      background-color: #009688 !important; 
      color: white !important;
    }
    
    .diagnosis-stepper {
      background: white;
      border-radius: 24px;
      box-shadow: 0 20px 60px rgba(0,0,0,0.08); /* Deep shadow */
      padding: 32px;
      border: 1px solid rgba(0,0,0,0.02);
    }
    
    .step-content {
      padding: 32px;
      min-height: 350px;
      animation: fadeIn 0.5s ease-out;
    }
    @keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }

    .step-title {
      font-size: 24px;
      font-weight: 700;
      color: #374151;
      margin-bottom: 32px;
      position: relative;
      display: inline-block;
    }
    .step-title::after {
      content: '';
      position: absolute;
      bottom: -8px;
      left: 0;
      width: 40px;
      height: 4px;
      background: #009688;
      border-radius: 2px;
    }

    /* Form Fields */
    .grid-form {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
      gap: 24px;
    }
    
    /* Upload Zone */
    .upload-zone {
      border: 2px dashed #b2dfdb;
      background: #f0faff;
      border-radius: 20px;
      height: 240px;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      color: #00796b;
      transition: all 0.3s;
      cursor: pointer;
    }
    .upload-zone:hover {
      background: #e0f7fa;
      border-color: #009688;
      transform: scale(1.01);
    }
    .upload-icon {
      font-size: 64px;
      width: 64px;
      height: 64px;
      margin-bottom: 16px;
      color: #26a69a;
    }

    /* Action Buttons */
    .actions {
      margin-top: 40px;
      display: flex;
      justify-content: flex-end;
      gap: 16px;
      border-top: 1px solid #f3f4f6;
      padding-top: 24px;
    }

    /* Analysis View */
    .analysis-placeholder {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      height: 100%;
      padding: 40px;
      text-align: center;
    }
    .large-btn {
      padding: 12px 48px;
      font-size: 18px;
      border-radius: 30px;
      margin-top: 32px;
      background: linear-gradient(135deg, #1e88e5, #42a5f5);
      box-shadow: 0 10px 20px rgba(30, 136, 229, 0.3);
    }
    .spinner {
      border: 4px solid #f3f3f3;
      border-top: 4px solid #009688;
      border-radius: 50%;
      width: 40px;
      height: 40px;
      animation: spin 1s linear infinite;
      margin-bottom: 24px;
    }
    @keyframes spin { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }
    
    .results-container { text-align: left; padding: 0 24px; }
    .result-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 32px; border-bottom: 1px solid #eee; padding-bottom: 16px; }
    .result-header h2 { margin: 0; color: #37474f; font-weight: 700; }
    
    .risk-badge { padding: 8px 24px; border-radius: 30px; font-weight: 800; font-size: 16px; letter-spacing: 1px; color: white; }
    .risk-badge.high { background: linear-gradient(135deg, #ef5350, #c62828); box-shadow: 0 4px 12px rgba(198, 40, 40, 0.3); }
    .risk-badge.low { background: linear-gradient(135deg, #66bb6a, #2e7d32); }
    
    .charts-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 32px; margin-bottom: 32px; }
    .chart-card { background: #f8f9fa; border-radius: 16px; padding: 24px; text-align: center; position: relative; }
    .chart-card h3 { color: #546e7a; margin-bottom: 16px; font-size: 16px; font-weight: 600; text-transform: uppercase; }
    .score-display { position: absolute; top: 55%; left: 50%; transform: translate(-50%, -50%); font-size: 32px; font-weight: 800; color: #37474f; }
    
    .recommendations { background: #e0f2f1; border-radius: 16px; padding: 24px; border: 1px solid #b2dfdb; }
    .recommendations h3 { display: flex; align-items: center; gap: 8px; color: #00695c; margin-top: 0; }
    .recommendations ul { margin: 0; padding-left: 20px; color: #004d40; }
    .recommendations li { margin-bottom: 8px; }
  `]
})
export class AiDiagnosisPageComponent {
  isAnalyzing = false;
  analysisComplete = false;
  riskScore = 85;
  riskLevel = 'high';

  // Data for Doughnut Chart
  public riskData: ChartData<'doughnut'> = {
    labels: ['Risk', 'Safe'],
    datasets: [{
      data: [85, 15],
      backgroundColor: ['#ef5350', '#e0e0e0'],
      borderWidth: 0,
      hoverOffset: 4
    }]
  };
  public riskOptions: ChartConfiguration['options'] = {
    responsive: true,
    cutout: '70%',
    plugins: { legend: { display: false } }
  };

  // Data for Bar Chart
  public confidenceData: ChartData<'bar'> = {
    labels: ['Imaging', 'Genomic', 'Clinical'],
    datasets: [{
      data: [92, 78, 88],
      backgroundColor: ['#42a5f5', '#ab47bc', '#ffa726'],
      borderRadius: 8,
      barThickness: 40
    }]
  };
  public confidenceOptions: ChartConfiguration['options'] = {
    responsive: true,
    plugins: { legend: { display: false } },
    scales: {
      y: { beginAtZero: true, max: 100 }
    }
  };

  runAnalysis() {
    this.isAnalyzing = true;
    // Simulate API delay
    setTimeout(() => {
      this.isAnalyzing = false;
      this.analysisComplete = true;
    }, 2000);
  }

  reset() {
    this.analysisComplete = false;
    this.isAnalyzing = false;
  }
}
