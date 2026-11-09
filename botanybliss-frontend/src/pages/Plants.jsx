import { useEffect, useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { getAllPlants } from "../services/plantService";
import "./Plants.css";

function Plants() {
  const [plants, setPlants] = useState([]);
  const [searchParams, setSearchParams] = useSearchParams();
const [search, setSearch] = useState(searchParams.get("search") || "");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchPlants = async () => {
      try {
        const data = await getAllPlants();
        setPlants(data);
      } catch (err) {
        setError("Unable to load plants.", err);
      } finally {
        setLoading(false);
      }
    };

    fetchPlants();
  }, []);

  useEffect(() => {
  setSearch(searchParams.get("search") || "");
}, [searchParams]);

  const filteredPlants = plants.filter((plant) =>
    plant.commonName.toLowerCase().includes(search.toLowerCase()) ||
    plant.scientificName.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="plants-page">
      <div className="plants-header">
        <h1>Explore Plants</h1>
        <p>Discover plants and learn more about their care.</p>

        <input
          type="text"
          placeholder="Search by common or scientific name..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
      </div>

      {loading && <p>Loading plants...</p>}
      {error && <p className="error-message">{error}</p>}

      {!loading && !error && (
        <>
          <p className="plant-count">
            Showing {filteredPlants.length} plants
          </p>

          <div className="plant-grid">
            {filteredPlants.map((plant) => (
              <Link
                to={`/plants/${plant.id}`}
                className="plant-card"
                key={plant.id}
              >
                {plant.imageUrl ? (
                  <img src={plant.imageUrl} alt={plant.commonName} />
                ) : (
                  <div className="plant-image-placeholder">
                    No image available
                  </div>
                )}

                <div className="plant-info">
                  <h3>{plant.commonName}</h3>
                  <p className="scientific-name">
                    {plant.scientificName}
                  </p>
                  <p>{plant.description}</p>
                </div>
              </Link>
            ))}
          </div>

          {filteredPlants.length === 0 && (
            <p className="no-plants">No matching plants found.</p>
          )}
        </>
      )}
    </div>
  );
}

export default Plants;