import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AllegianceResolve from './route/allegiance-routing-resolve.service';

const allegianceRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/allegiance.component').then(m => m.AllegianceComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/allegiance-detail.component').then(m => m.AllegianceDetailComponent),
    resolve: {
      allegiance: AllegianceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/allegiance-update.component').then(m => m.AllegianceUpdateComponent),
    resolve: {
      allegiance: AllegianceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/allegiance-update.component').then(m => m.AllegianceUpdateComponent),
    resolve: {
      allegiance: AllegianceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default allegianceRoute;
