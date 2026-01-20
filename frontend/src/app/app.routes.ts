import { Routes } from '@angular/router';
import { environment } from '../environments/environment';
import { clientDataResolver } from './core/resolver/client-data-resolver';
import { Dashboard } from './features/dashboard/dashboard';
import { NotificationPage } from './features/notification/notification-page/notification-page';
import { ProjectForm } from './features/project/project-form/project-form';
import { ProjectPage } from './features/project/project-page/project-page';
import { SprintForm } from './features/sprint/sprint-form/sprint-form';
import { SprintPage } from './features/sprint/sprint-page/sprint-page';
import { TicketForm } from './features/ticket/ticket-form/ticket-form';
import { TicketPage } from './features/ticket/ticket-page/ticket-page';
import { UserProfile } from './features/user/user-profile/user-profile';
import { CrudTableDemo } from './shared/component/crud-table/crud-table-demo';
import { authenticationGuard } from './core/guards/authentication-guard';
import { Login } from './features/authentication/login/login';
import { Logout } from './features/authentication/logout/logout';

export const routes: Routes = [
  {
    path: "",
    pathMatch: "prefix",
    component: Dashboard,
    canActivate: [authenticationGuard],
    resolve: { clientDetails: clientDataResolver },
  },
  {
    path: "project",
    canActivate: [authenticationGuard],
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
    canActivate: [authenticationGuard],
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
    canActivate: [authenticationGuard],
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
    canActivate: [authenticationGuard],
    resolve: { clientDetails: clientDataResolver },
  },
  {
    path: "notifications",
    canActivate: [authenticationGuard],
    component: NotificationPage,
  },

  {
    path: "authentication/login",
    component: Login
  },
  {
    path: "authentication/logout",
    component: Logout
  },


  ...(environment.enableDevelopmentRoutes
    ? [
      {
        path: "crud-table-demo",
        component: CrudTableDemo,
      },
    ]
    : []),
];
