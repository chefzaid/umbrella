import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFormula } from '../formula.model';
import { FormulaService } from '../service/formula.service';

const formulaResolve = (route: ActivatedRouteSnapshot): Observable<null | IFormula> => {
  const id = route.params['id'];
  if (id) {
    return inject(FormulaService)
      .find(id)
      .pipe(
        mergeMap((formula: HttpResponse<IFormula>) => {
          if (formula.body) {
            return of(formula.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default formulaResolve;
