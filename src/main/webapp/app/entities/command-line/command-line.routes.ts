import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CommandLineResolve from './route/command-line-routing-resolve.service';

const commandLineRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/command-line.component').then(m => m.CommandLineComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/command-line-detail.component').then(m => m.CommandLineDetailComponent),
    resolve: {
      commandLine: CommandLineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/command-line-update.component').then(m => m.CommandLineUpdateComponent),
    resolve: {
      commandLine: CommandLineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/command-line-update.component').then(m => m.CommandLineUpdateComponent),
    resolve: {
      commandLine: CommandLineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default commandLineRoute;
