import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIdDocument } from '../id-document.model';
import { IdDocumentService } from '../service/id-document.service';

const idDocumentResolve = (route: ActivatedRouteSnapshot): Observable<null | IIdDocument> => {
  const id = route.params['id'];
  if (id) {
    return inject(IdDocumentService)
      .find(id)
      .pipe(
        mergeMap((idDocument: HttpResponse<IIdDocument>) => {
          if (idDocument.body) {
            return of(idDocument.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default idDocumentResolve;
