import { ChangeDetectorRef, Component, Input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { DialogService } from '../../../core/services/dialog-service';
import { Observable, of } from 'rxjs';
import { Location } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-entity-card',
  imports: [
    MatCardModule,
    MatButtonModule
  ],
  templateUrl: './entity-card.html',
  styleUrl: './entity-card.css',
})
export class EntityCard<T> {
  constructor(
    private dialogService: DialogService,
    private changeDetectorRef: ChangeDetectorRef,
    private location: Location,
  ) {

  }

  @Input() showManipulateOptions: boolean = false;

  @Input() entity!: T;
  @Input() entityName: string = "Entity";
  @Input() update: (entity: T) => void = (entity: T) => {};
  @Input() delete: (entity: T) => Observable<void> = (entity: T) => {return of()};

  onUpdate() {
    this.update(this.entity);
  }
  onDelete() {
    this.dialogService.openConfirmDialog(
      `Delete current ${this.entityName.toLowerCase()}?`, null, null, null,
      () => {
        this.delete(this.entity).subscribe({
          next: () => {
            this.location.back(); // i'd prefer a more specific back
          },
          error: (error) => {
            console.error(`Could not delete ${this.entityName.toLowerCase()}!`, error);
            this.dialogService.openErrorDialog(
              `Could not delete ${this.entityName.toLowerCase()}!\nPlease try again later!`, 
              null
            );
          }
    });});
  }
}
