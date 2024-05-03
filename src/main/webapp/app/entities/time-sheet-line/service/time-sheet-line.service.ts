import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITimeSheetLine, NewTimeSheetLine } from '../time-sheet-line.model';

export type PartialUpdateTimeSheetLine = Partial<ITimeSheetLine> & Pick<ITimeSheetLine, 'id'>;

export type EntityResponseType = HttpResponse<ITimeSheetLine>;
export type EntityArrayResponseType = HttpResponse<ITimeSheetLine[]>;

@Injectable({ providedIn: 'root' })
export class TimeSheetLineService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/time-sheet-lines');

  create(timeSheetLine: NewTimeSheetLine): Observable<EntityResponseType> {
    return this.http.post<ITimeSheetLine>(this.resourceUrl, timeSheetLine, { observe: 'response' });
  }

  update(timeSheetLine: ITimeSheetLine): Observable<EntityResponseType> {
    return this.http.put<ITimeSheetLine>(`${this.resourceUrl}/${this.getTimeSheetLineIdentifier(timeSheetLine)}`, timeSheetLine, {
      observe: 'response',
    });
  }

  partialUpdate(timeSheetLine: PartialUpdateTimeSheetLine): Observable<EntityResponseType> {
    return this.http.patch<ITimeSheetLine>(`${this.resourceUrl}/${this.getTimeSheetLineIdentifier(timeSheetLine)}`, timeSheetLine, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITimeSheetLine>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITimeSheetLine[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTimeSheetLineIdentifier(timeSheetLine: Pick<ITimeSheetLine, 'id'>): number {
    return timeSheetLine.id;
  }

  compareTimeSheetLine(o1: Pick<ITimeSheetLine, 'id'> | null, o2: Pick<ITimeSheetLine, 'id'> | null): boolean {
    return o1 && o2 ? this.getTimeSheetLineIdentifier(o1) === this.getTimeSheetLineIdentifier(o2) : o1 === o2;
  }

  addTimeSheetLineToCollectionIfMissing<Type extends Pick<ITimeSheetLine, 'id'>>(
    timeSheetLineCollection: Type[],
    ...timeSheetLinesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const timeSheetLines: Type[] = timeSheetLinesToCheck.filter(isPresent);
    if (timeSheetLines.length > 0) {
      const timeSheetLineCollectionIdentifiers = timeSheetLineCollection.map(timeSheetLineItem =>
        this.getTimeSheetLineIdentifier(timeSheetLineItem),
      );
      const timeSheetLinesToAdd = timeSheetLines.filter(timeSheetLineItem => {
        const timeSheetLineIdentifier = this.getTimeSheetLineIdentifier(timeSheetLineItem);
        if (timeSheetLineCollectionIdentifiers.includes(timeSheetLineIdentifier)) {
          return false;
        }
        timeSheetLineCollectionIdentifiers.push(timeSheetLineIdentifier);
        return true;
      });
      return [...timeSheetLinesToAdd, ...timeSheetLineCollection];
    }
    return timeSheetLineCollection;
  }
}
