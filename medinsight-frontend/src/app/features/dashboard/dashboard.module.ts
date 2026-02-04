import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { DashboardRoutingModule } from './dashboard-routing.module';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { SidebarComponent } from './layout/sidebar/sidebar.component';
import { ToolbarComponent } from './layout/toolbar/toolbar.component';
import { DashboardHomeComponent } from './pages/dashboard-home/dashboard-home.component';

@NgModule({
    declarations: [
        MainLayoutComponent,
        ToolbarComponent,
        DashboardHomeComponent
    ],
    imports: [
        SharedModule,
        DashboardRoutingModule,
        SidebarComponent
    ]
})
export class DashboardModule { }
