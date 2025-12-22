import { Routes } from '@angular/router';
import { Dashboard } from './features/dashboard/dashboard';
import { environment } from '../environments/environment';
import { EntityTableDemo } from './shared/component/entity-table/entity-table-demo';
import { ProjectPage } from './features/project/project-page/project-page';
import { SprintPage } from './features/sprint/sprint-page/sprint-page';
import { UserProfile } from './features/user/user-profile/user-profile';
import { NotificationPage } from './features/notification/notification-page/notification-page';
import { TicketPage } from './features/ticket/ticket-page/ticket-page';
import { SprintForm } from './features/sprint/sprint-form/sprint-form';
import { ProjectForm } from './features/project/project-form/project-form';
import { TicketForm } from './features/ticket/ticket-form/ticket-form';

export const routes: Routes = [
    { path: "", component: Dashboard },
    { path: "project", children: [
      { path: "create", component: ProjectForm },
      { path: "update/:id", component: ProjectForm },
      { path: ":id", component: ProjectPage },
    ] },
    { path: "sprint", children: [
      { path: "create", component: SprintForm },
      { path: "update/:id", component: SprintForm },
      { path: ":id", component: SprintPage },
    ] },
    { path: "ticket", children: [
      { path: "create", component: TicketForm },
      { path: "update/:id", component: TicketForm },
      { path: ":id", component: TicketPage },
    ] },
    { path: "user/:id", component: UserProfile },
    { path: "notifications", component: NotificationPage },

    
    ...(environment.enableDevelopmentRoutes
    ? [
        {
          path: "entity-table-demo",
          component: EntityTableDemo,
        },
      ]
    : []),
];
