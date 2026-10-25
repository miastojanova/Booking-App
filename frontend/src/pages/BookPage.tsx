import { useEffect, useState } from 'react';
import {
  getCategories,
  getServicesByCategory,
  getAvailableSlots,
  createBooking,
} from '../api/services';
import type { Category, SalonService, AvailableSlot } from '../api/types';
import './BookPage.css';

export default function BookPage() {
  const [categories, setCategories] = useState<Category[]>([]);
  const [services, setServices] = useState<SalonService[]>([]);
  const [selectedCategory, setSelectedCategory] = useState<number | null>(null);
  const [selectedService, setSelectedService] = useState<SalonService | null>(null);
  const [date, setDate] = useState(() => new Date().toISOString().slice(0, 10));
  const [slots, setSlots] = useState<AvailableSlot[]>([]);
  const [selectedSlot, setSelectedSlot] = useState<string | null>(null);
  const [fullName, setFullName] = useState('');
  const [phone, setPhone] = useState('');
  const [email, setEmail] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    getCategories().then(setCategories).catch(console.error);
  }, []);

  useEffect(() => {
    if (selectedCategory) {
      getServicesByCategory(selectedCategory).then((s) => {
        setServices(s);
        setSelectedService(null);
        setSlots([]);
        setSelectedSlot(null);
      });
    } else {
      setServices([]);
    }
  }, [selectedCategory]);

  useEffect(() => {
    if (selectedService && date) {
      getAvailableSlots(selectedService.id, date)
        .then((s) => {
          setSlots(s);
          setSelectedSlot(null);
        })
        .catch(() => setSlots([]));
    } else {
      setSlots([]);
    }
  }, [selectedService, date]);

  const formatTime = (isoString: string) => {
    const d = new Date(isoString);
    return d.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', hour12: false });
  };

  const handleSubmit = async () => {
    if (!selectedService || !selectedSlot || !fullName.trim() || !phone.trim() || !email.trim()) {
      setError('Please fill in all fields.');
      return;
    }

    setSubmitting(true);
    setError('');
    try {
      const startDateTime = new Date(selectedSlot);
      const dayStr = `${startDateTime.getFullYear()}-${String(startDateTime.getMonth() + 1).padStart(2, '0')}-${String(startDateTime.getDate()).padStart(2, '0')}`;
      const timeStr = `${String(startDateTime.getHours()).padStart(2, '0')}:${String(startDateTime.getMinutes()).padStart(2, '0')}:00`;

      const result = await createBooking({
        id: selectedService.id,
        day: dayStr,
        startTime: timeStr,
        name: fullName.trim(),
        email: email.trim(),
        phone: phone.trim(),
      });
      setSuccessMessage(result.message || 'Your appointment has been booked successfully.');
      setSuccess(true);
      setSelectedSlot(null);
      setFullName('');
      setPhone('');
      setEmail('');
    } catch (err: unknown) {
      if (err && typeof err === 'object' && 'response' in err) {
        const axiosErr = err as { response?: { data?: { message?: string } } };
        setError(axiosErr.response?.data?.message || 'Booking failed. Please try again.');
      } else {
        setError('Booking failed. Please try again.');
      }
    } finally {
      setSubmitting(false);
    }
  };

  if (success) {
    return (
      <div className="book-page">
        <div className="success-message">
          <h1>Booking confirmed!</h1>
          <p>{successMessage}</p>
          <button className="btn-primary" onClick={() => setSuccess(false)}>
            Book another
          </button>
        </div>
      </div>
    );
  }

  const buttonLabel = selectedSlot
    ? `Book at ${formatTime(selectedSlot)}`
    : 'Choose a time slot';

  return (
    <div className="book-page">
      <h1 className="page-title">Book your visit</h1>
      <p className="page-subtitle">Pick a service, a day and a time that suits you.</p>

      <div className="card">
        <div className="card-label">1 · SERVICE</div>
        <div className="category-buttons">
          {categories.map((cat) => (
            <button
              key={cat.id}
              className={`category-btn ${selectedCategory === cat.id ? 'selected' : ''}`}
              onClick={() => setSelectedCategory(cat.id)}
            >
              {cat.name}
            </button>
          ))}
        </div>
        {services.length > 0 && (
          <div className="service-buttons">
            {services.map((svc) => (
              <button
                key={svc.id}
                className={`service-btn ${selectedService?.id === svc.id ? 'selected' : ''}`}
                onClick={() => setSelectedService(svc)}
              >
                {svc.name}
              </button>
            ))}
          </div>
        )}
      </div>

      <div className="card">
        <div className="card-label">2 · DATE &amp; TIME</div>
        <input
          type="date"
          className="date-input"
          value={date}
          onChange={(e) => setDate(e.target.value)}
        />
        {selectedService && (
          <div className="slot-grid">
            {slots.length === 0 ? (
              <p className="no-slots">No available slots for this day.</p>
            ) : (
              slots.map((slot) => (
                <button
                  key={slot.startTime}
                  className={`slot-btn ${selectedSlot === slot.startTime ? 'selected' : ''}`}
                  onClick={() => setSelectedSlot(slot.startTime)}
                >
                  {formatTime(slot.startTime)}
                </button>
              ))
            )}
          </div>
        )}
      </div>

      <div className="card">
        <div className="card-label">3 · YOUR DETAILS</div>
        <div className="details-row">
          <input
            type="text"
            className="text-input"
            placeholder="Full name"
            value={fullName}
            onChange={(e) => setFullName(e.target.value)}
          />
          <input
            type="tel"
            className="text-input"
            placeholder="Phone"
            value={phone}
            onChange={(e) => setPhone(e.target.value)}
          />
        </div>
        <div className="details-row">
          <input
            type="email"
            className="text-input"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
        </div>
      </div>

      {error && <p className="error-text">{error}</p>}

      <button
        className="btn-primary full-width"
        disabled={!selectedSlot || submitting}
        onClick={handleSubmit}
      >
        {submitting ? 'Booking...' : buttonLabel}
      </button>
    </div>
  );
}
