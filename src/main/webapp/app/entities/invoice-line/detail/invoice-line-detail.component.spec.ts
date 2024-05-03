import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { InvoiceLineDetailComponent } from './invoice-line-detail.component';

describe('InvoiceLine Management Detail Component', () => {
  let comp: InvoiceLineDetailComponent;
  let fixture: ComponentFixture<InvoiceLineDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InvoiceLineDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: InvoiceLineDetailComponent,
              resolve: { invoiceLine: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(InvoiceLineDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InvoiceLineDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load invoiceLine on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', InvoiceLineDetailComponent);

      // THEN
      expect(instance.invoiceLine()).toEqual(expect.objectContaining({ id: 123 }));
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
