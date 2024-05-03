jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { TimeSheetService } from '../service/time-sheet.service';

import { TimeSheetDeleteDialogComponent } from './time-sheet-delete-dialog.component';

describe('TimeSheet Management Delete Component', () => {
  let comp: TimeSheetDeleteDialogComponent;
  let fixture: ComponentFixture<TimeSheetDeleteDialogComponent>;
  let service: TimeSheetService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, TimeSheetDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(TimeSheetDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TimeSheetDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TimeSheetService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
