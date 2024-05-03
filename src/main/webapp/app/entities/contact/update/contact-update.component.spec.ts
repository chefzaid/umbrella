import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IAddress } from 'app/entities/address/address.model';
import { AddressService } from 'app/entities/address/service/address.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { IContact } from '../contact.model';
import { ContactService } from '../service/contact.service';
import { ContactFormService } from './contact-form.service';

import { ContactUpdateComponent } from './contact-update.component';

describe('Contact Management Update Component', () => {
  let comp: ContactUpdateComponent;
  let fixture: ComponentFixture<ContactUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let contactFormService: ContactFormService;
  let contactService: ContactService;
  let addressService: AddressService;
  let clientService: ClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ContactUpdateComponent],
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
      .overrideTemplate(ContactUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ContactUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contactFormService = TestBed.inject(ContactFormService);
    contactService = TestBed.inject(ContactService);
    addressService = TestBed.inject(AddressService);
    clientService = TestBed.inject(ClientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call address query and add missing value', () => {
      const contact: IContact = { id: 456 };
      const address: IAddress = { id: 8795 };
      contact.address = address;

      const addressCollection: IAddress[] = [{ id: 17025 }];
      jest.spyOn(addressService, 'query').mockReturnValue(of(new HttpResponse({ body: addressCollection })));
      const expectedCollection: IAddress[] = [address, ...addressCollection];
      jest.spyOn(addressService, 'addAddressToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contact });
      comp.ngOnInit();

      expect(addressService.query).toHaveBeenCalled();
      expect(addressService.addAddressToCollectionIfMissing).toHaveBeenCalledWith(addressCollection, address);
      expect(comp.addressesCollection).toEqual(expectedCollection);
    });

    it('Should call Client query and add missing value', () => {
      const contact: IContact = { id: 456 };
      const client: IClient = { id: 18852 };
      contact.client = client;

      const clientCollection: IClient[] = [{ id: 17314 }];
      jest.spyOn(clientService, 'query').mockReturnValue(of(new HttpResponse({ body: clientCollection })));
      const additionalClients = [client];
      const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
      jest.spyOn(clientService, 'addClientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contact });
      comp.ngOnInit();

      expect(clientService.query).toHaveBeenCalled();
      expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(
        clientCollection,
        ...additionalClients.map(expect.objectContaining),
      );
      expect(comp.clientsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const contact: IContact = { id: 456 };
      const address: IAddress = { id: 30631 };
      contact.address = address;
      const client: IClient = { id: 25669 };
      contact.client = client;

      activatedRoute.data = of({ contact });
      comp.ngOnInit();

      expect(comp.addressesCollection).toContain(address);
      expect(comp.clientsSharedCollection).toContain(client);
      expect(comp.contact).toEqual(contact);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContact>>();
      const contact = { id: 123 };
      jest.spyOn(contactFormService, 'getContact').mockReturnValue(contact);
      jest.spyOn(contactService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contact });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contact }));
      saveSubject.complete();

      // THEN
      expect(contactFormService.getContact).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contactService.update).toHaveBeenCalledWith(expect.objectContaining(contact));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContact>>();
      const contact = { id: 123 };
      jest.spyOn(contactFormService, 'getContact').mockReturnValue({ id: null });
      jest.spyOn(contactService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contact: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: contact }));
      saveSubject.complete();

      // THEN
      expect(contactFormService.getContact).toHaveBeenCalled();
      expect(contactService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IContact>>();
      const contact = { id: 123 };
      jest.spyOn(contactService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contact });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contactService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAddress', () => {
      it('Should forward to addressService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(addressService, 'compareAddress');
        comp.compareAddress(entity, entity2);
        expect(addressService.compareAddress).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareClient', () => {
      it('Should forward to clientService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(clientService, 'compareClient');
        comp.compareClient(entity, entity2);
        expect(clientService.compareClient).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
