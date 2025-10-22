export interface SimpleNotification {
   id: number;
  userId: number;
  type: string; // e.g. 'registered', 'verified', 'task', 'password'
  message: string;
  timestamp: Date;
}
