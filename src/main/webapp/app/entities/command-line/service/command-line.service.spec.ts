import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ICommandLine } from '../command-line.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../command-line.test-samples';

import { CommandLineService } from './command-line.service';

const requireRestSample: ICommandLine = {
  ...sampleWithRequiredData,
};

describe('CommandLine Service', () => {
  let service: CommandLineService;
  let httpMock: HttpTestingController;
  let expectedResult: ICommandLine | ICommandLine[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CommandLineService);
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

    it('should create a CommandLine', () => {
      const commandLine = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(commandLine).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CommandLine', () => {
      const commandLine = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(commandLine).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CommandLine', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CommandLine', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CommandLine', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCommandLineToCollectionIfMissing', () => {
      it('should add a CommandLine to an empty array', () => {
        const commandLine: ICommandLine = sampleWithRequiredData;
        expectedResult = service.addCommandLineToCollectionIfMissing([], commandLine);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(commandLine);
      });

      it('should not add a CommandLine to an array that contains it', () => {
        const commandLine: ICommandLine = sampleWithRequiredData;
        const commandLineCollection: ICommandLine[] = [
          {
            ...commandLine,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCommandLineToCollectionIfMissing(commandLineCollection, commandLine);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CommandLine to an array that doesn't contain it", () => {
        const commandLine: ICommandLine = sampleWithRequiredData;
        const commandLineCollection: ICommandLine[] = [sampleWithPartialData];
        expectedResult = service.addCommandLineToCollectionIfMissing(commandLineCollection, commandLine);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(commandLine);
      });

      it('should add only unique CommandLine to an array', () => {
        const commandLineArray: ICommandLine[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const commandLineCollection: ICommandLine[] = [sampleWithRequiredData];
        expectedResult = service.addCommandLineToCollectionIfMissing(commandLineCollection, ...commandLineArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const commandLine: ICommandLine = sampleWithRequiredData;
        const commandLine2: ICommandLine = sampleWithPartialData;
        expectedResult = service.addCommandLineToCollectionIfMissing([], commandLine, commandLine2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(commandLine);
        expect(expectedResult).toContain(commandLine2);
      });

      it('should accept null and undefined values', () => {
        const commandLine: ICommandLine = sampleWithRequiredData;
        expectedResult = service.addCommandLineToCollectionIfMissing([], null, commandLine, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(commandLine);
      });

      it('should return initial array if no CommandLine is added', () => {
        const commandLineCollection: ICommandLine[] = [sampleWithRequiredData];
        expectedResult = service.addCommandLineToCollectionIfMissing(commandLineCollection, undefined, null);
        expect(expectedResult).toEqual(commandLineCollection);
      });
    });

    describe('compareCommandLine', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCommandLine(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCommandLine(entity1, entity2);
        const compareResult2 = service.compareCommandLine(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCommandLine(entity1, entity2);
        const compareResult2 = service.compareCommandLine(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCommandLine(entity1, entity2);
        const compareResult2 = service.compareCommandLine(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
