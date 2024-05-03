import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProspect, NewProspect } from '../prospect.model';

export type PartialUpdateProspect = Partial<IProspect> & Pick<IProspect, 'id'>;

type RestOf<T extends IProspect | NewProspect> = Omit<T, 'expectedStartDate'> & {
  expectedStartDate?: string | null;
};

export type RestProspect = RestOf<IProspect>;

export type NewRestProspect = RestOf<NewProspect>;

export type PartialUpdateRestProspect = RestOf<PartialUpdateProspect>;

export type EntityResponseType = HttpResponse<IProspect>;
export type EntityArrayResponseType = HttpResponse<IProspect[]>;

@Injectable({ providedIn: 'root' })
export class ProspectService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/prospects');

  create(prospect: NewProspect): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prospect);
    return this.http
      .post<RestProspect>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(prospect: IProspect): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prospect);
    return this.http
      .put<RestProspect>(`${this.resourceUrl}/${this.getProspectIdentifier(prospect)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(prospect: PartialUpdateProspect): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(prospect);
    return this.http
      .patch<RestProspect>(`${this.resourceUrl}/${this.getProspectIdentifier(prospect)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestProspect>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProspect[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProspectIdentifier(prospect: Pick<IProspect, 'id'>): number {
    return prospect.id;
  }

  compareProspect(o1: Pick<IProspect, 'id'> | null, o2: Pick<IProspect, 'id'> | null): boolean {
    return o1 && o2 ? this.getProspectIdentifier(o1) === this.getProspectIdentifier(o2) : o1 === o2;
  }

  addProspectToCollectionIfMissing<Type extends Pick<IProspect, 'id'>>(
    prospectCollection: Type[],
    ...prospectsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const prospects: Type[] = prospectsToCheck.filter(isPresent);
    if (prospects.length > 0) {
      const prospectCollectionIdentifiers = prospectCollection.map(prospectItem => this.getProspectIdentifier(prospectItem));
      const prospectsToAdd = prospects.filter(prospectItem => {
        const prospectIdentifier = this.getProspectIdentifier(prospectItem);
        if (prospectCollectionIdentifiers.includes(prospectIdentifier)) {
          return false;
        }
        prospectCollectionIdentifiers.push(prospectIdentifier);
        return true;
      });
      return [...prospectsToAdd, ...prospectCollection];
    }
    return prospectCollection;
  }

  protected convertDateFromClient<T extends IProspect | NewProspect | PartialUpdateProspect>(prospect: T): RestOf<T> {
    return {
      ...prospect,
      expectedStartDate: prospect.expectedStartDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restProspect: RestProspect): IProspect {
    return {
      ...restProspect,
      expectedStartDate: restProspect.expectedStartDate ? dayjs(restProspect.expectedStartDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestProspect>): HttpResponse<IProspect> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestProspect[]>): HttpResponse<IProspect[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
