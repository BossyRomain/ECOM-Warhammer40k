import { Group } from 'app/entities/enumerations/group.model';
import { Faction } from 'app/entities/enumerations/faction.model';

export interface IAllegiance {
  id: number;
  group?: keyof typeof Group | null;
  faction?: keyof typeof Faction | null;
}

export type NewAllegiance = Omit<IAllegiance, 'id'> & { id: null };
