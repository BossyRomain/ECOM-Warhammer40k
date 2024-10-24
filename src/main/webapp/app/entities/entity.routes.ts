import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'ecomApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'client',
    data: { pageTitle: 'ecomApp.client.home.title' },
    loadChildren: () => import('./client/client.routes'),
  },
  {
    path: 'cart',
    data: { pageTitle: 'ecomApp.cart.home.title' },
    loadChildren: () => import('./cart/cart.routes'),
  },
  {
    path: 'command-line',
    data: { pageTitle: 'ecomApp.commandLine.home.title' },
    loadChildren: () => import('./command-line/command-line.routes'),
  },
  {
    path: 'product',
    data: { pageTitle: 'ecomApp.product.home.title' },
    loadChildren: () => import('./product/product.routes'),
  },
  {
    path: 'allegiance',
    data: { pageTitle: 'ecomApp.allegiance.home.title' },
    loadChildren: () => import('./allegiance/allegiance.routes'),
  },
  {
    path: 'product-image',
    data: { pageTitle: 'ecomApp.productImage.home.title' },
    loadChildren: () => import('./product-image/product-image.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
