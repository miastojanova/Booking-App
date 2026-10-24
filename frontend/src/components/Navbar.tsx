import { NavLink } from 'react-router-dom';
import './Navbar.css';

export default function Navbar() {
  return (
    <nav className="navbar">
      <span className="navbar-brand">My Beauty Salon</span>
      <div className="navbar-links">
        <NavLink to="/" className={({ isActive }) => (isActive ? 'nav-link active' : 'nav-link')}>
          Book
        </NavLink>
        <NavLink to="/bookings" className={({ isActive }) => (isActive ? 'nav-link active' : 'nav-link')}>
          Bookings
        </NavLink>
        <NavLink to="/admin" className={({ isActive }) => (isActive ? 'nav-link active' : 'nav-link')}>
          Admin
        </NavLink>
      </div>
    </nav>
  );
}
