import { useEffect, useState } from "react";
import { getAllPlants } from "../services/plantService";
import "./FeaturedPlants.css";

function FeaturedPlants() {
  const [plants, setPlants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchPlants = async () => {
      try {
        const data = await getAllPlants();
        setPlants(data.slice(0, 4));
      } catch (err) {
        setError("Unable to load plants. Please try again later.");
      } finally {
        setLoading(false);
      }
    };

    fetchPlants();
  }, []);

  return (
    <section className="featured-section">
      <div className="section-heading">
        <p>EXPLORE THE COLLECTION</p>
        <h2>Discover Popular Plants</h2>
        <span>Get to know some wonderful plants and their care needs.</span>
      </div>

      {loading && <p>Loading plants...</p>}

      {error && <p className="error-message">{error}</p>}

      {!loading && !error && plants.length === 0 && (
        <p>No plants available yet.</p>
      )}

      <div className="plant-grid">
        {plants.map((plant) => (
          <div className="plant-card" key={plant.id}>
            {plant.imageUrl ? (
              <img src={plant.imageUrl} alt={plant.commonName} />
            ) : (
              <div className="plant-image-placeholder">
                No image available
              </div>
            )}

            <div className="plant-info">
              <h3>{plant.commonName}</h3>
              <p className="scientific-name">{plant.scientificName}</p>
              <p>{plant.description}</p>
            </div>
          </div>
        ))}
      </div>
    </section>
  );
}

export default FeaturedPlants;