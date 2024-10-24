import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { Group } from 'app/entities/enumerations/group.model';
import { Faction } from 'app/entities/enumerations/faction.model';
import { IAllegiance } from '../allegiance.model';
import { AllegianceService } from '../service/allegiance.service';
import { AllegianceFormGroup, AllegianceFormService } from './allegiance-form.service';

@Component({
  standalone: true,
  selector: 'jhi-allegiance-update',
  templateUrl: './allegiance-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AllegianceUpdateComponent implements OnInit {
  isSaving = false;
  allegiance: IAllegiance | null = null;
  groupValues = Object.keys(Group);
  factionValues = Object.keys(Faction);

  protected allegianceService = inject(AllegianceService);
  protected allegianceFormService = inject(AllegianceFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AllegianceFormGroup = this.allegianceFormService.createAllegianceFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ allegiance }) => {
      this.allegiance = allegiance;
      if (allegiance) {
        this.updateForm(allegiance);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const allegiance = this.allegianceFormService.getAllegiance(this.editForm);
    if (allegiance.id !== null) {
      this.subscribeToSaveResponse(this.allegianceService.update(allegiance));
    } else {
      this.subscribeToSaveResponse(this.allegianceService.create(allegiance));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAllegiance>>): void {
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

  protected updateForm(allegiance: IAllegiance): void {
    this.allegiance = allegiance;
    this.allegianceFormService.resetForm(this.editForm, allegiance);
  }
}
