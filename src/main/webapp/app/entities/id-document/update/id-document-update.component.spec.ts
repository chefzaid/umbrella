import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IDocument } from 'app/entities/document/document.model';
import { DocumentService } from 'app/entities/document/service/document.service';
import { IdDocumentService } from '../service/id-document.service';
import { IIdDocument } from '../id-document.model';
import { IdDocumentFormService } from './id-document-form.service';

import { IdDocumentUpdateComponent } from './id-document-update.component';

describe('IdDocument Management Update Component', () => {
  let comp: IdDocumentUpdateComponent;
  let fixture: ComponentFixture<IdDocumentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let idDocumentFormService: IdDocumentFormService;
  let idDocumentService: IdDocumentService;
  let documentService: DocumentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, IdDocumentUpdateComponent],
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
      .overrideTemplate(IdDocumentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IdDocumentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    idDocumentFormService = TestBed.inject(IdDocumentFormService);
    idDocumentService = TestBed.inject(IdDocumentService);
    documentService = TestBed.inject(DocumentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call document query and add missing value', () => {
      const idDocument: IIdDocument = { id: 456 };
      const document: IDocument = { id: 27321 };
      idDocument.document = document;

      const documentCollection: IDocument[] = [{ id: 10588 }];
      jest.spyOn(documentService, 'query').mockReturnValue(of(new HttpResponse({ body: documentCollection })));
      const expectedCollection: IDocument[] = [document, ...documentCollection];
      jest.spyOn(documentService, 'addDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ idDocument });
      comp.ngOnInit();

      expect(documentService.query).toHaveBeenCalled();
      expect(documentService.addDocumentToCollectionIfMissing).toHaveBeenCalledWith(documentCollection, document);
      expect(comp.documentsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const idDocument: IIdDocument = { id: 456 };
      const document: IDocument = { id: 24978 };
      idDocument.document = document;

      activatedRoute.data = of({ idDocument });
      comp.ngOnInit();

      expect(comp.documentsCollection).toContain(document);
      expect(comp.idDocument).toEqual(idDocument);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIdDocument>>();
      const idDocument = { id: 123 };
      jest.spyOn(idDocumentFormService, 'getIdDocument').mockReturnValue(idDocument);
      jest.spyOn(idDocumentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ idDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: idDocument }));
      saveSubject.complete();

      // THEN
      expect(idDocumentFormService.getIdDocument).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(idDocumentService.update).toHaveBeenCalledWith(expect.objectContaining(idDocument));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIdDocument>>();
      const idDocument = { id: 123 };
      jest.spyOn(idDocumentFormService, 'getIdDocument').mockReturnValue({ id: null });
      jest.spyOn(idDocumentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ idDocument: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: idDocument }));
      saveSubject.complete();

      // THEN
      expect(idDocumentFormService.getIdDocument).toHaveBeenCalled();
      expect(idDocumentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIdDocument>>();
      const idDocument = { id: 123 };
      jest.spyOn(idDocumentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ idDocument });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(idDocumentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
