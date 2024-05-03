import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExpenseNote } from '../expense-note.model';
import { ExpenseNoteService } from '../service/expense-note.service';

const expenseNoteResolve = (route: ActivatedRouteSnapshot): Observable<null | IExpenseNote> => {
  const id = route.params['id'];
  if (id) {
    return inject(ExpenseNoteService)
      .find(id)
      .pipe(
        mergeMap((expenseNote: HttpResponse<IExpenseNote>) => {
          if (expenseNote.body) {
            return of(expenseNote.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default expenseNoteResolve;
