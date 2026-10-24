import { useEffect, useState } from 'react';
import { getAllBookings } from '../api/services';
import type { Booking } from '../api/types';
import './BookingsPage.css';

export default function BookingsPage() {
  const [bookings, setBookings] = useState<Booking[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getAllBookings()
      .then(setBookings)
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  const fmt = (iso: string) => {
    const d = new Date(iso);
    return d.toLocaleString([], {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      hour12: false,
    });
  };

  return (
    <div className="bookings-page">
      <h1 className="page-title">Bookings</h1>
      <p className="page-subtitle">All appointments made so far.</p>

      {loading ? (
        <p className="loading-text">Loading...</p>
      ) : bookings.length === 0 ? (
        <div className="card empty-card">
          <p>No bookings yet.</p>
        </div>
      ) : (
        <div className="bookings-table-wrapper card">
          <table className="bookings-table">
            <thead>
              <tr>
                <th>#</th>
                <th>Client</th>
                <th>Service</th>
                <th>Start</th>
                <th>End</th>
              </tr>
            </thead>
            <tbody>
              {bookings.map((b) => (
                <tr key={b.id}>
                  <td>{b.id}</td>
                  <td>{b.clientName}</td>
                  <td>{b.serviceName}</td>
                  <td>{fmt(b.startAt)}</td>
                  <td>{fmt(b.endAt)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
