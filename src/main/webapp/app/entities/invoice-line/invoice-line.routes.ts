import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { InvoiceLineComponent } from './list/invoice-line.component';
import { InvoiceLineDetailComponent } from './detail/invoice-line-detail.component';
import { InvoiceLineUpdateComponent } from './update/invoice-line-update.component';
import InvoiceLineResolve from './route/invoice-line-routing-resolve.service';

const invoiceLineRoute: Routes = [
  {
    path: '',
    component: InvoiceLineComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InvoiceLineDetailComponent,
    resolve: {
      invoiceLine: InvoiceLineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InvoiceLineUpdateComponent,
    resolve: {
      invoiceLine: InvoiceLineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InvoiceLineUpdateComponent,
    resolve: {
      invoiceLine: InvoiceLineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default invoiceLineRoute;
