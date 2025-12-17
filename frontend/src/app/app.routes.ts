import { Routes } from '@angular/router';
import { Dashboard } from './features/dashboard/dashboard';
import { environment } from '../environments/environment.development';
import { EntityTableDemo } from './shared/component/entity-table/entity-table-demo';

export const routes: Routes = [
    ...(environment.enableDevelopmentRoutes
    ? [
        {
          path: 'entity-table-demo',
          component: EntityTableDemo,
        },
      ]
    : []),
];
