jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ActivityReportService } from '../service/activity-report.service';

import { ActivityReportDeleteDialogComponent } from './activity-report-delete-dialog.component';

describe('ActivityReport Management Delete Component', () => {
  let comp: ActivityReportDeleteDialogComponent;
  let fixture: ComponentFixture<ActivityReportDeleteDialogComponent>;
  let service: ActivityReportService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ActivityReportDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(ActivityReportDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ActivityReportDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ActivityReportService);
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
