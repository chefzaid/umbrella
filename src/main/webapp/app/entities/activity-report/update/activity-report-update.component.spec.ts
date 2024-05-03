import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { ActivityReportService } from '../service/activity-report.service';
import { IActivityReport } from '../activity-report.model';
import { ActivityReportFormService } from './activity-report-form.service';

import { ActivityReportUpdateComponent } from './activity-report-update.component';

describe('ActivityReport Management Update Component', () => {
  let comp: ActivityReportUpdateComponent;
  let fixture: ComponentFixture<ActivityReportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let activityReportFormService: ActivityReportFormService;
  let activityReportService: ActivityReportService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ActivityReportUpdateComponent],
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
      .overrideTemplate(ActivityReportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ActivityReportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    activityReportFormService = TestBed.inject(ActivityReportFormService);
    activityReportService = TestBed.inject(ActivityReportService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const activityReport: IActivityReport = { id: 456 };
      const employee: IEmployee = { id: 5991 };
      activityReport.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 7782 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ activityReport });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining),
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const activityReport: IActivityReport = { id: 456 };
      const employee: IEmployee = { id: 10792 };
      activityReport.employee = employee;

      activatedRoute.data = of({ activityReport });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.activityReport).toEqual(activityReport);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IActivityReport>>();
      const activityReport = { id: 123 };
      jest.spyOn(activityReportFormService, 'getActivityReport').mockReturnValue(activityReport);
      jest.spyOn(activityReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ activityReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: activityReport }));
      saveSubject.complete();

      // THEN
      expect(activityReportFormService.getActivityReport).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(activityReportService.update).toHaveBeenCalledWith(expect.objectContaining(activityReport));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IActivityReport>>();
      const activityReport = { id: 123 };
      jest.spyOn(activityReportFormService, 'getActivityReport').mockReturnValue({ id: null });
      jest.spyOn(activityReportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ activityReport: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: activityReport }));
      saveSubject.complete();

      // THEN
      expect(activityReportFormService.getActivityReport).toHaveBeenCalled();
      expect(activityReportService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IActivityReport>>();
      const activityReport = { id: 123 };
      jest.spyOn(activityReportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ activityReport });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(activityReportService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEmployee', () => {
      it('Should forward to employeeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeeService, 'compareEmployee');
        comp.compareEmployee(entity, entity2);
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
