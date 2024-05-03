import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IDocument } from 'app/entities/document/document.model';
import { DocumentService } from 'app/entities/document/service/document.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IPaySlip } from '../pay-slip.model';
import { PaySlipService } from '../service/pay-slip.service';
import { PaySlipFormService } from './pay-slip-form.service';

import { PaySlipUpdateComponent } from './pay-slip-update.component';

describe('PaySlip Management Update Component', () => {
  let comp: PaySlipUpdateComponent;
  let fixture: ComponentFixture<PaySlipUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let paySlipFormService: PaySlipFormService;
  let paySlipService: PaySlipService;
  let documentService: DocumentService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, PaySlipUpdateComponent],
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
      .overrideTemplate(PaySlipUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PaySlipUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paySlipFormService = TestBed.inject(PaySlipFormService);
    paySlipService = TestBed.inject(PaySlipService);
    documentService = TestBed.inject(DocumentService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call document query and add missing value', () => {
      const paySlip: IPaySlip = { id: 456 };
      const document: IDocument = { id: 13372 };
      paySlip.document = document;

      const documentCollection: IDocument[] = [{ id: 2201 }];
      jest.spyOn(documentService, 'query').mockReturnValue(of(new HttpResponse({ body: documentCollection })));
      const expectedCollection: IDocument[] = [document, ...documentCollection];
      jest.spyOn(documentService, 'addDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paySlip });
      comp.ngOnInit();

      expect(documentService.query).toHaveBeenCalled();
      expect(documentService.addDocumentToCollectionIfMissing).toHaveBeenCalledWith(documentCollection, document);
      expect(comp.documentsCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const paySlip: IPaySlip = { id: 456 };
      const employee: IEmployee = { id: 15766 };
      paySlip.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 7324 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paySlip });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining),
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const paySlip: IPaySlip = { id: 456 };
      const document: IDocument = { id: 24737 };
      paySlip.document = document;
      const employee: IEmployee = { id: 7751 };
      paySlip.employee = employee;

      activatedRoute.data = of({ paySlip });
      comp.ngOnInit();

      expect(comp.documentsCollection).toContain(document);
      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.paySlip).toEqual(paySlip);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaySlip>>();
      const paySlip = { id: 123 };
      jest.spyOn(paySlipFormService, 'getPaySlip').mockReturnValue(paySlip);
      jest.spyOn(paySlipService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paySlip });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paySlip }));
      saveSubject.complete();

      // THEN
      expect(paySlipFormService.getPaySlip).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(paySlipService.update).toHaveBeenCalledWith(expect.objectContaining(paySlip));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaySlip>>();
      const paySlip = { id: 123 };
      jest.spyOn(paySlipFormService, 'getPaySlip').mockReturnValue({ id: null });
      jest.spyOn(paySlipService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paySlip: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: paySlip }));
      saveSubject.complete();

      // THEN
      expect(paySlipFormService.getPaySlip).toHaveBeenCalled();
      expect(paySlipService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPaySlip>>();
      const paySlip = { id: 123 };
      jest.spyOn(paySlipService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paySlip });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paySlipService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDocument', () => {
      it('Should forward to documentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(documentService, 'compareDocument');
        comp.compareDocument(entity, entity2);
        expect(documentService.compareDocument).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
