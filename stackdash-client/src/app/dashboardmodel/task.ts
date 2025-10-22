export interface Task {

    id:number;
    title:string;
    description : string;
    dueDate:Date;
    status:string;
    assignedById:number;
    assignedToId:number
}
