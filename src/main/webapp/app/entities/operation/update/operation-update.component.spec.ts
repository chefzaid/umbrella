import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IActivityReport } from 'app/entities/activity-report/activity-report.model';
import { ActivityReportService } from 'app/entities/activity-report/service/activity-report.service';
import { OperationService } from '../service/operation.service';
import { IOperation } from '../operation.model';
import { OperationFormService } from './operation-form.service';

import { OperationUpdateComponent } from './operation-update.component';

describe('Operation Management Update Component', () => {
  let comp: OperationUpdateComponent;
  let fixture: ComponentFixture<OperationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let operationFormService: OperationFormService;
  let operationService: OperationService;
  let activityReportService: ActivityReportService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, OperationUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(OperationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OperationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    operationFormService = TestBed.inject(OperationFormService);
    operationService = TestBed.inject(OperationService);
    activityReportService = TestBed.inject(ActivityReportService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ActivityReport query and add missing value', () => {
      const operation: IOperation = { id: 456 };
      const activityReport: IActivityReport = { id: 31826 };
      operation.activityReport = activityReport;

      const activityReportCollection: IActivityReport[] = [{ id: 26900 }];
      jest.spyOn(activityReportService, 'query').mockReturnValue(of(new HttpResponse({ body: activityReportCollection })));
      const additionalActivityReports = [activityReport];
      const expectedCollection: IActivityReport[] = [...additionalActivityReports, ...activityReportCollection];
      jest.spyOn(activityReportService, 'addActivityReportToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ operation });
      comp.ngOnInit();

      expect(activityReportService.query).toHaveBeenCalled();
      expect(activityReportService.addActivityReportToCollectionIfMissing).toHaveBeenCalledWith(
        activityReportCollection,
        ...additionalActivityReports.map(expect.objectContaining),
      );
      expect(comp.activityReportsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const operation: IOperation = { id: 456 };
      const activityReport: IActivityReport = { id: 1669 };
      operation.activityReport = activityReport;

      activatedRoute.data = of({ operation });
      comp.ngOnInit();

      expect(comp.activityReportsSharedCollection).toContain(activityReport);
      expect(comp.operation).toEqual(operation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOperation>>();
      const operation = { id: 123 };
      jest.spyOn(operationFormService, 'getOperation').mockReturnValue(operation);
      jest.spyOn(operationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: operation }));
      saveSubject.complete();

      // THEN
      expect(operationFormService.getOperation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(operationService.update).toHaveBeenCalledWith(expect.objectContaining(operation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOperation>>();
      const operation = { id: 123 };
      jest.spyOn(operationFormService, 'getOperation').mockReturnValue({ id: null });
      jest.spyOn(operationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: operation }));
      saveSubject.complete();

      // THEN
      expect(operationFormService.getOperation).toHaveBeenCalled();
      expect(operationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOperation>>();
      const operation = { id: 123 };
      jest.spyOn(operationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(operationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareActivityReport', () => {
      it('Should forward to activityReportService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(activityReportService, 'compareActivityReport');
        comp.compareActivityReport(entity, entity2);
        expect(activityReportService.compareActivityReport).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
