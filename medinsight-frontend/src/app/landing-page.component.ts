import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { RouterModule } from '@angular/router';

@Component({
    selector: 'app-landing-page',
    standalone: true,
    imports: [CommonModule, MatButtonModule, MatIconModule, RouterModule],
    template: `
    <div class="landing-container">
      <!-- Navbar -->
      <nav class="navbar">
        <div class="logo">
          <mat-icon>local_hospital</mat-icon>
          <span>MedInsight<span class="highlight">+</span></span>
        </div>
        <div class="nav-links">
          <a href="#features">Features</a>
          <a href="#about">About</a>
          <a href="#contact">Contact</a>
          <button mat-stroked-button color="primary" routerLink="/dashboard">Log In</button>
        </div>
      </nav>

      <!-- Hero Section -->
      <header class="hero">
        <div class="hero-content">
          <div class="badge">New: AI Agentic Diagnosis</div>
          <h1>The Future of <br><span class="gradient-text">Precision Medicine</span></h1>
          <p class="subtitle">
            MedInsight+ leverages advanced Multi-Agent AI to assist doctors in diagnosing breast cancer with unprecedented accuracy. 
            Integrate Clinical, Imaging, and Genomic data in one secure platform.
          </p>
          <div class="cta-group">
            <button mat-flat-button color="primary" class="cta-primary" routerLink="/dashboard">
              Get Started <mat-icon>arrow_forward</mat-icon>
            </button>
            <button mat-stroked-button class="cta-secondary">Watch Demo</button>
          </div>
        </div>
        <div class="hero-visual">
          <div class="glass-card card-1">
            <mat-icon class="icon-lg text-blue">analytics</mat-icon>
            <div class="card-text">
              <h3>99.8%</h3>
              <p>Diagnostic Accuracy</p>
            </div>
          </div>
          <div class="glass-card card-2">
            <mat-icon class="icon-lg text-teal">security</mat-icon>
            <div class="card-text">
              <h3>HIPAA</h3>
              <p>Compliant Security</p>
            </div>
          </div>
          <div class="glass-card card-3">
            <mat-icon class="icon-lg text-purple">psychology</mat-icon>
            <div class="card-text">
              <h3>4 Agents</h3>
              <p>Cooperative AI</p>
            </div>
          </div>
        </div>
      </header>

      <!-- Features Section -->
      <section id="features" class="features">
        <div class="section-header">
           <h2>Engineered for Excellence</h2>
           <p>A complete ecosystem for modern healthcare facilities.</p>
        </div>
        
        <div class="feature-grid">
          <div class="feature-card">
            <div class="icon-box bg-blue-light"><mat-icon>medical_services</mat-icon></div>
            <h3>Multimodal AI</h3>
            <p>Combines mammography, histology, and patient history for holistic analysis.</p>
          </div>
           <div class="feature-card">
            <div class="icon-box bg-teal-light"><mat-icon>speed</mat-icon></div>
            <h3>Real-time Results</h3>
            <p>Get diagnostic confidence scores and explainable reasoning in seconds.</p>
          </div>
           <div class="feature-card">
            <div class="icon-box bg-purple-light"><mat-icon>hub</mat-icon></div>
            <h3>Seamless Integration</h3>
            <p>Connects with existing EHR systems and DICOM servers effortlessly.</p>
          </div>
        </div>
      </section>

      <!-- Footer -->
      <footer>
        <p>&copy; 2024 MedInsight+ E-Health System. Built for Advanced Software Engineering.</p>
      </footer>
    </div>
  `,
    styles: [`
    :host {
      display: block;
      min-height: 100vh;
      background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
      font-family: 'Roboto', sans-serif;
    }
    
    .landing-container {
      max-width: 100%;
      overflow-x: hidden;
    }

    /* Navbar */
    .navbar {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 24px 48px;
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      z-index: 10;
    }
    .logo {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 24px;
      font-weight: 700;
      color: #37474f;
    }
    .highlight { color: #009688; }
    .nav-links { display: flex; gap: 32px; align-items: center; }
    .nav-links a { text-decoration: none; color: #546e7a; font-weight: 500; transition: color 0.3s; }
    .nav-links a:hover { color: #009688; }

    /* Hero */
    .hero {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 140px 8% 80px;
      min-height: 90vh;
    }
    .hero-content { max-width: 50%; }
    
    .badge {
      display: inline-block;
      background: rgba(0, 150, 136, 0.1);
      color: #009688;
      padding: 8px 16px;
      border-radius: 20px;
      font-weight: 600;
      font-size: 14px;
      margin-bottom: 24px;
    }
    
    h1 {
      font-size: 64px;
      line-height: 1.1;
      font-weight: 800;
      color: #263238;
      margin-bottom: 24px;
    }
    .gradient-text {
      background: linear-gradient(45deg, #009688, #4db6ac);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
    }
    .subtitle {
      font-size: 18px;
      color: #546e7a;
      line-height: 1.6;
      margin-bottom: 40px;
      max-width: 80%;
    }
    
    .cta-group { display: flex; gap: 16px; }
    .cta-primary { 
      padding: 24px 32px !important; 
      font-size: 16px !important; 
      border-radius: 30px !important; 
    }
    .cta-secondary {
       padding: 24px 32px !important; 
       font-size: 16px !important; 
       border-radius: 30px !important;
       border-color: #cfd8dc !important;
       color: #546e7a !important;
    }

    /* Visual (Glass Cards) */
    .hero-visual {
      position: relative;
      width: 45%;
      height: 400px;
    }
    .glass-card {
      position: absolute;
      background: rgba(255, 255, 255, 0.7);
      backdrop-filter: blur(10px);
      border-radius: 24px;
      padding: 24px;
      box-shadow: 0 8px 32px rgba(0, 0, 0, 0.05);
      border: 1px solid rgba(255, 255, 255, 0.8);
      display: flex;
      align-items: center;
      gap: 16px;
      width: 200px;
      animation: float 6s ease-in-out infinite;
    }
    .card-1 { top: 0; right: 20%; z-index: 3; }
    .card-2 { top: 120px; left: 10%; z-index: 2; animation-delay: 1s; }
    .card-3 { bottom: 0; right: 10%; z-index: 1; animation-delay: 2s; }
    
    @keyframes float {
      0% { transform: translateY(0px); }
      50% { transform: translateY(-20px); }
      100% { transform: translateY(0px); }
    }

    .icon-lg { transform: scale(1.5); }
    .text-blue { color: #1e88e5; }
    .text-teal { color: #009688; }
    .text-purple { color: #8e24aa; }
    
    .card-text h3 { margin: 0; font-size: 20px; font-weight: 700; color: #37474f; }
    .card-text p { margin: 0; font-size: 12px; color: #78909c; }

    /* Features */
    .features { padding: 80px 8%; background: white; }
    .section-header { text-align: center; margin-bottom: 64px; }
    .section-header h2 { font-size: 36px; font-weight: 700; color: #263238; }
    
    .feature-grid {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 40px;
    }
    .feature-card {
      padding: 32px;
      background: #fcfcfc;
      border-radius: 16px;
      transition: transform 0.3s;
    }
    .feature-card:hover { transform: translateY(-10px); box-shadow: 0 10px 20px rgba(0,0,0,0.05); }
    
    .icon-box {
      width: 60px;
      height: 60px;
      border-radius: 16px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 24px;
    }
    .bg-blue-light { background: #e3f2fd; color: #1e88e5; }
    .bg-teal-light { background: #e0f2f1; color: #009688; }
    .bg-purple-light { background: #f3e5f5; color: #8e24aa; }
    
    .feature-card h3 { font-size: 20px; font-weight: 600; margin-bottom: 12px; }
    .feature-card p { color: #546e7a; line-height: 1.6; }

    footer {
      text-align: center;
      padding: 40px;
      color: #90a4ae;
      font-size: 14px;
      background: #eceff1;
    }

    @media (max-width: 960px) {
      .hero { flex-direction: column; padding-top: 100px; }
      .hero-content { max-width: 100%; text-align: center; margin-bottom: 40px; }
      .cta-group { justify-content: center; }
      .feature-grid { grid-template-columns: 1fr; }
      .hero-visual { width: 100%; height: 300px; }
      .nav-links { display: none; } /* Mobile menu simplified for now */
    }
  `]
})
export class LandingPageComponent { }
