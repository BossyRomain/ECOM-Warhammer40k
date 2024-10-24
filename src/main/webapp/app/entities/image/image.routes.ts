import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ImageResolve from './route/image-routing-resolve.service';

const imageRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/image.component').then(m => m.ImageComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/image-detail.component').then(m => m.ImageDetailComponent),
    resolve: {
      image: ImageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/image-update.component').then(m => m.ImageUpdateComponent),
    resolve: {
      image: ImageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/image-update.component').then(m => m.ImageUpdateComponent),
    resolve: {
      image: ImageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default imageRoute;
