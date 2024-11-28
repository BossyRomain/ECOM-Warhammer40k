import { Cart } from "./cart";
import { User } from "./user";

export interface Client {
    id: number;
    user: User;
    cart: Cart;
    firstName: string;
    lastName: String;
    birthday: Date;
    newsletter: boolean;
    authToken: string;

}
