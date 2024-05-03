import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ParameterGroupService } from '../service/parameter-group.service';
import { IParameterGroup } from '../parameter-group.model';
import { ParameterGroupFormService } from './parameter-group-form.service';

import { ParameterGroupUpdateComponent } from './parameter-group-update.component';

describe('ParameterGroup Management Update Component', () => {
  let comp: ParameterGroupUpdateComponent;
  let fixture: ComponentFixture<ParameterGroupUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let parameterGroupFormService: ParameterGroupFormService;
  let parameterGroupService: ParameterGroupService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ParameterGroupUpdateComponent],
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
      .overrideTemplate(ParameterGroupUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ParameterGroupUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    parameterGroupFormService = TestBed.inject(ParameterGroupFormService);
    parameterGroupService = TestBed.inject(ParameterGroupService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const parameterGroup: IParameterGroup = { id: 456 };

      activatedRoute.data = of({ parameterGroup });
      comp.ngOnInit();

      expect(comp.parameterGroup).toEqual(parameterGroup);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParameterGroup>>();
      const parameterGroup = { id: 123 };
      jest.spyOn(parameterGroupFormService, 'getParameterGroup').mockReturnValue(parameterGroup);
      jest.spyOn(parameterGroupService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parameterGroup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: parameterGroup }));
      saveSubject.complete();

      // THEN
      expect(parameterGroupFormService.getParameterGroup).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(parameterGroupService.update).toHaveBeenCalledWith(expect.objectContaining(parameterGroup));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParameterGroup>>();
      const parameterGroup = { id: 123 };
      jest.spyOn(parameterGroupFormService, 'getParameterGroup').mockReturnValue({ id: null });
      jest.spyOn(parameterGroupService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parameterGroup: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: parameterGroup }));
      saveSubject.complete();

      // THEN
      expect(parameterGroupFormService.getParameterGroup).toHaveBeenCalled();
      expect(parameterGroupService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParameterGroup>>();
      const parameterGroup = { id: 123 };
      jest.spyOn(parameterGroupService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parameterGroup });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(parameterGroupService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
