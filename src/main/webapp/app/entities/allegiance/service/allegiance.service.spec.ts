import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAllegiance } from '../allegiance.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../allegiance.test-samples';

import { AllegianceService } from './allegiance.service';

const requireRestSample: IAllegiance = {
  ...sampleWithRequiredData,
};

describe('Allegiance Service', () => {
  let service: AllegianceService;
  let httpMock: HttpTestingController;
  let expectedResult: IAllegiance | IAllegiance[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AllegianceService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Allegiance', () => {
      const allegiance = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(allegiance).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Allegiance', () => {
      const allegiance = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(allegiance).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Allegiance', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Allegiance', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Allegiance', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAllegianceToCollectionIfMissing', () => {
      it('should add a Allegiance to an empty array', () => {
        const allegiance: IAllegiance = sampleWithRequiredData;
        expectedResult = service.addAllegianceToCollectionIfMissing([], allegiance);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(allegiance);
      });

      it('should not add a Allegiance to an array that contains it', () => {
        const allegiance: IAllegiance = sampleWithRequiredData;
        const allegianceCollection: IAllegiance[] = [
          {
            ...allegiance,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAllegianceToCollectionIfMissing(allegianceCollection, allegiance);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Allegiance to an array that doesn't contain it", () => {
        const allegiance: IAllegiance = sampleWithRequiredData;
        const allegianceCollection: IAllegiance[] = [sampleWithPartialData];
        expectedResult = service.addAllegianceToCollectionIfMissing(allegianceCollection, allegiance);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(allegiance);
      });

      it('should add only unique Allegiance to an array', () => {
        const allegianceArray: IAllegiance[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const allegianceCollection: IAllegiance[] = [sampleWithRequiredData];
        expectedResult = service.addAllegianceToCollectionIfMissing(allegianceCollection, ...allegianceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const allegiance: IAllegiance = sampleWithRequiredData;
        const allegiance2: IAllegiance = sampleWithPartialData;
        expectedResult = service.addAllegianceToCollectionIfMissing([], allegiance, allegiance2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(allegiance);
        expect(expectedResult).toContain(allegiance2);
      });

      it('should accept null and undefined values', () => {
        const allegiance: IAllegiance = sampleWithRequiredData;
        expectedResult = service.addAllegianceToCollectionIfMissing([], null, allegiance, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(allegiance);
      });

      it('should return initial array if no Allegiance is added', () => {
        const allegianceCollection: IAllegiance[] = [sampleWithRequiredData];
        expectedResult = service.addAllegianceToCollectionIfMissing(allegianceCollection, undefined, null);
        expect(expectedResult).toEqual(allegianceCollection);
      });
    });

    describe('compareAllegiance', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAllegiance(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAllegiance(entity1, entity2);
        const compareResult2 = service.compareAllegiance(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAllegiance(entity1, entity2);
        const compareResult2 = service.compareAllegiance(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAllegiance(entity1, entity2);
        const compareResult2 = service.compareAllegiance(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
