import { Component } from '@angular/core';
import { Location } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { SprintDetails } from '../../../core/ng-openapi';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { SprintService } from '../../../core/services/sprint-service';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { DialogService } from '../../../core/services/dialog-service';

@Component({
  selector: 'app-sprint-form',
  imports: [
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatCardModule,
    MatDatepickerModule,
    MatNativeDateModule,
    ReactiveFormsModule,
  ],
  templateUrl: './sprint-form.html',
  styleUrl: './sprint-form.css',
})
export class SprintForm {
  sprintForm!: FormGroup;
  isEditMode: boolean = false;
  sprintId: number = -1;
  projectId: number = -1;

  statuses: SprintDetails["status"][] = [
    "PLANNED",
    "ACTIVE",
    "DONE",
    "PAUSED"
  ];

  constructor(
    private sprintService: SprintService,
    private dialogService: DialogService,
    private formBuilder: FormBuilder,
    private location: Location,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.sprintForm = this.formBuilder.group({
      title: ["", [Validators.required, Validators.maxLength(100)]],
      description: ["", [Validators.required]],
      startDate: [null, Validators.required],
      dueDate: [null, Validators.required],
      endDate: [null],
      status: ["PLANNED", Validators.required]
    });

    this.route.params.subscribe(params => {
      this.sprintId = params["id"]
      this.isEditMode = this.sprintId ? true : false;

      this.route.queryParams.subscribe(queryParams => {
        this.projectId = queryParams["projectId"];
        if (!this.projectId) {
          this.dialogService.openErrorDialog(
            "No parent project to place sprint in! Try creating/updating sprint again!",
            () => {this.location.back();}
          )
          return;
        }

        if (!this.isEditMode) {
          return;
        }
        this.loadSprint(this.sprintId);
      });
    });
  }

  loadSprint(id: number) {
    this.sprintService.getDetailsById(id).subscribe({
      next: (response) => {
        response.startDate = new Date(response.startDate);
        response.dueDate = new Date(response.dueDate);
        response.endDate = response.endDate
          ? new Date(response.endDate)
          : undefined;
        this.sprintForm.patchValue(response);
      },
      error: (error) => {
        console.error("Could not load sprint!", error);
        this.dialogService.openErrorDialog(
          "Could not load sprint! Try again later.",
          () => {this.location.back();}
        )
      }
    });
  }

  submit() {
    if (this.sprintForm.invalid) {
      return;
    }

    if (this.isEditMode) {
      this.sprintService.update(
        {
          id: this.route.snapshot.paramMap.get("id"),
          ...this.sprintForm.value,
        }
      ).subscribe({
        next: (response) => {
          this.exitForm();
        },
        error: (error) => {
          console.error("Could not save changes!", error);
        }
      });
      return;
    }

    this.sprintService.create(
      {
        ...this.sprintForm.value,
        projectId: this.projectId
      }
    ).subscribe({
      next: (response) => {
        this.exitForm();
      },
      error: (error) => {
        console.error("Could not save changes!", error);
      }
    });
  }

  exitForm() {
    this.location.back();
  }
}

