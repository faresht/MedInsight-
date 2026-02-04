import { Component, inject } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';

@Component({
  selector: 'app-main-layout',
  template: `
    <div class="layout-container">
      <mat-sidenav-container class="sidenav-container">
        <mat-sidenav #drawer class="sidenav" fixedInViewport
            [attr.role]="(isHandset$ | async) ? 'dialog' : 'navigation'"
            [mode]="(isHandset$ | async) ? 'over' : 'side'"
            [opened]="(isHandset$ | async) === false">
          <app-sidebar></app-sidebar>
        </mat-sidenav>
        <mat-sidenav-content>
          <app-toolbar [drawer]="drawer"></app-toolbar>
          <main class="content">
            <router-outlet></router-outlet>
          </main>
        </mat-sidenav-content>
      </mat-sidenav-container>
    </div>
  `,
  styles: [`
    .layout-container {
      position: absolute;
      top: 0;
      bottom: 0;
      left: 0;
      right: 0;
    }
    .sidenav-container {
      height: 100%;
    }
    .sidenav {
      width: 250px;
      box-shadow: 2px 0 5px rgba(0,0,0,0.1);
      border-right: none;
    }
    .content {
      padding: 20px;
      height: calc(100vh - 64px); // Adjust based on toolbar height
      overflow-y: auto;
      background-color: #f5f7fa;
    }
  `]
})
export class MainLayoutComponent {
  private breakpointObserver = inject(BreakpointObserver);

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );
}
