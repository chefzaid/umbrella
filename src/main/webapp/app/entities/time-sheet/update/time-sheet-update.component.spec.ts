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
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { ITimeSheet } from '../time-sheet.model';
import { TimeSheetService } from '../service/time-sheet.service';
import { TimeSheetFormService } from './time-sheet-form.service';

import { TimeSheetUpdateComponent } from './time-sheet-update.component';

describe('TimeSheet Management Update Component', () => {
  let comp: TimeSheetUpdateComponent;
  let fixture: ComponentFixture<TimeSheetUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let timeSheetFormService: TimeSheetFormService;
  let timeSheetService: TimeSheetService;
  let projectService: ProjectService;
  let documentService: DocumentService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, TimeSheetUpdateComponent],
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
      .overrideTemplate(TimeSheetUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TimeSheetUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    timeSheetFormService = TestBed.inject(TimeSheetFormService);
    timeSheetService = TestBed.inject(TimeSheetService);
    projectService = TestBed.inject(ProjectService);
    documentService = TestBed.inject(DocumentService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call project query and add missing value', () => {
      const timeSheet: ITimeSheet = { id: 456 };
      const project: IProject = { id: 21689 };
      timeSheet.project = project;

      const projectCollection: IProject[] = [{ id: 10450 }];
      jest.spyOn(projectService, 'query').mockReturnValue(of(new HttpResponse({ body: projectCollection })));
      const expectedCollection: IProject[] = [project, ...projectCollection];
      jest.spyOn(projectService, 'addProjectToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ timeSheet });
      comp.ngOnInit();

      expect(projectService.query).toHaveBeenCalled();
      expect(projectService.addProjectToCollectionIfMissing).toHaveBeenCalledWith(projectCollection, project);
      expect(comp.projectsCollection).toEqual(expectedCollection);
    });

    it('Should call document query and add missing value', () => {
      const timeSheet: ITimeSheet = { id: 456 };
      const document: IDocument = { id: 30951 };
      timeSheet.document = document;

      const documentCollection: IDocument[] = [{ id: 24308 }];
      jest.spyOn(documentService, 'query').mockReturnValue(of(new HttpResponse({ body: documentCollection })));
      const expectedCollection: IDocument[] = [document, ...documentCollection];
      jest.spyOn(documentService, 'addDocumentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ timeSheet });
      comp.ngOnInit();

      expect(documentService.query).toHaveBeenCalled();
      expect(documentService.addDocumentToCollectionIfMissing).toHaveBeenCalledWith(documentCollection, document);
      expect(comp.documentsCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const timeSheet: ITimeSheet = { id: 456 };
      const employee: IEmployee = { id: 16443 };
      timeSheet.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 29484 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [employee];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ timeSheet });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining),
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const timeSheet: ITimeSheet = { id: 456 };
      const project: IProject = { id: 23486 };
      timeSheet.project = project;
      const document: IDocument = { id: 2146 };
      timeSheet.document = document;
      const employee: IEmployee = { id: 22115 };
      timeSheet.employee = employee;

      activatedRoute.data = of({ timeSheet });
      comp.ngOnInit();

      expect(comp.projectsCollection).toContain(project);
      expect(comp.documentsCollection).toContain(document);
      expect(comp.employeesSharedCollection).toContain(employee);
      expect(comp.timeSheet).toEqual(timeSheet);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITimeSheet>>();
      const timeSheet = { id: 123 };
      jest.spyOn(timeSheetFormService, 'getTimeSheet').mockReturnValue(timeSheet);
      jest.spyOn(timeSheetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ timeSheet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: timeSheet }));
      saveSubject.complete();

      // THEN
      expect(timeSheetFormService.getTimeSheet).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(timeSheetService.update).toHaveBeenCalledWith(expect.objectContaining(timeSheet));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITimeSheet>>();
      const timeSheet = { id: 123 };
      jest.spyOn(timeSheetFormService, 'getTimeSheet').mockReturnValue({ id: null });
      jest.spyOn(timeSheetService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ timeSheet: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: timeSheet }));
      saveSubject.complete();

      // THEN
      expect(timeSheetFormService.getTimeSheet).toHaveBeenCalled();
      expect(timeSheetService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITimeSheet>>();
      const timeSheet = { id: 123 };
      jest.spyOn(timeSheetService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ timeSheet });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(timeSheetService.update).toHaveBeenCalled();
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

    describe('compareEmployee', () => {
      it('Should forward to employeeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(employeeService, 'compareEmployee');
        comp.compareEmployee(entity, entity2);
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
