import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { MileageAllowanceService } from '../service/mileage-allowance.service';
import { IMileageAllowance } from '../mileage-allowance.model';
import { MileageAllowanceFormService } from './mileage-allowance-form.service';

import { MileageAllowanceUpdateComponent } from './mileage-allowance-update.component';

describe('MileageAllowance Management Update Component', () => {
  let comp: MileageAllowanceUpdateComponent;
  let fixture: ComponentFixture<MileageAllowanceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let mileageAllowanceFormService: MileageAllowanceFormService;
  let mileageAllowanceService: MileageAllowanceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, MileageAllowanceUpdateComponent],
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
      .overrideTemplate(MileageAllowanceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MileageAllowanceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    mileageAllowanceFormService = TestBed.inject(MileageAllowanceFormService);
    mileageAllowanceService = TestBed.inject(MileageAllowanceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const mileageAllowance: IMileageAllowance = { id: 456 };

      activatedRoute.data = of({ mileageAllowance });
      comp.ngOnInit();

      expect(comp.mileageAllowance).toEqual(mileageAllowance);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMileageAllowance>>();
      const mileageAllowance = { id: 123 };
      jest.spyOn(mileageAllowanceFormService, 'getMileageAllowance').mockReturnValue(mileageAllowance);
      jest.spyOn(mileageAllowanceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mileageAllowance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mileageAllowance }));
      saveSubject.complete();

      // THEN
      expect(mileageAllowanceFormService.getMileageAllowance).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(mileageAllowanceService.update).toHaveBeenCalledWith(expect.objectContaining(mileageAllowance));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMileageAllowance>>();
      const mileageAllowance = { id: 123 };
      jest.spyOn(mileageAllowanceFormService, 'getMileageAllowance').mockReturnValue({ id: null });
      jest.spyOn(mileageAllowanceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mileageAllowance: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mileageAllowance }));
      saveSubject.complete();

      // THEN
      expect(mileageAllowanceFormService.getMileageAllowance).toHaveBeenCalled();
      expect(mileageAllowanceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMileageAllowance>>();
      const mileageAllowance = { id: 123 };
      jest.spyOn(mileageAllowanceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mileageAllowance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(mileageAllowanceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
