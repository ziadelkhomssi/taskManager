import { FocusMonitor } from '@angular/cdk/a11y';
import { Component, ElementRef, HostBinding, Input, Optional, Self, ViewChild } from '@angular/core';
import { AbstractControl, ControlValueAccessor, FormGroup, NgControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { MatFormFieldControl } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Subject } from 'rxjs';

export function fileSizeValidator(maxSizeInBytes: number) {
  return (control: AbstractControl) => {
    const file = control.value as File | null;
    if (file && file.size > maxSizeInBytes) {
      return { fileSizeExceeded: { fileSize: file.size, maxFileSize: maxSizeInBytes } };
    }
    return null;
  };
}   

@Component({
  selector: 'app-image-upload',
  imports: [
    MatIconModule
  ],
  providers: [
    { provide: MatFormFieldControl, useExisting: ImageUpload }
  ],
  templateUrl: './image-upload.html',
  styleUrl: './image-upload.css',
  host: {
    "[attr.aria-describedby]": "describedBy"
  }
})
export class ImageUpload implements MatFormFieldControl<File | null>, ControlValueAccessor {
  static nextId = 0;

  @ViewChild("fileInput", { static: true })
  fileInput!: ElementRef<HTMLInputElement>;

  @Input() placeholder = "";
  @Input() required = false;
  @Input() disabled = false;

  value!: File | null;
  imagePreview = "";
  imageName = "";
  fileSizeKb = 0;

  id = "";
  describedBy = "";
  userAriaDescribedBy?: string | undefined;
  controlType = "image-upload";

  readonly stateChanges = new Subject<void>();
  autofilled?: boolean | undefined;
  disableAutomaticLabeling?: boolean | undefined;
  describedByIds?: string[] | undefined;

  focused = false;

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
    private focusMonitor: FocusMonitor,
    private elementRef: ElementRef<HTMLElement>,
    @Optional() @Self() public ngControl: NgControl
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
    this.id = `image-upload-${ImageUpload.nextId++}`;
  }

  private onChange: (value: File | null) => void = () => {};
  private onTouched: () => void = () => {};

  setDescribedByIds(ids: string[]): void {
    this.describedBy = ids.join(" ");
  }

  onContainerClick(): void {
    if (!this.disabled) {
      this.onTouched();
      this.fileInput.nativeElement.click();
    }
  }

  writeValue(file: File | null): void {
    if (file) {
      this.loadFile(file);
    } else {
      this.clear();
    }
  }

  registerOnChange(fn: (value: File | null) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
    this.stateChanges.next();
  }

  onFileChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    this.handleFile(file);
  }

  onFileDrop(event: DragEvent): void {
    event.preventDefault();
    if (this.disabled) return;

    const file = event.dataTransfer?.files?.[0];
    this.handleFile(file);
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
  }

  removeImage(event: MouseEvent): void {
    event.stopPropagation();
    this.clear();
    this.onChange(null);
    this.onTouched();
  }

  private handleFile(file?: File): void {
    if (!file || !file.type.startsWith("image/")) {
      return;
    }

    this.loadFile(file);
    this.onChange(file);
    this.onTouched();
  }

  private loadFile(file: File): void {
    this.value = file;
    this.imageName = file.name;
    this.fileSizeKb = Math.round(file.size / 1024);

    const reader = new FileReader();
    reader.onload = () => {
      this.imagePreview = reader.result as string;
      this.stateChanges.next();
    };
    reader.readAsDataURL(file);
  }

  private clear(): void {
    this.value = null;
    this.imagePreview = "";
    this.imageName = "";
    this.fileSizeKb = 0;
    this.fileInput.nativeElement.value = "";
    this.stateChanges.next();
  }

  ngOnDestroy(): void {
    this.focusMonitor.stopMonitoring(this.elementRef);
    this.stateChanges.complete();
  }
}
