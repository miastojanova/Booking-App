export interface Category {
  id: number;
  name: string;
}

export interface SalonService {
  id: number;
  categoryId: number;
  name: string;
  durationMinutes: number;
}

export interface AvailableSlot {
  startTime: string;
  endTime: string;
}

export interface CreateBooking {
  id: number;
  day: string;
  startTime: string;
  name: string;
  email: string;
  phone: string;
}

export interface Booking {
  id: number;
  serviceId: number;
  serviceName: string;
  clientName: string;
  startAt: string;
  endAt: string;
}

export interface WorkingHours {
  day: string;
  openTime: string | null;
  closeTime: string | null;
  closed: boolean;
}
