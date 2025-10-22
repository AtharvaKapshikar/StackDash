export interface AddTask {
    title:string;
    description : string;
    dueDate:Date;
    status:string;
    assignedById:number;
    assignedToId:number
}
