import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import { getPlantById } from "../services/plantService";
import "./PlantDetails.css";

function PlantDetails() {
  const { id } = useParams();

  const [plant, setPlant] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchPlant = async () => {
      try {
        const data = await getPlantById(id);
        setPlant(data);
      } catch (err) {
        setError("Plant not found.",err);
      } finally {
        setLoading(false);
      }
    };

    fetchPlant();
  }, [id]);

  if (loading) return <p>Loading plant details...</p>;
  if (error) return <p>{error}</p>;

  return (
    <div className="plant-details-page">
      <Link to="/plants" className="back-link">
        ← Back to Plants
      </Link>

      <div className="plant-details">
        <div className="details-image">
          {plant.imageUrl ? (
            <img src={plant.imageUrl} alt={plant.commonName} />
          ) : (
            <div className="plant-image-placeholder">
              No image available
            </div>
          )}
        </div>

        <div className="details-content">
          <h1>{plant.commonName}</h1>
          <p className="scientific-name">
            {plant.scientificName}
          </p>

          <p className="plant-description">
            {plant.description}
          </p>

          <div className="care-details">
            <h2>Plant Care Information</h2>

            <div className="care-item">
              <h3>☀️ Sunlight</h3>
              <p>{plant.sunlight || "Not specified"}</p>
            </div>

            <div className="care-item">
              <h3>💧 Watering</h3>
              <p>{plant.watering || "Not specified"}</p>
            </div>

            <div className="care-item">
              <h3>🌱 Soil Type</h3>
              <p>{plant.soilType || "Not specified"}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default PlantDetails;