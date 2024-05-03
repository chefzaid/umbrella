import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { FormulaService } from '../service/formula.service';
import { IFormula } from '../formula.model';
import { FormulaFormService } from './formula-form.service';

import { FormulaUpdateComponent } from './formula-update.component';

describe('Formula Management Update Component', () => {
  let comp: FormulaUpdateComponent;
  let fixture: ComponentFixture<FormulaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let formulaFormService: FormulaFormService;
  let formulaService: FormulaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, FormulaUpdateComponent],
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
      .overrideTemplate(FormulaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FormulaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    formulaFormService = TestBed.inject(FormulaFormService);
    formulaService = TestBed.inject(FormulaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const formula: IFormula = { id: 456 };

      activatedRoute.data = of({ formula });
      comp.ngOnInit();

      expect(comp.formula).toEqual(formula);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFormula>>();
      const formula = { id: 123 };
      jest.spyOn(formulaFormService, 'getFormula').mockReturnValue(formula);
      jest.spyOn(formulaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formula });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: formula }));
      saveSubject.complete();

      // THEN
      expect(formulaFormService.getFormula).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(formulaService.update).toHaveBeenCalledWith(expect.objectContaining(formula));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFormula>>();
      const formula = { id: 123 };
      jest.spyOn(formulaFormService, 'getFormula').mockReturnValue({ id: null });
      jest.spyOn(formulaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formula: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: formula }));
      saveSubject.complete();

      // THEN
      expect(formulaFormService.getFormula).toHaveBeenCalled();
      expect(formulaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFormula>>();
      const formula = { id: 123 };
      jest.spyOn(formulaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formula });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(formulaService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
