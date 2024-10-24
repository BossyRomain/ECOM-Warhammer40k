import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ICommandLine } from '../command-line.model';

@Component({
  standalone: true,
  selector: 'jhi-command-line-detail',
  templateUrl: './command-line-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CommandLineDetailComponent {
  commandLine = input<ICommandLine | null>(null);

  previousState(): void {
    window.history.back();
  }
}
