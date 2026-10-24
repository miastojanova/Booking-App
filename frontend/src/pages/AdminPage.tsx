import { useState } from 'react';
import { getWorkingHours, updateWorkingHours } from '../api/services';
import type { WorkingHours } from '../api/types';
import './AdminPage.css';

export default function AdminPage() {
  const [date, setDate] = useState(() => new Date().toISOString().slice(0, 10));
  const [schedule, setSchedule] = useState<WorkingHours | null>(null);
  const [openTime, setOpenTime] = useState('09:00');
  const [closeTime, setCloseTime] = useState('17:00');
  const [closed, setClosed] = useState(false);
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const [message, setMessage] = useState('');

  const loadSchedule = async () => {
    setLoading(true);
    setMessage('');
    try {
      const data = await getWorkingHours(date);
      setSchedule(data);
      setOpenTime(data.openTime ?? '09:00');
      setCloseTime(data.closeTime ?? '17:00');
      setClosed(data.closed);
    } catch {
      setMessage('Failed to load schedule.');
    } finally {
      setLoading(false);
    }
  };

  const handleSave = async () => {
    setSaving(true);
    setMessage('');
    try {
      const updated = await updateWorkingHours({
        day: date,
        openTime: closed ? null : openTime,
        closeTime: closed ? null : closeTime,
        closed,
      });
      setSchedule(updated);
      setMessage('Schedule updated successfully!');
    } catch {
      setMessage('Failed to update schedule.');
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="admin-page">
      <h1 className="page-title">Admin</h1>
      <p className="page-subtitle">Manage working hours for each day.</p>

      <div className="card">
        <div className="card-label">SELECT A DAY</div>
        <div className="admin-date-row">
          <input
            type="date"
            className="date-input"
            value={date}
            onChange={(e) => {
              setDate(e.target.value);
              setSchedule(null);
              setMessage('');
            }}
          />
          <button className="btn-secondary" onClick={loadSchedule} disabled={loading}>
            {loading ? 'Loading...' : 'Select'}
          </button>
        </div>
      </div>

      {schedule && (
        <div className="card">
          <div className="card-label">WORKING HOURS — {date}</div>

          <div className="admin-field">
            <label className="admin-label">
              <input
                type="checkbox"
                checked={closed}
                onChange={(e) => setClosed(e.target.checked)}
              />
              <span>Closed for the day</span>
            </label>
          </div>

          {!closed && (
            <div className="admin-times">
              <div className="admin-field">
                <label className="admin-label">Opening time</label>
                <input
                  type="time"
                  className="time-input"
                  value={openTime}
                  onChange={(e) => setOpenTime(e.target.value)}
                />
              </div>
              <div className="admin-field">
                <label className="admin-label">Closing time</label>
                <input
                  type="time"
                  className="time-input"
                  value={closeTime}
                  onChange={(e) => setCloseTime(e.target.value)}
                />
              </div>
            </div>
          )}

          <button className="btn-primary" onClick={handleSave} disabled={saving}>
            {saving ? 'Saving...' : 'Save changes'}
          </button>

          {message && <p className="admin-message">{message}</p>}
        </div>
      )}
    </div>
  );
}
