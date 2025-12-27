import { Component, ElementRef, HostBinding, Input, Optional, Self } from '@angular/core';
import { AbstractControlDirective, ControlValueAccessor, NgControl } from '@angular/forms';
import { MatFormFieldControl } from '@angular/material/form-field';
import { UserSummary } from '../../../core/ng-openapi';
import { Observable, Subject } from 'rxjs';
import { FocusMonitor } from '@angular/cdk/a11y';
import { MatDialog, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { UserListModal } from '../user-list-modal/user-list-modal';
import { UserService } from '../../../core/services/user-service';
import { FallbackImage } from '../../directive/fallback-image/fallback-image';
import { PageQuery } from '../../types/types';

@Component({
  selector: 'app-user-select',
  imports: [
    FallbackImage,
    MatDialogModule
  ],
  providers: [
    { provide: MatFormFieldControl, useExisting: UserSelect }
  ],
  templateUrl: './user-select.html',
  styleUrl: './user-select.css',
})
export class UserSelect implements MatFormFieldControl<UserSummary>, ControlValueAccessor {
  static nextId = 0;

  @Input() placeholder = "";
  @Input() required = false;
  @Input() disabled = false;

  value!: UserSummary | null;

  id = "";
  describedBy = "";
  controlType = "user-select";

  readonly stateChanges = new Subject<void>();

  focused = false;

  private onChange: (value: UserSummary | null) => void = () => {};
  private onTouched: () => void = () => {};
  
  get empty(): boolean {
    return !this.value;
  }

  get shouldLabelFloat(): boolean {
    return this.focused || !this.empty;
  }

  @HostBinding("class.floating")
  get isFloating(): boolean {
    return this.shouldLabelFloat;
  }

  get errorState(): boolean {
    return !!(
      this.ngControl &&
      this.ngControl.invalid &&
      (this.ngControl.touched || this.ngControl.dirty)
    );
  }

  constructor(
    private userService: UserService,
    private focusMonitor: FocusMonitor,
    private elementRef: ElementRef<HTMLElement>,
    @Optional() @Self() public ngControl: NgControl,
    private dialog: MatDialog,
  ) {
    if (ngControl) {
      ngControl.valueAccessor = this;
    }

    this.focusMonitor.monitor(this.elementRef, true).subscribe(origin => {
      this.focused = !!origin;
      this.stateChanges.next();
    });
  }

  ngOnInit() {
    this.id = `user-select-${UserSelect.nextId++}`;
  }

  setDescribedByIds(ids: string[]): void {
    this.describedBy = ids.join(" ");
  }
  onContainerClick(event?: MouseEvent): void {
    if (this.disabled) return;

    if (!this.focused) {
      this.elementRef.nativeElement.focus();
    }

    this.onTouched();
    this.openUserList();
  }

  writeValue(userSummary: UserSummary | null): void {
    this.value = userSummary;
    this.stateChanges.next();
  }

  registerOnChange(fn: (value: UserSummary | null) => void): void {
    this.onChange = fn;
  }
  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }
  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
    this.stateChanges.next();
  }

  openUserList() {
    this.dialog.open(UserListModal, {
      maxWidth: "600px",
      width: "600px",
      data: {
        fetchUsers: (query: PageQuery) => this.userService.getSummaryList(query),
        onUserClick: (dialogRef: MatDialogRef<UserListModal>, user: UserSummary) => {
          dialogRef.close();
          this.loadUser(user);
        },
        description: "User Select"
      }
    });
  }
  loadUser(user: UserSummary) {
    this.value = user;
    this.onChange(user);
    this.onTouched();
    this.stateChanges.next();
  }

  ngOnDestroy(): void {
    this.focusMonitor.stopMonitoring(this.elementRef);
    this.stateChanges.complete();
  }
}
