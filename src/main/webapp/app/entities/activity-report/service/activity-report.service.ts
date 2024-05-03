import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IActivityReport, NewActivityReport } from '../activity-report.model';

export type PartialUpdateActivityReport = Partial<IActivityReport> & Pick<IActivityReport, 'id'>;

type RestOf<T extends IActivityReport | NewActivityReport> = Omit<T, 'month'> & {
  month?: string | null;
};

export type RestActivityReport = RestOf<IActivityReport>;

export type NewRestActivityReport = RestOf<NewActivityReport>;

export type PartialUpdateRestActivityReport = RestOf<PartialUpdateActivityReport>;

export type EntityResponseType = HttpResponse<IActivityReport>;
export type EntityArrayResponseType = HttpResponse<IActivityReport[]>;

@Injectable({ providedIn: 'root' })
export class ActivityReportService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/activity-reports');

  create(activityReport: NewActivityReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(activityReport);
    return this.http
      .post<RestActivityReport>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(activityReport: IActivityReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(activityReport);
    return this.http
      .put<RestActivityReport>(`${this.resourceUrl}/${this.getActivityReportIdentifier(activityReport)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(activityReport: PartialUpdateActivityReport): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(activityReport);
    return this.http
      .patch<RestActivityReport>(`${this.resourceUrl}/${this.getActivityReportIdentifier(activityReport)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestActivityReport>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestActivityReport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getActivityReportIdentifier(activityReport: Pick<IActivityReport, 'id'>): number {
    return activityReport.id;
  }

  compareActivityReport(o1: Pick<IActivityReport, 'id'> | null, o2: Pick<IActivityReport, 'id'> | null): boolean {
    return o1 && o2 ? this.getActivityReportIdentifier(o1) === this.getActivityReportIdentifier(o2) : o1 === o2;
  }

  addActivityReportToCollectionIfMissing<Type extends Pick<IActivityReport, 'id'>>(
    activityReportCollection: Type[],
    ...activityReportsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const activityReports: Type[] = activityReportsToCheck.filter(isPresent);
    if (activityReports.length > 0) {
      const activityReportCollectionIdentifiers = activityReportCollection.map(activityReportItem =>
        this.getActivityReportIdentifier(activityReportItem),
      );
      const activityReportsToAdd = activityReports.filter(activityReportItem => {
        const activityReportIdentifier = this.getActivityReportIdentifier(activityReportItem);
        if (activityReportCollectionIdentifiers.includes(activityReportIdentifier)) {
          return false;
        }
        activityReportCollectionIdentifiers.push(activityReportIdentifier);
        return true;
      });
      return [...activityReportsToAdd, ...activityReportCollection];
    }
    return activityReportCollection;
  }

  protected convertDateFromClient<T extends IActivityReport | NewActivityReport | PartialUpdateActivityReport>(
    activityReport: T,
  ): RestOf<T> {
    return {
      ...activityReport,
      month: activityReport.month?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restActivityReport: RestActivityReport): IActivityReport {
    return {
      ...restActivityReport,
      month: restActivityReport.month ? dayjs(restActivityReport.month) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestActivityReport>): HttpResponse<IActivityReport> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestActivityReport[]>): HttpResponse<IActivityReport[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
