import { Injectable } from '@angular/core';
import { BaseApiService } from './base-api-service';
import { PageQuery, PageResponseSprintSummary, SprintControllerService, SprintCreate, SprintDetails, SprintUpdate } from '../ng-openapi';
import { catchError, Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class SprintService extends BaseApiService {

  constructor(
    private sprintController: SprintControllerService
  ) {
    super();
  }

  getSummaryList(query: PageQuery): Observable<PageResponseSprintSummary> {
    return this.sprintController
      .getSummaryList2(query, 'body')
      .pipe(catchError(this.handleError));
  }

  getDetailsById(id: number): Observable<SprintDetails> {
    return this.sprintController
      .getDetailsById2(id, 'body')
      .pipe(catchError(this.handleError));
  }

  create(command: SprintCreate): Observable<any> {
    return this.sprintController
      .createSprint(command, 'body')
      .pipe(catchError(this.handleError));
  }

  update(command: SprintUpdate): Observable<any> {
    return this.sprintController
      .updateSprint(command, 'body')
      .pipe(catchError(this.handleError));
  }

  deleteById(id: number): Observable<any> {
    return this.sprintController
      .deleteSprintById(id, 'body')
      .pipe(catchError(this.handleError));
  }
}

