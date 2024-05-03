import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { IdDocumentComponent } from './list/id-document.component';
import { IdDocumentDetailComponent } from './detail/id-document-detail.component';
import { IdDocumentUpdateComponent } from './update/id-document-update.component';
import IdDocumentResolve from './route/id-document-routing-resolve.service';

const idDocumentRoute: Routes = [
  {
    path: '',
    component: IdDocumentComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IdDocumentDetailComponent,
    resolve: {
      idDocument: IdDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IdDocumentUpdateComponent,
    resolve: {
      idDocument: IdDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IdDocumentUpdateComponent,
    resolve: {
      idDocument: IdDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default idDocumentRoute;
