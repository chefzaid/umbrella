import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';
import { IAddress } from 'app/entities/address/address.model';
import { AddressService } from 'app/entities/address/service/address.service';
import { IEmploymentContract } from 'app/entities/employment-contract/employment-contract.model';
import { EmploymentContractService } from 'app/entities/employment-contract/service/employment-contract.service';
import { IIdDocument } from 'app/entities/id-document/id-document.model';
import { IdDocumentService } from 'app/entities/id-document/service/id-document.service';
import { IWallet } from 'app/entities/wallet/wallet.model';
import { WalletService } from 'app/entities/wallet/service/wallet.service';
import { IEmployee } from '../employee.model';
import { EmployeeService } from '../service/employee.service';
import { EmployeeFormService } from './employee-form.service';

import { EmployeeUpdateComponent } from './employee-update.component';

describe('Employee Management Update Component', () => {
  let comp: EmployeeUpdateComponent;
  let fixture: ComponentFixture<EmployeeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employeeFormService: EmployeeFormService;
  let employeeService: EmployeeService;
  let appUserService: AppUserService;
  let addressService: AddressService;
  let employmentContractService: EmploymentContractService;
  let idDocumentService: IdDocumentService;
  let walletService: WalletService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, EmployeeUpdateComponent],
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
      .overrideTemplate(EmployeeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmployeeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employeeFormService = TestBed.inject(EmployeeFormService);
    employeeService = TestBed.inject(EmployeeService);
    appUserService = TestBed.inject(AppUserService);
    addressService = TestBed.inject(AddressService);
    employmentContractService = TestBed.inject(EmploymentContractService);
    idDocumentService = TestBed.inject(IdDocumentService);
    walletService = TestBed.inject(WalletService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call user query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const user: IAppUser = { id: 12296 };
      employee.user = user;

      const userCollection: IAppUser[] = [{ id: 15444 }];
      jest.spyOn(appUserService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const expectedCollection: IAppUser[] = [user, ...userCollection];
      jest.spyOn(appUserService, 'addAppUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(appUserService.query).toHaveBeenCalled();
      expect(appUserService.addAppUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, user);
      expect(comp.usersCollection).toEqual(expectedCollection);
    });

    it('Should call address query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const address: IAddress = { id: 2693 };
      employee.address = address;

      const addressCollection: IAddress[] = [{ id: 20578 }];
      jest.spyOn(addressService, 'query').mockReturnValue(of(new HttpResponse({ body: addressCollection })));
      const expectedCollection: IAddress[] = [address, ...addressCollection];
      jest.spyOn(addressService, 'addAddressToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(addressService.query).toHaveBeenCalled();
      expect(addressService.addAddressToCollectionIfMissing).toHaveBeenCalledWith(addressCollection, address);
      expect(comp.addressesCollection).toEqual(expectedCollection);
    });

    it('Should call contract query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const contract: IEmploymentContract = { id: 14942 };
      employee.contract = contract;

      const contractCollection: IEmploymentContract[] = [{ id: 5028 }];
      jest.spyOn(employmentContractService, 'query').mockReturnValue(of(new HttpResponse({ body: contractCollection })));
      const expectedCollection: IEmploymentContract[] = [contract, ...contractCollection];
      jest.spyOn(employmentContractService, 'addEmploymentContractToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(employmentContractService.query).toHaveBeenCalled();
      expect(employmentContractService.addEmploymentContractToCollectionIfMissing).toHaveBeenCalledWith(contractCollection, contract);
      expect(comp.contractsCollection).toEqual(expectedCollection);
    });

    it('Should call idDocument query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const idDocument: IIdDocument = { id: 27078 };
      employee.idDocument = idDocument;

      const idDocumentCollection: IIdDocument[] = [{ id: 9876 }];
      jest.spyOn(idDocumentService, 'query').mockReturnValue(of(new HttpResponse({ body: idDocumentCollection })));
      const expectedCollection: IIdDocument[] = [idDocument, ...idDocumentCollection];
      jest.spyOn(idDocumentService, 'addIdDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(idDocumentService.query).toHaveBeenCalled();
      expect(idDocumentService.addIdDocumentToCollectionIfMissing).toHaveBeenCalledWith(idDocumentCollection, idDocument);
      expect(comp.idDocumentsCollection).toEqual(expectedCollection);
    });

    it('Should call wallet query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const wallet: IWallet = { id: 23366 };
      employee.wallet = wallet;

      const walletCollection: IWallet[] = [{ id: 3485 }];
      jest.spyOn(walletService, 'query').mockReturnValue(of(new HttpResponse({ body: walletCollection })));
      const expectedCollection: IWallet[] = [wallet, ...walletCollection];
      jest.spyOn(walletService, 'addWalletToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(walletService.query).toHaveBeenCalled();
      expect(walletService.addWalletToCollectionIfMissing).toHaveBeenCalledWith(walletCollection, wallet);
      expect(comp.walletsCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const manager: IEmployee = { id: 20532 };
      employee.manager = manager;

      const employeeCollection: IEmployee[] = [{ id: 2049 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [manager];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining),
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const employee: IEmployee = { id: 456 };
      const user: IAppUser = { id: 31646 };
      employee.user = user;
      const address: IAddress = { id: 12198 };
      employee.address = address;
      const contract: IEmploymentContract = { id: 15202 };
      employee.contract = contract;
      const idDocument: IIdDocument = { id: 1247 };
      employee.idDocument = idDocument;
      const wallet: IWallet = { id: 8470 };
      employee.wallet = wallet;
      const manager: IEmployee = { id: 3859 };
      employee.manager = manager;

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(comp.usersCollection).toContain(user);
      expect(comp.addressesCollection).toContain(address);
      expect(comp.contractsCollection).toContain(contract);
      expect(comp.idDocumentsCollection).toContain(idDocument);
      expect(comp.walletsCollection).toContain(wallet);
      expect(comp.employeesSharedCollection).toContain(manager);
      expect(comp.employee).toEqual(employee);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployee>>();
      const employee = { id: 123 };
      jest.spyOn(employeeFormService, 'getEmployee').mockReturnValue(employee);
      jest.spyOn(employeeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employee }));
      saveSubject.complete();

      // THEN
      expect(employeeFormService.getEmployee).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(employeeService.update).toHaveBeenCalledWith(expect.objectContaining(employee));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployee>>();
      const employee = { id: 123 };
      jest.spyOn(employeeFormService, 'getEmployee').mockReturnValue({ id: null });
      jest.spyOn(employeeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employee: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employee }));
      saveSubject.complete();

      // THEN
      expect(employeeFormService.getEmployee).toHaveBeenCalled();
      expect(employeeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployee>>();
      const employee = { id: 123 };
      jest.spyOn(employeeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employeeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAppUser', () => {
      it('Should forward to appUserService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(appUserService, 'compareAppUser');
        comp.compareAppUser(entity, entity2);
        expect(appUserService.compareAppUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareAddress', () => {
      it('Should forward to addressService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(addressService, 'compareAddress');
        comp.compareAddress(entity, entity2);
        expect(addressService.compareAddress).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareIdDocument', () => {
      it('Should forward to idDocumentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(idDocumentService, 'compareIdDocument');
        comp.compareIdDocument(entity, entity2);
        expect(idDocumentService.compareIdDocument).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareWallet', () => {
      it('Should forward to walletService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(walletService, 'compareWallet');
        comp.compareWallet(entity, entity2);
        expect(walletService.compareWallet).toHaveBeenCalledWith(entity, entity2);
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
