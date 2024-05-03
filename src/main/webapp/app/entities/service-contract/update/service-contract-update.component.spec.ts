import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { ServiceContractService } from '../service/service-contract.service';
import { IServiceContract } from '../service-contract.model';
import { ServiceContractFormService } from './service-contract-form.service';

import { ServiceContractUpdateComponent } from './service-contract-update.component';

describe('ServiceContract Management Update Component', () => {
  let comp: ServiceContractUpdateComponent;
  let fixture: ComponentFixture<ServiceContractUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let serviceContractFormService: ServiceContractFormService;
  let serviceContractService: ServiceContractService;
  let clientService: ClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ServiceContractUpdateComponent],
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
      .overrideTemplate(ServiceContractUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ServiceContractUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    serviceContractFormService = TestBed.inject(ServiceContractFormService);
    serviceContractService = TestBed.inject(ServiceContractService);
    clientService = TestBed.inject(ClientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call employee query and add missing value', () => {
      const serviceContract: IServiceContract = { id: 456 };
      const employee: IClient = { id: 4629 };
      serviceContract.employee = employee;

      const employeeCollection: IClient[] = [{ id: 17945 }];
      jest.spyOn(clientService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const expectedCollection: IClient[] = [employee, ...employeeCollection];
      jest.spyOn(clientService, 'addClientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ serviceContract });
      comp.ngOnInit();

      expect(clientService.query).toHaveBeenCalled();
      expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(employeeCollection, employee);
      expect(comp.employeesCollection).toEqual(expectedCollection);
    });

    it('Should call ServiceContract query and add missing value', () => {
      const serviceContract: IServiceContract = { id: 456 };
      const serviceContract: IServiceContract = { id: 5804 };
      serviceContract.serviceContract = serviceContract;

      const serviceContractCollection: IServiceContract[] = [{ id: 29854 }];
      jest.spyOn(serviceContractService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceContractCollection })));
      const additionalServiceContracts = [serviceContract];
      const expectedCollection: IServiceContract[] = [...additionalServiceContracts, ...serviceContractCollection];
      jest.spyOn(serviceContractService, 'addServiceContractToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ serviceContract });
      comp.ngOnInit();

      expect(serviceContractService.query).toHaveBeenCalled();
      expect(serviceContractService.addServiceContractToCollectionIfMissing).toHaveBeenCalledWith(
        serviceContractCollection,
        ...additionalServiceContracts.map(expect.objectContaining),
      );
      expect(comp.serviceContractsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const serviceContract: IServiceContract = { id: 456 };
      const employee: IClient = { id: 304 };
      serviceContract.employee = employee;
      const serviceContract: IServiceContract = { id: 29743 };
      serviceContract.serviceContract = serviceContract;

      activatedRoute.data = of({ serviceContract });
      comp.ngOnInit();

      expect(comp.employeesCollection).toContain(employee);
      expect(comp.serviceContractsSharedCollection).toContain(serviceContract);
      expect(comp.serviceContract).toEqual(serviceContract);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServiceContract>>();
      const serviceContract = { id: 123 };
      jest.spyOn(serviceContractFormService, 'getServiceContract').mockReturnValue(serviceContract);
      jest.spyOn(serviceContractService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceContract });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: serviceContract }));
      saveSubject.complete();

      // THEN
      expect(serviceContractFormService.getServiceContract).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(serviceContractService.update).toHaveBeenCalledWith(expect.objectContaining(serviceContract));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServiceContract>>();
      const serviceContract = { id: 123 };
      jest.spyOn(serviceContractFormService, 'getServiceContract').mockReturnValue({ id: null });
      jest.spyOn(serviceContractService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceContract: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: serviceContract }));
      saveSubject.complete();

      // THEN
      expect(serviceContractFormService.getServiceContract).toHaveBeenCalled();
      expect(serviceContractService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServiceContract>>();
      const serviceContract = { id: 123 };
      jest.spyOn(serviceContractService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceContract });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(serviceContractService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareClient', () => {
      it('Should forward to clientService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(clientService, 'compareClient');
        comp.compareClient(entity, entity2);
        expect(clientService.compareClient).toHaveBeenCalledWith(entity, entity2);
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
  });
});
