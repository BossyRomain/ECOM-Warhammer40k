import { Injectable } from '@angular/core';
import { CommandLine } from '../model/command-line';

@Injectable({
  providedIn: 'root'
})
export class ClientServiceService {

  constructor() { }

  public currentCart:CommandLine[] = [];

  private connected:boolean = false;

  public isConnected():boolean{
    return this.connected;
  }

  public connect():void{
    this.connected = true;
  }

  public disconnect():void{
    this.connected = false;
  }
}
