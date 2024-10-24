import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAllegiance } from '../allegiance.model';
import { AllegianceService } from '../service/allegiance.service';

@Component({
  standalone: true,
  templateUrl: './allegiance-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AllegianceDeleteDialogComponent {
  allegiance?: IAllegiance;

  protected allegianceService = inject(AllegianceService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.allegianceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
