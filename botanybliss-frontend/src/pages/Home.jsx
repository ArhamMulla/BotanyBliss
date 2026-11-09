import { useState } from "react";
import "./Home.css";
import FeaturedPlants from "../components/FeaturedPlants";
import { useNavigate } from "react-router-dom";

function Home() {
  const [search, setSearch] = useState("");
  const navigate = useNavigate();

  const handleSearch = (e) => {
  e.preventDefault();

  navigate(`/plants?search=${encodeURIComponent(search.trim())}`);
};

  return (
    <div className="home">
      <section className="hero">
        <div className="hero-content">
          <p className="hero-label">WELCOME TO BOTANYBLISS</p>

          <h1>
            Discover the beauty
            <br />
            of the plant world.
          </h1>

          <p className="hero-description">
            Explore plants, learn how to care for them, and grow
            your knowledge one leaf at a time.
          </p>

          <form className="search-form" onSubmit={handleSearch}>
            <input
              type="text"
              placeholder="Search plants by name..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />

            <button type="submit">Search</button>
          </form>
        </div>
      </section>
      <FeaturedPlants />
    </div>
  );
}

export default Home;