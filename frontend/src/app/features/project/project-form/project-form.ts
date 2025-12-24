import { Component } from '@angular/core';
import { Location } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ProjectDetails } from '../../../core/ng-openapi';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { ProjectService } from '../../../core/services/project-service';
import { DialogService } from '../../../core/services/dialog-service';
import { MatIconModule } from '@angular/material/icon';
import { ImageUpload } from "../../../shared/component/image-upload/image-upload";

@Component({
  selector: 'app-project-form',
  imports: [
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    ReactiveFormsModule,
    ImageUpload
  ],
  templateUrl: './project-form.html',
  styleUrl: './project-form.css',
})
export class ProjectForm {
  projectForm!: FormGroup;
  isEditMode: boolean = false;
  projectId: number = -1;

  statuses: ProjectDetails["status"][] = [
    "PLANNED",
    "ACTIVE",
    "DONE",
    "ARCHIVED"
  ];

  constructor(
    private projectService: ProjectService,
    private dialogService: DialogService,
    private formBuilder: FormBuilder,
    private location: Location,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.projectForm = this.formBuilder.group({
      title: ["", [Validators.required, Validators.maxLength(100)]],
      description: [""],
      profilePicture: [null],
      status: ["PLANNED", Validators.required]
    });


    this.route.params.subscribe(params => {
      this.projectId = params["id"]
      if (!this.projectId) {
        return;
      }
      this.isEditMode = true;
      this.loadProject(this.projectId);
    });
  }

  loadProject(id: number) {
    this.projectService.getDetailsById(id).subscribe({
      next: (response) => {
        this.projectForm.patchValue(response)
        this.loadProjectProfilePicture(id)
      },
      error: (error) => {
        console.error("Could not load project!", error);
        this.dialogService.openErrorDialog(
          "Could not load project! Try again later!",
          () => { this.exitForm() }
        );
      }
    });
  }

  loadProjectProfilePicture(id: number) {
    this.projectService.getProfilePicture(id).subscribe({
      next: (response: File | null) => {
        if (response == null) {
          return;
        }
        this.projectForm.get("profilePicture")?.setValue(response);
      },
      error: (error) => {
        console.error("Could not load project profile picture!", error);
        this.dialogService.openErrorDialog(
          "Warning! Could not load project profile picture!", null
        );
      }
    })
  }

  submit() {
    if (this.projectForm.invalid) {
      return;
    }

    const command = {
      id: this.projectId == -1 ? null : this.projectId,
      title: this.projectForm.value.title,
      description: this.projectForm.value.description,
      status: this.projectForm.value.status
    };

    const formData = new FormData();

    formData.append(
      "command",
      new Blob(
        [JSON.stringify(command)],
        { type: "application/json" }
      )
    );

    const profilePicture: File = this.projectForm.value.profilePicture
    if (profilePicture) {
      formData.append("profilePicture", profilePicture);
    }

    if (this.isEditMode) {

      this.projectService.update(formData).subscribe({
        next: () => this.exitForm(),
        error: (error) => {
          console.error("Could not save changes!", error)
          this.dialogService.openErrorDialog(
            "Could not save changes! Try again later!", null
          );
        }
      });
      return;
    }

    this.projectService.create(formData).subscribe({
      next: () => this.exitForm(),
      error: (error) => {
        console.error("Could not create project!", error)
        this.dialogService.openErrorDialog(
          "Could not create project! Try again later!", null
        );
      }
    });
  }

  exitForm() {
    this.location.back();
  }

}
