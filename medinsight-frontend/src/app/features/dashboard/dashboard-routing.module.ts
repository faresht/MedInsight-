import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { DashboardHomeComponent } from './pages/dashboard-home/dashboard-home.component';

const routes: Routes = [
    {
        path: '',
        component: MainLayoutComponent,
        children: [
            { path: '', component: DashboardHomeComponent },
            { path: 'patients', loadComponent: () => import('./pages/patients-page.component').then(m => m.PatientsPageComponent) },
            { path: 'appointments', loadComponent: () => import('./pages/appointments-page.component').then(m => m.AppointmentsPageComponent) },
            { path: 'reports', loadComponent: () => import('./pages/reports-page.component').then(m => m.ReportsPageComponent) },
            { path: 'diagnosis', loadComponent: () => import('./pages/ai-diagnosis-page.component').then(m => m.AiDiagnosisPageComponent) }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class DashboardRoutingModule { }
