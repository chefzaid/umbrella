import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDocument } from 'app/entities/document/document.model';
import { DocumentService } from 'app/entities/document/service/document.service';
import { IdType } from 'app/entities/enumerations/id-type.model';
import { IdDocumentService } from '../service/id-document.service';
import { IIdDocument } from '../id-document.model';
import { IdDocumentFormService, IdDocumentFormGroup } from './id-document-form.service';

@Component({
  standalone: true,
  selector: 'jhi-id-document-update',
  templateUrl: './id-document-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class IdDocumentUpdateComponent implements OnInit {
  isSaving = false;
  idDocument: IIdDocument | null = null;
  idTypeValues = Object.keys(IdType);

  documentsCollection: IDocument[] = [];

  protected idDocumentService = inject(IdDocumentService);
  protected idDocumentFormService = inject(IdDocumentFormService);
  protected documentService = inject(DocumentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: IdDocumentFormGroup = this.idDocumentFormService.createIdDocumentFormGroup();

  compareDocument = (o1: IDocument | null, o2: IDocument | null): boolean => this.documentService.compareDocument(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ idDocument }) => {
      this.idDocument = idDocument;
      if (idDocument) {
        this.updateForm(idDocument);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const idDocument = this.idDocumentFormService.getIdDocument(this.editForm);
    if (idDocument.id !== null) {
      this.subscribeToSaveResponse(this.idDocumentService.update(idDocument));
    } else {
      this.subscribeToSaveResponse(this.idDocumentService.create(idDocument));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIdDocument>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(idDocument: IIdDocument): void {
    this.idDocument = idDocument;
    this.idDocumentFormService.resetForm(this.editForm, idDocument);

    this.documentsCollection = this.documentService.addDocumentToCollectionIfMissing<IDocument>(
      this.documentsCollection,
      idDocument.document,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.documentService
      .query({ filter: 'iddocument-is-null' })
      .pipe(map((res: HttpResponse<IDocument[]>) => res.body ?? []))
      .pipe(
        map((documents: IDocument[]) =>
          this.documentService.addDocumentToCollectionIfMissing<IDocument>(documents, this.idDocument?.document),
        ),
      )
      .subscribe((documents: IDocument[]) => (this.documentsCollection = documents));
  }
}
