import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { ITimeSheet } from 'app/entities/time-sheet/time-sheet.model';
import { TimeSheetService } from 'app/entities/time-sheet/service/time-sheet.service';
import { TimeSheetLineService } from '../service/time-sheet-line.service';
import { ITimeSheetLine } from '../time-sheet-line.model';
import { TimeSheetLineFormService } from './time-sheet-line-form.service';

import { TimeSheetLineUpdateComponent } from './time-sheet-line-update.component';

describe('TimeSheetLine Management Update Component', () => {
  let comp: TimeSheetLineUpdateComponent;
  let fixture: ComponentFixture<TimeSheetLineUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let timeSheetLineFormService: TimeSheetLineFormService;
  let timeSheetLineService: TimeSheetLineService;
  let timeSheetService: TimeSheetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, TimeSheetLineUpdateComponent],
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
      .overrideTemplate(TimeSheetLineUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TimeSheetLineUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    timeSheetLineFormService = TestBed.inject(TimeSheetLineFormService);
    timeSheetLineService = TestBed.inject(TimeSheetLineService);
    timeSheetService = TestBed.inject(TimeSheetService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TimeSheet query and add missing value', () => {
      const timeSheetLine: ITimeSheetLine = { id: 456 };
      const timeSheet: ITimeSheet = { id: 28923 };
      timeSheetLine.timeSheet = timeSheet;

      const timeSheetCollection: ITimeSheet[] = [{ id: 8206 }];
      jest.spyOn(timeSheetService, 'query').mockReturnValue(of(new HttpResponse({ body: timeSheetCollection })));
      const additionalTimeSheets = [timeSheet];
      const expectedCollection: ITimeSheet[] = [...additionalTimeSheets, ...timeSheetCollection];
      jest.spyOn(timeSheetService, 'addTimeSheetToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ timeSheetLine });
      comp.ngOnInit();

      expect(timeSheetService.query).toHaveBeenCalled();
      expect(timeSheetService.addTimeSheetToCollectionIfMissing).toHaveBeenCalledWith(
        timeSheetCollection,
        ...additionalTimeSheets.map(expect.objectContaining),
      );
      expect(comp.timeSheetsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const timeSheetLine: ITimeSheetLine = { id: 456 };
      const timeSheet: ITimeSheet = { id: 9328 };
      timeSheetLine.timeSheet = timeSheet;

      activatedRoute.data = of({ timeSheetLine });
      comp.ngOnInit();

      expect(comp.timeSheetsSharedCollection).toContain(timeSheet);
      expect(comp.timeSheetLine).toEqual(timeSheetLine);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITimeSheetLine>>();
      const timeSheetLine = { id: 123 };
      jest.spyOn(timeSheetLineFormService, 'getTimeSheetLine').mockReturnValue(timeSheetLine);
      jest.spyOn(timeSheetLineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ timeSheetLine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: timeSheetLine }));
      saveSubject.complete();

      // THEN
      expect(timeSheetLineFormService.getTimeSheetLine).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(timeSheetLineService.update).toHaveBeenCalledWith(expect.objectContaining(timeSheetLine));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITimeSheetLine>>();
      const timeSheetLine = { id: 123 };
      jest.spyOn(timeSheetLineFormService, 'getTimeSheetLine').mockReturnValue({ id: null });
      jest.spyOn(timeSheetLineService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ timeSheetLine: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: timeSheetLine }));
      saveSubject.complete();

      // THEN
      expect(timeSheetLineFormService.getTimeSheetLine).toHaveBeenCalled();
      expect(timeSheetLineService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITimeSheetLine>>();
      const timeSheetLine = { id: 123 };
      jest.spyOn(timeSheetLineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ timeSheetLine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(timeSheetLineService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTimeSheet', () => {
      it('Should forward to timeSheetService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(timeSheetService, 'compareTimeSheet');
        comp.compareTimeSheet(entity, entity2);
        expect(timeSheetService.compareTimeSheet).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
