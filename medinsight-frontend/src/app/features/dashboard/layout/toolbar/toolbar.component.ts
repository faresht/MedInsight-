import { Component, Input, OnInit, inject } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-toolbar',
  template: `
    <mat-toolbar color="white" class="main-toolbar">
      <button mat-icon-button (click)="drawer.toggle()">
        <mat-icon>menu</mat-icon>
      </button>
      
      <span class="spacer"></span>

      <button mat-button [matMenuTriggerFor]="userMenu" class="user-button">
        <img src="assets/avatar-placeholder.png" class="avatar" alt="User" onError="this.src='https://material.angular.io/assets/img/examples/shiba1.jpg'">
        <span class="username">{{ username }}</span>
        <mat-icon>arrow_drop_down</mat-icon>
      </button>
      
      <mat-menu #userMenu="matMenu">
        <button mat-menu-item>
          <mat-icon>person</mat-icon>
          <span>Profile</span>
        </button>
        <button mat-menu-item>
          <mat-icon>settings</mat-icon>
          <span>Settings</span>
        </button>
        <mat-divider></mat-divider>
        <button mat-menu-item (click)="logout()">
          <mat-icon>logout</mat-icon>
          <span>Logout</span>
        </button>
      </mat-menu>
    </mat-toolbar>
  `,
  styles: [`
    .main-toolbar {
      background: white;
      box-shadow: 0 2px 4px rgba(0,0,0,0.05);
      position: relative;
      z-index: 2;
    }
    .user-button {
      display: flex;
      align-items: center;
      gap: 8px;
    }
    .avatar {
      width: 32px;
      height: 32px;
      border-radius: 50%;
      background-color: #eee;
      object-fit: cover;
    }
    .username {
      font-weight: 500;
    }
  `]
})
export class ToolbarComponent implements OnInit {
  @Input() drawer!: MatSidenav;
  username = 'User';

  private keycloakService = inject(KeycloakService);

  async ngOnInit() {
    try {
      if (await this.keycloakService.isLoggedIn()) {
        const userProfile = await this.keycloakService.loadUserProfile();
        this.username = userProfile.firstName ? `${userProfile.firstName} ${userProfile.lastName}` : userProfile.username || 'User';
      }
    } catch (error) {
      console.error('Failed to load user profile', error);
    }
  }

  logout() {
    this.keycloakService.logout();
  }
}
