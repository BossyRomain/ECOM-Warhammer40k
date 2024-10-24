import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IAllegiance } from '../allegiance.model';

@Component({
  standalone: true,
  selector: 'jhi-allegiance-detail',
  templateUrl: './allegiance-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AllegianceDetailComponent {
  allegiance = input<IAllegiance | null>(null);

  previousState(): void {
    window.history.back();
  }
}
