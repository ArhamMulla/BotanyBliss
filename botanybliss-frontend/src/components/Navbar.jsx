import { Link, useNavigate } from "react-router-dom";
import "./Navbar.css";

function Navbar() {
  const navigate = useNavigate();

  const user = JSON.parse(localStorage.getItem("user"));

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    navigate("/login");
    window.location.reload();
  };

  return (
    <nav className="navbar">
      <Link to="/" className="logo">BotanyBliss</Link>

      <div className="nav-links">
        <Link to="/">Home</Link>
        <Link to="/plants">Explore Plants</Link>

        {user ? (
          <>
            <Link to="/contributions/new">Submit Contribution</Link>
            <Link to="/contributions/my">My Contributions</Link>
            <span>Hi, {user.name}</span>
            <button onClick={handleLogout}>Logout</button>
          </>
        ) : (
          <>
            <Link to="/login">Login</Link>
            <Link to="/register" className="register-btn">Sign Up</Link>
          </>
        )}

        {user?.role === "ADMIN" && (
  <>
    <Link to="/admin">Admin Dashboard</Link>
    <Link to="/admin/plants">Manage Plants</Link>
  </>
)}
      </div>
    </nav>
  );
}

export default Navbar;