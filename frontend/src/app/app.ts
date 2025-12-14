import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MatSliderModule } from '@angular/material/slider';
import { MatSidenav, MatSidenavContainer, MatSidenavContent, MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbar } from '@angular/material/toolbar';
import { MatIcon } from '@angular/material/icon';
import { MatNavList } from '@angular/material/list';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet, 
    MatSidenavContainer, 
    MatSidenav, 
    MatSidenavModule,
    MatSidenavContent, 
    MatToolbar,
    MatIcon,
  ],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');

  logout() {

  }
}
