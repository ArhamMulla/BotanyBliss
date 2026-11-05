import { useEffect, useState } from "react";
import {
  getAllPlants,
  createPlant,
  updatePlant,
  deletePlant,
} from "../services/plantService";
import "./ManagePlants.css";

const emptyForm = {
  commonName: "",
  scientificName: "",
  description: "",
  sunlight: "",
  watering: "",
  soilType: "",
  imageUrl: "",
};

function ManagePlants() {
  const [plants, setPlants] = useState([]);
  const [formData, setFormData] = useState(emptyForm);
  const [editingId, setEditingId] = useState(null);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const loadPlants = async () => {
    try {
      const data = await getAllPlants();
      setPlants(data);
    } catch {
      setError("Unable to load plants.");
    }
  };

  useEffect(() => {
    loadPlants();
  }, []);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");
    setError("");

    try {
      if (editingId) {
        await updatePlant(editingId, formData);
        setMessage("Plant updated successfully.");
      } else {
        await createPlant(formData);
        setMessage("Plant added successfully.");
      }

      setFormData(emptyForm);
      setEditingId(null);
      await loadPlants();
    } catch (err) {
      setError(
        err.response?.data?.error || "Unable to save plant."
      );
    }
  };

  const handleEdit = (plant) => {
    setEditingId(plant.id);
    setFormData({
      commonName: plant.commonName || "",
      scientificName: plant.scientificName || "",
      description: plant.description || "",
      sunlight: plant.sunlight || "",
      watering: plant.watering || "",
      soilType: plant.soilType || "",
      imageUrl: plant.imageUrl || "",
    });

    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure you want to delete this plant?")) {
      return;
    }

    setError("");
    setMessage("");

    try {
      await deletePlant(id);
      setMessage("Plant deleted successfully.");
      await loadPlants();
    } catch (err) {
      setError(
        err.response?.data?.error || "Unable to delete plant."
      );
    }
  };

  const handleCancel = () => {
    setEditingId(null);
    setFormData(emptyForm);
    setError("");
    setMessage("");
  };

  return (
    <div className="manage-plants">
      <h2>Manage Plants</h2>

      <form className="plant-form" onSubmit={handleSubmit}>
        <h3>{editingId ? "Update Plant" : "Add New Plant"}</h3>

        <label>Common Name</label>
        <input
          name="commonName"
          value={formData.commonName}
          onChange={handleChange}
          required
        />

        <label>Scientific Name</label>
        <input
          name="scientificName"
          value={formData.scientificName}
          onChange={handleChange}
          required
        />

        <label>Description</label>
        <textarea
          name="description"
          value={formData.description}
          onChange={handleChange}
        />

        <label>Sunlight</label>
        <input
          name="sunlight"
          value={formData.sunlight}
          onChange={handleChange}
        />

        <label>Watering</label>
        <input
          name="watering"
          value={formData.watering}
          onChange={handleChange}
        />

        <label>Soil Type</label>
        <input
          name="soilType"
          value={formData.soilType}
          onChange={handleChange}
        />

        <label>Image URL</label>
        <input
          name="imageUrl"
          value={formData.imageUrl}
          onChange={handleChange}
        />

        <div className="form-actions">
          <button type="submit">
            {editingId ? "Update Plant" : "Add Plant"}
          </button>

          {editingId && (
            <button type="button" className="cancel-btn" onClick={handleCancel}>
              Cancel
            </button>
          )}
        </div>
      </form>

      {message && <p className="success-message">{message}</p>}
      {error && <p className="error-message">{error}</p>}

      <h3>Existing Plants ({plants.length})</h3>

      <div className="managed-plant-list">
        {plants.map((plant) => (
          <div className="managed-plant-card" key={plant.id}>
            <div>
              <h4>{plant.commonName}</h4>
              <p>{plant.scientificName}</p>
            </div>

            <div className="plant-actions">
              <button onClick={() => handleEdit(plant)}>
                Edit
              </button>

              <button
                className="delete-btn"
                onClick={() => handleDelete(plant.id)}
              >
                Delete
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default ManagePlants;