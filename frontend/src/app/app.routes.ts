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
import { clientDataResolver } from './core/resolver/client-data-resolver';
import { CrudTableDemo } from './shared/component/crud-table/crud-table-demo';

export const routes: Routes = [
  { 
    path: "", 
    component: Dashboard,
    resolve: { clientDetails: clientDataResolver },
  },
  { 
    path: "project", 
    resolve: { clientDetails: clientDataResolver }, 
    children: [
      { 
        path: "create", 
        component: ProjectForm,
        resolve: { clientDetails: clientDataResolver },
      },
      { 
        path: "update/:id", 
        component: ProjectForm,
        resolve: { clientDetails: clientDataResolver },
       },
      { 
        path: ":id", 
        component: ProjectPage,
        resolve: { clientDetails: clientDataResolver },
      },
    ] 
  },
  { 
    path: "sprint", 
    resolve: { clientDetails: clientDataResolver },
    children: [
      { 
        path: "create", 
        component: SprintForm,
        resolve: { clientDetails: clientDataResolver },
      },
      { 
        path: "update/:id", 
        component: SprintForm,
        resolve: { clientDetails: clientDataResolver },
      },
      { 
        path: ":id", 
        component: SprintPage,
        resolve: { clientDetails: clientDataResolver },
      },
    ] 
  },
  { 
    path: "ticket", 
    resolve: { clientDetails: clientDataResolver },
    children: [
      { 
        path: "create", 
        component: TicketForm,
        resolve: { clientDetails: clientDataResolver },
      },
      { 
        path: "update/:id", 
        component: TicketForm,
        resolve: { clientDetails: clientDataResolver },
      },
      { 
        path: ":id", 
        component: TicketPage,
        resolve: { clientDetails: clientDataResolver },
      },
    ] 
  },
  { 
    path: "user/:id", 
    component: UserProfile, 
    resolve: { clientDetails: clientDataResolver },
  },
  { 
    path: "notifications", 
    component: NotificationPage, 
    resolve: { clientDetails: clientDataResolver },
  },

    
    ...(environment.enableDevelopmentRoutes
    ? [
        {
          path: "entity-table-demo",
          component: EntityTableDemo,
        },
        {
          path: "crud-table-demo",
          component: CrudTableDemo,
        },
      ]
    : []),
];
