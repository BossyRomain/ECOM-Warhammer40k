import { Authority } from "./authority";

export interface User {
    id: number;
    username: string;
    password: string;
    authority: Authority;

}
