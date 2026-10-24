import api from './client';
import type {
  AvailableSlot,
  Booking,
  Category,
  CreateBooking,
  SalonService,
  WorkingHours,
} from './types';

export const getCategories = () =>
  api.get<Category[]>('/categories').then((r) => r.data);

export const getServicesByCategory = (categoryId: number) =>
  api.get<SalonService[]>('/services', { params: { categoryId } }).then((r) => r.data);

export const getAvailableSlots = (serviceId: number, date: string) =>
  api.get<AvailableSlot[]>('/availability', { params: { serviceId, date } }).then((r) => r.data);

export const createBooking = (data: CreateBooking) =>
  api.post<Booking>('/bookings', data).then((r) => r.data);

export const getAllBookings = () =>
  api.get<Booking[]>('/bookings').then((r) => r.data);

export const getWorkingHours = (date: string) =>
  api.get<WorkingHours>('/working-hours', { params: { date } }).then((r) => r.data);

export const getAllSchedules = () =>
  api.get<WorkingHours[]>('/working-hours/all').then((r) => r.data);

export const updateWorkingHours = (data: WorkingHours) =>
  api.put<WorkingHours>('/working-hours', data).then((r) => r.data);
