export interface Patient {
    id?: string;
    firstName: string;
    lastName: string;
    email: string;
    phoneNumber: string;
    dateOfBirth: string;
    gender: string;
    address: string;
    emergencyContactName: string;
    emergencyContactPhone: string;
    bloodGroup?: string;
    allergies?: string[];
    medicalHistory?: string[];
    createdAt?: string;
    updatedAt?: string;
}
