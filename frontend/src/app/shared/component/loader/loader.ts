import { AsyncPipe } from '@angular/common';
import { Component } from '@angular/core';
import { MatProgressSpinnerModule, MatSpinner } from '@angular/material/progress-spinner';
import { LoaderService } from '../../../core/services/loader-service';

@Component({
  selector: 'app-loader',
  imports: [
    MatProgressSpinnerModule,
    AsyncPipe,
  ],
  templateUrl: './loader.html',
  styleUrl: './loader.css',
})
export class Loader {
  loading$;

  constructor(private loaderService: LoaderService) {
    this.loading$ = this.loaderService.loading$;
  }
}
