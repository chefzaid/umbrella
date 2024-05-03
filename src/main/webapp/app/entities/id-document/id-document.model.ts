import { IDocument } from 'app/entities/document/document.model';
import { IdType } from 'app/entities/enumerations/id-type.model';

export interface IIdDocument {
  id: number;
  idType?: keyof typeof IdType | null;
  idNumber?: string | null;
  document?: IDocument | null;
}

export type NewIdDocument = Omit<IIdDocument, 'id'> & { id: null };
