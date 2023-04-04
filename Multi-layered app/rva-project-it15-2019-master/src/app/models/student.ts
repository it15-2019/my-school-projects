import { Departman } from "./departman";
import { Status } from "./status";

export class Student
{
    id!: number;
    ime!: string;
    prezime!: string;
    brojIndeksa!: string;
    status!: Status;
    departman!: Departman;
}