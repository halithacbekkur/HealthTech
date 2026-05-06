export interface Patient {
  id?: number;
  firstName: string;
  lastName: string;
  identityNumber: string;
  dateOfBirth: string;
  gender: string;
  phone: string;
  email: string;
  address: string;
  medicalHistory: string;
  createdAt?: string;
  updatedAt?: string;
}
