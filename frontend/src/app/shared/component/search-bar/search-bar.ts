import { AsyncPipe } from '@angular/common';
import { Component, Input, Output, EventEmitter, OnInit, OnDestroy } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatOptionModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { combineLatest, debounceTime, distinctUntilChanged, map, Observable, startWith, Subject, takeUntil } from 'rxjs';

const DEBOUNCE_TIME_MILLISECONDS: number = 500


export interface SearchQuery {
	search: string,
	filter: string
}

@Component({
	selector: 'app-search-bar',
	imports: [
		ReactiveFormsModule,
		MatFormFieldModule,
		MatAutocompleteModule,
		MatInputModule,
		MatSelectModule,
		MatOptionModule
	],
	templateUrl: './search-bar.html',
	styleUrl: './search-bar.css',
})

export class SearchBar implements OnInit, OnDestroy {
  @Input() filters: string[] = [];
  @Output() readonly queryEvent = new EventEmitter<SearchQuery>();

  searchControl = new FormControl('', { nonNullable: true });
  filterControl = new FormControl('', { nonNullable: true });

  private destroy$ = new Subject<void>();

  ngOnInit() {
    if (this.filters.length) {
      this.filterControl.setValue(this.filters[0]);
    }

    combineLatest([
      this.searchControl.valueChanges.pipe(startWith(this.searchControl.value)),
      this.filterControl.valueChanges.pipe(startWith(this.filterControl.value))
    ])
      .pipe(
        debounceTime(DEBOUNCE_TIME_MILLISECONDS),
        distinctUntilChanged(
          (
						[searchOld, filterOld], 
						[searchNew, filterNew]
					) => searchOld === searchNew && filterOld === filterNew
        ),
        takeUntil(this.destroy$)
      )
      .subscribe(([search, filter]) => {
        this.queryEvent.emit({ search, filter });
      });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }
}

