import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IFormula } from 'app/entities/formula/formula.model';
import { FormulaService } from 'app/entities/formula/service/formula.service';
import { ProspectService } from '../service/prospect.service';
import { IProspect } from '../prospect.model';
import { ProspectFormService } from './prospect-form.service';

import { ProspectUpdateComponent } from './prospect-update.component';

describe('Prospect Management Update Component', () => {
  let comp: ProspectUpdateComponent;
  let fixture: ComponentFixture<ProspectUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let prospectFormService: ProspectFormService;
  let prospectService: ProspectService;
  let formulaService: FormulaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ProspectUpdateComponent],
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
      .overrideTemplate(ProspectUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProspectUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    prospectFormService = TestBed.inject(ProspectFormService);
    prospectService = TestBed.inject(ProspectService);
    formulaService = TestBed.inject(FormulaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call formula query and add missing value', () => {
      const prospect: IProspect = { id: 456 };
      const formula: IFormula = { id: 17314 };
      prospect.formula = formula;

      const formulaCollection: IFormula[] = [{ id: 19711 }];
      jest.spyOn(formulaService, 'query').mockReturnValue(of(new HttpResponse({ body: formulaCollection })));
      const expectedCollection: IFormula[] = [formula, ...formulaCollection];
      jest.spyOn(formulaService, 'addFormulaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prospect });
      comp.ngOnInit();

      expect(formulaService.query).toHaveBeenCalled();
      expect(formulaService.addFormulaToCollectionIfMissing).toHaveBeenCalledWith(formulaCollection, formula);
      expect(comp.formulasCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const prospect: IProspect = { id: 456 };
      const formula: IFormula = { id: 10444 };
      prospect.formula = formula;

      activatedRoute.data = of({ prospect });
      comp.ngOnInit();

      expect(comp.formulasCollection).toContain(formula);
      expect(comp.prospect).toEqual(prospect);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProspect>>();
      const prospect = { id: 123 };
      jest.spyOn(prospectFormService, 'getProspect').mockReturnValue(prospect);
      jest.spyOn(prospectService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prospect });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prospect }));
      saveSubject.complete();

      // THEN
      expect(prospectFormService.getProspect).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(prospectService.update).toHaveBeenCalledWith(expect.objectContaining(prospect));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProspect>>();
      const prospect = { id: 123 };
      jest.spyOn(prospectFormService, 'getProspect').mockReturnValue({ id: null });
      jest.spyOn(prospectService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prospect: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: prospect }));
      saveSubject.complete();

      // THEN
      expect(prospectFormService.getProspect).toHaveBeenCalled();
      expect(prospectService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProspect>>();
      const prospect = { id: 123 };
      jest.spyOn(prospectService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prospect });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(prospectService.update).toHaveBeenCalled();
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
  });
});
