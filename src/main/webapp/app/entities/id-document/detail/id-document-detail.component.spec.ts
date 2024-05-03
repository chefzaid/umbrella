import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { IdDocumentDetailComponent } from './id-document-detail.component';

describe('IdDocument Management Detail Component', () => {
  let comp: IdDocumentDetailComponent;
  let fixture: ComponentFixture<IdDocumentDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IdDocumentDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: IdDocumentDetailComponent,
              resolve: { idDocument: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(IdDocumentDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IdDocumentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load idDocument on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', IdDocumentDetailComponent);

      // THEN
      expect(instance.idDocument()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
