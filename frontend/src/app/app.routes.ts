import { Routes } from '@angular/router';
import { Dashboard } from './features/dashboard/dashboard';
import { environment } from '../environments/environment';
import { EntityTableDemo } from './shared/component/entity-table/entity-table-demo';
import { ProjectPage } from './features/project/project-page/project-page';
import { ManageProjectPage } from './features/project/manage-project-page/manage-project-page';
import { SprintPage } from './features/sprint/sprint-page/sprint-page';
import { UserProfile } from './features/user/user-profile/user-profile';
import { ManageSprintPage } from './features/sprint/manage-sprint-page/manage-sprint-page';
import { NotificationPage } from './features/notification/notification-page/notification-page';
import { ManageTicketPage } from './features/ticket/manage-ticket-page/manage-ticket-page';

export const routes: Routes = [
    { path: "", component: Dashboard },
    { path: "project", children: [
      { path: "create", component: ManageProjectPage },
      { path: "update/:id", component: ManageProjectPage },
      { path: ":id", component: ProjectPage },
    ] },
    { path: "sprint", children: [
      { path: "create", component: ManageSprintPage },
      { path: "update/:id", component: ManageSprintPage },
      { path: ":id", component: SprintPage },
    ] },
    { path: "ticket", children: [
      { path: "create", component: ManageTicketPage },
      { path: "update/:id", component: ManageTicketPage },
      { path: ":id", component: SprintPage },
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
