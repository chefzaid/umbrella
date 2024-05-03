import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IFormula } from 'app/entities/formula/formula.model';
import { FormulaService } from 'app/entities/formula/service/formula.service';
import { EmploymentContractService } from '../service/employment-contract.service';
import { IEmploymentContract } from '../employment-contract.model';
import { EmploymentContractFormService } from './employment-contract-form.service';

import { EmploymentContractUpdateComponent } from './employment-contract-update.component';

describe('EmploymentContract Management Update Component', () => {
  let comp: EmploymentContractUpdateComponent;
  let fixture: ComponentFixture<EmploymentContractUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employmentContractFormService: EmploymentContractFormService;
  let employmentContractService: EmploymentContractService;
  let formulaService: FormulaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, EmploymentContractUpdateComponent],
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
      .overrideTemplate(EmploymentContractUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmploymentContractUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employmentContractFormService = TestBed.inject(EmploymentContractFormService);
    employmentContractService = TestBed.inject(EmploymentContractService);
    formulaService = TestBed.inject(FormulaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call forumula query and add missing value', () => {
      const employmentContract: IEmploymentContract = { id: 456 };
      const forumula: IFormula = { id: 23469 };
      employmentContract.forumula = forumula;

      const forumulaCollection: IFormula[] = [{ id: 31854 }];
      jest.spyOn(formulaService, 'query').mockReturnValue(of(new HttpResponse({ body: forumulaCollection })));
      const expectedCollection: IFormula[] = [forumula, ...forumulaCollection];
      jest.spyOn(formulaService, 'addFormulaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employmentContract });
      comp.ngOnInit();

      expect(formulaService.query).toHaveBeenCalled();
      expect(formulaService.addFormulaToCollectionIfMissing).toHaveBeenCalledWith(forumulaCollection, forumula);
      expect(comp.forumulasCollection).toEqual(expectedCollection);
    });

    it('Should call EmploymentContract query and add missing value', () => {
      const employmentContract: IEmploymentContract = { id: 456 };
      const employmentContract: IEmploymentContract = { id: 10440 };
      employmentContract.employmentContract = employmentContract;

      const employmentContractCollection: IEmploymentContract[] = [{ id: 21710 }];
      jest.spyOn(employmentContractService, 'query').mockReturnValue(of(new HttpResponse({ body: employmentContractCollection })));
      const additionalEmploymentContracts = [employmentContract];
      const expectedCollection: IEmploymentContract[] = [...additionalEmploymentContracts, ...employmentContractCollection];
      jest.spyOn(employmentContractService, 'addEmploymentContractToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employmentContract });
      comp.ngOnInit();

      expect(employmentContractService.query).toHaveBeenCalled();
      expect(employmentContractService.addEmploymentContractToCollectionIfMissing).toHaveBeenCalledWith(
        employmentContractCollection,
        ...additionalEmploymentContracts.map(expect.objectContaining),
      );
      expect(comp.employmentContractsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const employmentContract: IEmploymentContract = { id: 456 };
      const forumula: IFormula = { id: 31961 };
      employmentContract.forumula = forumula;
      const employmentContract: IEmploymentContract = { id: 4203 };
      employmentContract.employmentContract = employmentContract;

      activatedRoute.data = of({ employmentContract });
      comp.ngOnInit();

      expect(comp.forumulasCollection).toContain(forumula);
      expect(comp.employmentContractsSharedCollection).toContain(employmentContract);
      expect(comp.employmentContract).toEqual(employmentContract);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmploymentContract>>();
      const employmentContract = { id: 123 };
      jest.spyOn(employmentContractFormService, 'getEmploymentContract').mockReturnValue(employmentContract);
      jest.spyOn(employmentContractService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employmentContract });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employmentContract }));
      saveSubject.complete();

      // THEN
      expect(employmentContractFormService.getEmploymentContract).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(employmentContractService.update).toHaveBeenCalledWith(expect.objectContaining(employmentContract));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmploymentContract>>();
      const employmentContract = { id: 123 };
      jest.spyOn(employmentContractFormService, 'getEmploymentContract').mockReturnValue({ id: null });
      jest.spyOn(employmentContractService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employmentContract: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employmentContract }));
      saveSubject.complete();

      // THEN
      expect(employmentContractFormService.getEmploymentContract).toHaveBeenCalled();
      expect(employmentContractService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmploymentContract>>();
      const employmentContract = { id: 123 };
      jest.spyOn(employmentContractService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employmentContract });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employmentContractService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFormula', () => {
      it('Should forward to formulaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(formulaService, 'compareFormula');
        comp.compareFormula(entity, entity2);
        expect(formulaService.compareFormula).toHaveBeenCalledWith(entity, entity2);
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
  });
});
