import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IImage } from '../image.model';

@Component({
  standalone: true,
  selector: 'jhi-image-detail',
  templateUrl: './image-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ImageDetailComponent {
  image = input<IImage | null>(null);

  previousState(): void {
    window.history.back();
  }
}
