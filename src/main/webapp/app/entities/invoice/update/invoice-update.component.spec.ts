import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IProject } from 'app/entities/project/project.model';
import { ProjectService } from 'app/entities/project/service/project.service';
import { IDocument } from 'app/entities/document/document.model';
import { DocumentService } from 'app/entities/document/service/document.service';
import { IInvoice } from '../invoice.model';
import { InvoiceService } from '../service/invoice.service';
import { InvoiceFormService } from './invoice-form.service';

import { InvoiceUpdateComponent } from './invoice-update.component';

describe('Invoice Management Update Component', () => {
  let comp: InvoiceUpdateComponent;
  let fixture: ComponentFixture<InvoiceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let invoiceFormService: InvoiceFormService;
  let invoiceService: InvoiceService;
  let projectService: ProjectService;
  let documentService: DocumentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, InvoiceUpdateComponent],
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
      .overrideTemplate(InvoiceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InvoiceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    invoiceFormService = TestBed.inject(InvoiceFormService);
    invoiceService = TestBed.inject(InvoiceService);
    projectService = TestBed.inject(ProjectService);
    documentService = TestBed.inject(DocumentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call poject query and add missing value', () => {
      const invoice: IInvoice = { id: 456 };
      const poject: IProject = { id: 7769 };
      invoice.poject = poject;

      const pojectCollection: IProject[] = [{ id: 5832 }];
      jest.spyOn(projectService, 'query').mockReturnValue(of(new HttpResponse({ body: pojectCollection })));
      const expectedCollection: IProject[] = [poject, ...pojectCollection];
      jest.spyOn(projectService, 'addProjectToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      expect(projectService.query).toHaveBeenCalled();
      expect(projectService.addProjectToCollectionIfMissing).toHaveBeenCalledWith(pojectCollection, poject);
      expect(comp.pojectsCollection).toEqual(expectedCollection);
    });

    it('Should call document query and add missing value', () => {
      const invoice: IInvoice = { id: 456 };
      const document: IDocument = { id: 22651 };
      invoice.document = document;

      const documentCollection: IDocument[] = [{ id: 12514 }];
      jest.spyOn(documentService, 'query').mockReturnValue(of(new HttpResponse({ body: documentCollection })));
      const expectedCollection: IDocument[] = [document, ...documentCollection];
      jest.spyOn(documentService, 'addDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      expect(documentService.query).toHaveBeenCalled();
      expect(documentService.addDocumentToCollectionIfMissing).toHaveBeenCalledWith(documentCollection, document);
      expect(comp.documentsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const invoice: IInvoice = { id: 456 };
      const poject: IProject = { id: 9111 };
      invoice.poject = poject;
      const document: IDocument = { id: 16755 };
      invoice.document = document;

      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      expect(comp.pojectsCollection).toContain(poject);
      expect(comp.documentsCollection).toContain(document);
      expect(comp.invoice).toEqual(invoice);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoice>>();
      const invoice = { id: 123 };
      jest.spyOn(invoiceFormService, 'getInvoice').mockReturnValue(invoice);
      jest.spyOn(invoiceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: invoice }));
      saveSubject.complete();

      // THEN
      expect(invoiceFormService.getInvoice).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(invoiceService.update).toHaveBeenCalledWith(expect.objectContaining(invoice));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoice>>();
      const invoice = { id: 123 };
      jest.spyOn(invoiceFormService, 'getInvoice').mockReturnValue({ id: null });
      jest.spyOn(invoiceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoice: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: invoice }));
      saveSubject.complete();

      // THEN
      expect(invoiceFormService.getInvoice).toHaveBeenCalled();
      expect(invoiceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoice>>();
      const invoice = { id: 123 };
      jest.spyOn(invoiceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(invoiceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProject', () => {
      it('Should forward to projectService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(projectService, 'compareProject');
        comp.compareProject(entity, entity2);
        expect(projectService.compareProject).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDocument', () => {
      it('Should forward to documentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(documentService, 'compareDocument');
        comp.compareDocument(entity, entity2);
        expect(documentService.compareDocument).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
