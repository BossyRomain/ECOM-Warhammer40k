import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICommandLine } from '../command-line.model';
import { CommandLineService } from '../service/command-line.service';

@Component({
  standalone: true,
  templateUrl: './command-line-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CommandLineDeleteDialogComponent {
  commandLine?: ICommandLine;

  protected commandLineService = inject(CommandLineService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.commandLineService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
