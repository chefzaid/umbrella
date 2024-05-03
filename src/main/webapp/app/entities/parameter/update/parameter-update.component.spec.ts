import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';
import { IEnterprise } from 'app/entities/enterprise/enterprise.model';
import { EnterpriseService } from 'app/entities/enterprise/service/enterprise.service';
import { IParameter } from '../parameter.model';
import { ParameterService } from '../service/parameter.service';
import { ParameterFormService } from './parameter-form.service';

import { ParameterUpdateComponent } from './parameter-update.component';

describe('Parameter Management Update Component', () => {
  let comp: ParameterUpdateComponent;
  let fixture: ComponentFixture<ParameterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let parameterFormService: ParameterFormService;
  let parameterService: ParameterService;
  let appUserService: AppUserService;
  let enterpriseService: EnterpriseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ParameterUpdateComponent],
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
      .overrideTemplate(ParameterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ParameterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    parameterFormService = TestBed.inject(ParameterFormService);
    parameterService = TestBed.inject(ParameterService);
    appUserService = TestBed.inject(AppUserService);
    enterpriseService = TestBed.inject(EnterpriseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call AppUser query and add missing value', () => {
      const parameter: IParameter = { id: 456 };
      const appUser: IAppUser = { id: 17387 };
      parameter.appUser = appUser;

      const appUserCollection: IAppUser[] = [{ id: 5666 }];
      jest.spyOn(appUserService, 'query').mockReturnValue(of(new HttpResponse({ body: appUserCollection })));
      const additionalAppUsers = [appUser];
      const expectedCollection: IAppUser[] = [...additionalAppUsers, ...appUserCollection];
      jest.spyOn(appUserService, 'addAppUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ parameter });
      comp.ngOnInit();

      expect(appUserService.query).toHaveBeenCalled();
      expect(appUserService.addAppUserToCollectionIfMissing).toHaveBeenCalledWith(
        appUserCollection,
        ...additionalAppUsers.map(expect.objectContaining),
      );
      expect(comp.appUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Enterprise query and add missing value', () => {
      const parameter: IParameter = { id: 456 };
      const enterprise: IEnterprise = { id: 16754 };
      parameter.enterprise = enterprise;

      const enterpriseCollection: IEnterprise[] = [{ id: 750 }];
      jest.spyOn(enterpriseService, 'query').mockReturnValue(of(new HttpResponse({ body: enterpriseCollection })));
      const additionalEnterprises = [enterprise];
      const expectedCollection: IEnterprise[] = [...additionalEnterprises, ...enterpriseCollection];
      jest.spyOn(enterpriseService, 'addEnterpriseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ parameter });
      comp.ngOnInit();

      expect(enterpriseService.query).toHaveBeenCalled();
      expect(enterpriseService.addEnterpriseToCollectionIfMissing).toHaveBeenCalledWith(
        enterpriseCollection,
        ...additionalEnterprises.map(expect.objectContaining),
      );
      expect(comp.enterprisesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const parameter: IParameter = { id: 456 };
      const appUser: IAppUser = { id: 18791 };
      parameter.appUser = appUser;
      const enterprise: IEnterprise = { id: 9800 };
      parameter.enterprise = enterprise;

      activatedRoute.data = of({ parameter });
      comp.ngOnInit();

      expect(comp.appUsersSharedCollection).toContain(appUser);
      expect(comp.enterprisesSharedCollection).toContain(enterprise);
      expect(comp.parameter).toEqual(parameter);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParameter>>();
      const parameter = { id: 123 };
      jest.spyOn(parameterFormService, 'getParameter').mockReturnValue(parameter);
      jest.spyOn(parameterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parameter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: parameter }));
      saveSubject.complete();

      // THEN
      expect(parameterFormService.getParameter).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(parameterService.update).toHaveBeenCalledWith(expect.objectContaining(parameter));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParameter>>();
      const parameter = { id: 123 };
      jest.spyOn(parameterFormService, 'getParameter').mockReturnValue({ id: null });
      jest.spyOn(parameterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parameter: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: parameter }));
      saveSubject.complete();

      // THEN
      expect(parameterFormService.getParameter).toHaveBeenCalled();
      expect(parameterService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParameter>>();
      const parameter = { id: 123 };
      jest.spyOn(parameterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parameter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(parameterService.update).toHaveBeenCalled();
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
