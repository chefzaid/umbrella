import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExpenseNote, NewExpenseNote } from '../expense-note.model';

export type PartialUpdateExpenseNote = Partial<IExpenseNote> & Pick<IExpenseNote, 'id'>;

type RestOf<T extends IExpenseNote | NewExpenseNote> = Omit<T, 'creationDate' | 'submitDate' | 'validationDate'> & {
  creationDate?: string | null;
  submitDate?: string | null;
  validationDate?: string | null;
};

export type RestExpenseNote = RestOf<IExpenseNote>;

export type NewRestExpenseNote = RestOf<NewExpenseNote>;

export type PartialUpdateRestExpenseNote = RestOf<PartialUpdateExpenseNote>;

export type EntityResponseType = HttpResponse<IExpenseNote>;
export type EntityArrayResponseType = HttpResponse<IExpenseNote[]>;

@Injectable({ providedIn: 'root' })
export class ExpenseNoteService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/expense-notes');

  create(expenseNote: NewExpenseNote): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expenseNote);
    return this.http
      .post<RestExpenseNote>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(expenseNote: IExpenseNote): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expenseNote);
    return this.http
      .put<RestExpenseNote>(`${this.resourceUrl}/${this.getExpenseNoteIdentifier(expenseNote)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(expenseNote: PartialUpdateExpenseNote): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expenseNote);
    return this.http
      .patch<RestExpenseNote>(`${this.resourceUrl}/${this.getExpenseNoteIdentifier(expenseNote)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestExpenseNote>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestExpenseNote[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getExpenseNoteIdentifier(expenseNote: Pick<IExpenseNote, 'id'>): number {
    return expenseNote.id;
  }

  compareExpenseNote(o1: Pick<IExpenseNote, 'id'> | null, o2: Pick<IExpenseNote, 'id'> | null): boolean {
    return o1 && o2 ? this.getExpenseNoteIdentifier(o1) === this.getExpenseNoteIdentifier(o2) : o1 === o2;
  }

  addExpenseNoteToCollectionIfMissing<Type extends Pick<IExpenseNote, 'id'>>(
    expenseNoteCollection: Type[],
    ...expenseNotesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const expenseNotes: Type[] = expenseNotesToCheck.filter(isPresent);
    if (expenseNotes.length > 0) {
      const expenseNoteCollectionIdentifiers = expenseNoteCollection.map(expenseNoteItem => this.getExpenseNoteIdentifier(expenseNoteItem));
      const expenseNotesToAdd = expenseNotes.filter(expenseNoteItem => {
        const expenseNoteIdentifier = this.getExpenseNoteIdentifier(expenseNoteItem);
        if (expenseNoteCollectionIdentifiers.includes(expenseNoteIdentifier)) {
          return false;
        }
        expenseNoteCollectionIdentifiers.push(expenseNoteIdentifier);
        return true;
      });
      return [...expenseNotesToAdd, ...expenseNoteCollection];
    }
    return expenseNoteCollection;
  }

  protected convertDateFromClient<T extends IExpenseNote | NewExpenseNote | PartialUpdateExpenseNote>(expenseNote: T): RestOf<T> {
    return {
      ...expenseNote,
      creationDate: expenseNote.creationDate?.format(DATE_FORMAT) ?? null,
      submitDate: expenseNote.submitDate?.toJSON() ?? null,
      validationDate: expenseNote.validationDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restExpenseNote: RestExpenseNote): IExpenseNote {
    return {
      ...restExpenseNote,
      creationDate: restExpenseNote.creationDate ? dayjs(restExpenseNote.creationDate) : undefined,
      submitDate: restExpenseNote.submitDate ? dayjs(restExpenseNote.submitDate) : undefined,
      validationDate: restExpenseNote.validationDate ? dayjs(restExpenseNote.validationDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestExpenseNote>): HttpResponse<IExpenseNote> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestExpenseNote[]>): HttpResponse<IExpenseNote[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
