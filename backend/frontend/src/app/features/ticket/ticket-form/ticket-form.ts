import { Component } from '@angular/core';
import { Location } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { TicketDetails, UserSummary } from '../../../core/ng-openapi';
import { TicketService } from '../../../core/services/ticket-service';
import { DialogService } from '../../../core/services/dialog-service';
import { ActivatedRoute } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { UserSelect } from "../../../shared/component/user-select/user-select";

@Component({
  selector: 'app-ticket-form',
  imports: [
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatSelectModule,
    MatButtonModule,
    MatInputModule,
    UserSelect
],
  templateUrl: './ticket-form.html',
  styleUrl: './ticket-form.css',
})
export class TicketForm {
  ticketForm!: FormGroup;
  isEditMode: boolean = false;
  ticketId: number = -1;
  sprintId: number = -1;

  statuses: TicketDetails["status"][] = [
    "COMPLETED",
    "IN_PROGRESS",
    "IN_TESTING",
    "BACKLOG"
  ];

  priorities: TicketDetails["priority"][] = [
    "HIGH",
    "MEDIUM",
    "LOW"
  ];
  
  constructor(
    private ticketService: TicketService,
    private dialogService: DialogService,
    private formBuilder: FormBuilder,
    private location: Location,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.ticketForm = this.formBuilder.group({
      title: ["", [Validators.required, Validators.maxLength(100)]],
      description: ["", [Validators.required]],
      userSummary: ["", Validators.required],
      status: ["PLANNED", Validators.required],
      priority: ["PLANNED", Validators.required],
    });

    this.route.params.subscribe(params => {
      this.ticketId = params["id"]
      this.isEditMode = this.ticketId ? true : false;

      this.route.queryParams.subscribe(queryParams => {
        this.sprintId = queryParams["sprintId"];
        if (!this.sprintId && !this.isEditMode) {
          this.dialogService.openErrorDialog(
            "No parent sprint to place ticket in! Try creating ticket again!",
            () => {this.location.back();}
          )
          return;
        }

        if (!this.isEditMode) {
          return;
        }
        this.ticketForm.get("userSummary")?.disable({ onlySelf: true, emitEvent: false });   
        this.loadTicket(this.ticketId);
      });
    });
  }

  loadTicket(id: number) {
    this.ticketService.getDetailsById(id).subscribe({
      next: (response) => {
        this.ticketForm.patchValue(response);
      },
      error: (error) => {
        console.error("Could not load ticket!", error);
        this.dialogService.openErrorDialog(
          "Could not load ticket! Try again later!",
          () => { this.exitForm() }
        );
      }
    });
  }

  submit() {
    if (this.ticketForm.invalid) {
      return;
    }

    const userId: string = this.ticketForm.get("userSummary")?.value.id;

    if (this.isEditMode) {
      this.ticketService.update(
        {
          id: this.ticketId,
          title: this.ticketForm.get("title")?.value,
          description: this.ticketForm.get("description")?.value,
          assignedUserId: userId,
          priority: this.ticketForm.get("priority")?.value,
          status: this.ticketForm.get("status")?.value,
        }
      ).subscribe({
        next: (response) => {
          this.exitForm();
        },
        error: (error) => {
          console.error("Could not save changes!", error);
          this.dialogService.openErrorDialog(
            "Could not save changes! Try again later!",
            null
          );
        }
      });
      return;
    }

    this.ticketService.create(
      {
        title: this.ticketForm.get("title")?.value,
        description: this.ticketForm.get("description")?.value,
        assignedUserId: userId,
        priority: this.ticketForm.get("priority")?.value,
        status: this.ticketForm.get("status")?.value,
        sprintId: this.sprintId,
      }
    ).subscribe({
      next: (response) => {
        this.exitForm();
      },
      error: (error) => {
        console.error("Could not save changes!", error);
        this.dialogService.openErrorDialog(
          "Could not save changes! Try again later!",
          null
        );
      }
    });
  }

  exitForm() {
    this.location.back();
  }
}
