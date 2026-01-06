import { Injectable } from '@angular/core';
import { BehaviorSubject, debounceTime, Subject, switchMap, takeUntil, timer } from 'rxjs';

const DEBOUNCE_MILLISECONDS: number = 300;

@Injectable({
  providedIn: 'root',
})
export class LoaderService {
  private loadingSubject = new BehaviorSubject<boolean>(false);

  private showTrigger = new Subject<void>();
  private hideTrigger = new Subject<void>();

  readonly loading$ = this.loadingSubject.asObservable();

  constructor() {
    this.showTrigger.pipe(
      switchMap(() =>
        timer(DEBOUNCE_MILLISECONDS).pipe(
          takeUntil(this.hideTrigger)
        )
      )
    ).subscribe(() => {
      this.loadingSubject.next(true);
    });
  }

  show() {
    this.showTrigger.next();
  }

  hide() {
    this.hideTrigger.next();
    this.loadingSubject.next(false);
  }
}
