import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IInvoice } from 'app/entities/invoice/invoice.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';
import { InvoiceLineService } from '../service/invoice-line.service';
import { IInvoiceLine } from '../invoice-line.model';
import { InvoiceLineFormService } from './invoice-line-form.service';

import { InvoiceLineUpdateComponent } from './invoice-line-update.component';

describe('InvoiceLine Management Update Component', () => {
  let comp: InvoiceLineUpdateComponent;
  let fixture: ComponentFixture<InvoiceLineUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let invoiceLineFormService: InvoiceLineFormService;
  let invoiceLineService: InvoiceLineService;
  let invoiceService: InvoiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, InvoiceLineUpdateComponent],
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
      .overrideTemplate(InvoiceLineUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InvoiceLineUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    invoiceLineFormService = TestBed.inject(InvoiceLineFormService);
    invoiceLineService = TestBed.inject(InvoiceLineService);
    invoiceService = TestBed.inject(InvoiceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Invoice query and add missing value', () => {
      const invoiceLine: IInvoiceLine = { id: 456 };
      const invoice: IInvoice = { id: 21821 };
      invoiceLine.invoice = invoice;

      const invoiceCollection: IInvoice[] = [{ id: 10455 }];
      jest.spyOn(invoiceService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceCollection })));
      const additionalInvoices = [invoice];
      const expectedCollection: IInvoice[] = [...additionalInvoices, ...invoiceCollection];
      jest.spyOn(invoiceService, 'addInvoiceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoiceLine });
      comp.ngOnInit();

      expect(invoiceService.query).toHaveBeenCalled();
      expect(invoiceService.addInvoiceToCollectionIfMissing).toHaveBeenCalledWith(
        invoiceCollection,
        ...additionalInvoices.map(expect.objectContaining),
      );
      expect(comp.invoicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const invoiceLine: IInvoiceLine = { id: 456 };
      const invoice: IInvoice = { id: 27442 };
      invoiceLine.invoice = invoice;

      activatedRoute.data = of({ invoiceLine });
      comp.ngOnInit();

      expect(comp.invoicesSharedCollection).toContain(invoice);
      expect(comp.invoiceLine).toEqual(invoiceLine);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoiceLine>>();
      const invoiceLine = { id: 123 };
      jest.spyOn(invoiceLineFormService, 'getInvoiceLine').mockReturnValue(invoiceLine);
      jest.spyOn(invoiceLineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceLine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: invoiceLine }));
      saveSubject.complete();

      // THEN
      expect(invoiceLineFormService.getInvoiceLine).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(invoiceLineService.update).toHaveBeenCalledWith(expect.objectContaining(invoiceLine));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoiceLine>>();
      const invoiceLine = { id: 123 };
      jest.spyOn(invoiceLineFormService, 'getInvoiceLine').mockReturnValue({ id: null });
      jest.spyOn(invoiceLineService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceLine: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: invoiceLine }));
      saveSubject.complete();

      // THEN
      expect(invoiceLineFormService.getInvoiceLine).toHaveBeenCalled();
      expect(invoiceLineService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoiceLine>>();
      const invoiceLine = { id: 123 };
      jest.spyOn(invoiceLineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoiceLine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(invoiceLineService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareInvoice', () => {
      it('Should forward to invoiceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(invoiceService, 'compareInvoice');
        comp.compareInvoice(entity, entity2);
        expect(invoiceService.compareInvoice).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
