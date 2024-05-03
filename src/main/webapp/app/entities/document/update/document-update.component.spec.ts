import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IParameter } from 'app/entities/parameter/parameter.model';
import { ParameterService } from 'app/entities/parameter/service/parameter.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IEmploymentContract } from 'app/entities/employment-contract/employment-contract.model';
import { EmploymentContractService } from 'app/entities/employment-contract/service/employment-contract.service';
import { IServiceContract } from 'app/entities/service-contract/service-contract.model';
import { ServiceContractService } from 'app/entities/service-contract/service/service-contract.service';
import { IEnterprise } from 'app/entities/enterprise/enterprise.model';
import { EnterpriseService } from 'app/entities/enterprise/service/enterprise.service';
import { IDocument } from '../document.model';
import { DocumentService } from '../service/document.service';
import { DocumentFormService } from './document-form.service';

import { DocumentUpdateComponent } from './document-update.component';

describe('Document Management Update Component', () => {
  let comp: DocumentUpdateComponent;
  let fixture: ComponentFixture<DocumentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentFormService: DocumentFormService;
  let documentService: DocumentService;
  let parameterService: ParameterService;
  let employeeService: EmployeeService;
  let employmentContractService: EmploymentContractService;
  let serviceContractService: ServiceContractService;
  let enterpriseService: EnterpriseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, DocumentUpdateComponent],
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
      .overrideTemplate(DocumentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentFormService = TestBed.inject(DocumentFormService);
    documentService = TestBed.inject(DocumentService);
    parameterService = TestBed.inject(ParameterService);
    employeeService = TestBed.inject(EmployeeService);
    employmentContractService = TestBed.inject(EmploymentContractService);
    serviceContractService = TestBed.inject(ServiceContractService);
    enterpriseService = TestBed.inject(EnterpriseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call documentType query and add missing value', () => {
      const document: IDocument = { id: 456 };
      const documentType: IParameter = { id: 6042 };
      document.documentType = documentType;

      const documentTypeCollection: IParameter[] = [{ id: 17519 }];
      jest.spyOn(parameterService, 'query').mockReturnValue(of(new HttpResponse({ body: documentTypeCollection })));
      const expectedCollection: IParameter[] = [documentType, ...documentTypeCollection];
      jest.spyOn(parameterService, 'addParameterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ document });
      comp.ngOnInit();

      expect(parameterService.query).toHaveBeenCalled();
      expect(parameterService.addParameterToCollectionIfMissing).toHaveBeenCalledWith(documentTypeCollection, documentType);
      expect(comp.documentTypesCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const document: IDocument = { id: 456 };
      const employee: IEmployee = { id: 16570 };
      document.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 24557 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ document });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining),
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call EmploymentContract query and add missing value', () => {
      const document: IDocument = { id: 456 };
      const employmentContract: IEmploymentContract = { id: 31949 };
      document.employmentContract = employmentContract;

      const employmentContractCollection: IEmploymentContract[] = [{ id: 17810 }];
      jest.spyOn(employmentContractService, 'query').mockReturnValue(of(new HttpResponse({ body: employmentContractCollection })));
      const additionalEmploymentContracts = [employmentContract];
      const expectedCollection: IEmploymentContract[] = [...additionalEmploymentContracts, ...employmentContractCollection];
      jest.spyOn(employmentContractService, 'addEmploymentContractToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ document });
      comp.ngOnInit();

      expect(employmentContractService.query).toHaveBeenCalled();
      expect(employmentContractService.addEmploymentContractToCollectionIfMissing).toHaveBeenCalledWith(
        employmentContractCollection,
        ...additionalEmploymentContracts.map(expect.objectContaining),
      );
      expect(comp.employmentContractsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ServiceContract query and add missing value', () => {
      const document: IDocument = { id: 456 };
      const serviceContract: IServiceContract = { id: 24704 };
      document.serviceContract = serviceContract;

      const serviceContractCollection: IServiceContract[] = [{ id: 20795 }];
      jest.spyOn(serviceContractService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceContractCollection })));
      const additionalServiceContracts = [serviceContract];
      const expectedCollection: IServiceContract[] = [...additionalServiceContracts, ...serviceContractCollection];
      jest.spyOn(serviceContractService, 'addServiceContractToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ document });
      comp.ngOnInit();

      expect(serviceContractService.query).toHaveBeenCalled();
      expect(serviceContractService.addServiceContractToCollectionIfMissing).toHaveBeenCalledWith(
        serviceContractCollection,
        ...additionalServiceContracts.map(expect.objectContaining),
      );
      expect(comp.serviceContractsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Enterprise query and add missing value', () => {
      const document: IDocument = { id: 456 };
      const enterprise: IEnterprise = { id: 9474 };
      document.enterprise = enterprise;

      const enterpriseCollection: IEnterprise[] = [{ id: 14021 }];
      jest.spyOn(enterpriseService, 'query').mockReturnValue(of(new HttpResponse({ body: enterpriseCollection })));
      const additionalEnterprises = [enterprise];
      const expectedCollection: IEnterprise[] = [...additionalEnterprises, ...enterpriseCollection];
      jest.spyOn(enterpriseService, 'addEnterpriseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ document });
      comp.ngOnInit();

      expect(enterpriseService.query).toHaveBeenCalled();
      expect(enterpriseService.addEnterpriseToCollectionIfMissing).toHaveBeenCalledWith(
        enterpriseCollection,
        ...additionalEnterprises.map(expect.objectContaining),
      );
      expect(comp.enterprisesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const document: IDocument = { id: 456 };
      const documentType: IParameter = { id: 16501 };
      document.documentType = documentType;
      const employee: IEmployee = { id: 8712 };
      document.employee = employee;
      const employmentContract: IEmploymentContract = { id: 22955 };
      document.employmentContract = employmentContract;
      const serviceContract: IServiceContract = { id: 32527 };
      document.serviceContract = serviceContract;
      const enterprise: IEnterprise = { id: 14215 };
      document.enterprise = enterprise;

      activatedRoute.data = of({ document });
      comp.ngOnInit();

      expect(comp.documentTypesCollection).toContain(documentType);
      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.employmentContractsSharedCollection).toContain(employmentContract);
      expect(comp.serviceContractsSharedCollection).toContain(serviceContract);
      expect(comp.enterprisesSharedCollection).toContain(enterprise);
      expect(comp.document).toEqual(document);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocument>>();
      const document = { id: 123 };
      jest.spyOn(documentFormService, 'getDocument').mockReturnValue(document);
      jest.spyOn(documentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ document });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: document }));
      saveSubject.complete();

      // THEN
      expect(documentFormService.getDocument).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentService.update).toHaveBeenCalledWith(expect.objectContaining(document));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocument>>();
      const document = { id: 123 };
      jest.spyOn(documentFormService, 'getDocument').mockReturnValue({ id: null });
      jest.spyOn(documentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ document: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: document }));
      saveSubject.complete();

      // THEN
      expect(documentFormService.getDocument).toHaveBeenCalled();
      expect(documentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocument>>();
      const document = { id: 123 };
      jest.spyOn(documentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ document });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareParameter', () => {
      it('Should forward to parameterService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(parameterService, 'compareParameter');
        comp.compareParameter(entity, entity2);
        expect(parameterService.compareParameter).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareEmploymentContract', () => {
      it('Should forward to employmentContractService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employmentContractService, 'compareEmploymentContract');
        comp.compareEmploymentContract(entity, entity2);
        expect(employmentContractService.compareEmploymentContract).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareServiceContract', () => {
      it('Should forward to serviceContractService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(serviceContractService, 'compareServiceContract');
        comp.compareServiceContract(entity, entity2);
        expect(serviceContractService.compareServiceContract).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEnterprise', () => {
      it('Should forward to enterpriseService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(enterpriseService, 'compareEnterprise');
        comp.compareEnterprise(entity, entity2);
        expect(enterpriseService.compareEnterprise).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
