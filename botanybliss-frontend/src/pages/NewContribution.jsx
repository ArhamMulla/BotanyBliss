import { useEffect, useState } from "react";
import { getAllPlants } from "../services/plantService";
import { submitContribution } from "../services/contributionService";
import "./NewContribution.css";

function NewContribution() {
  const [type, setType] = useState("NEW_PLANT");
  const [plants, setPlants] = useState([]);
  const [plantId, setPlantId] = useState("");
  const [formData, setFormData] = useState({
    commonName: "",
    scientificName: "",
    description: "",
    sunlight: "",
    watering: "",
    soilType: "",
    imageUrl: "",
  });

  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    getAllPlants()
      .then(setPlants)
      .catch(() => setError("Unable to load plants."));
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

    const contributionData = {
      contributionType: type,
      ...(type === "UPDATE_PLANT" && { plantId: Number(plantId) }),
      ...formData,
    };

    try {
      await submitContribution(contributionData);
      setMessage("Contribution submitted successfully!");
      setPlantId("");
      setFormData({
        commonName: "",
        scientificName: "",
        description: "",
        sunlight: "",
        watering: "",
        soilType: "",
        imageUrl: "",
      });
    } catch (err) {
      setError(
        err.response?.data?.error || "Unable to submit contribution."
      );
    }
  };

  return (
    <div className="contribution-page">
      <h2>Submit a Plant Contribution</h2>

      <form onSubmit={handleSubmit}>
        <label>Contribution Type</label>

        <select
          value={type}
          onChange={(e) => {
            setType(e.target.value);
            setMessage("");
            setError("");
          }}
        >
          <option value="NEW_PLANT">Add a New Plant</option>
          <option value="UPDATE_PLANT">Suggest Plant Updates</option>
        </select>

        {type === "UPDATE_PLANT" && (
          <>
            <label>Select Plant</label>
            <select
              value={plantId}
              onChange={(e) => setPlantId(e.target.value)}
              required
            >
              <option value="">Choose a plant</option>
              {plants.map((plant) => (
                <option key={plant.id} value={plant.id}>
                  {plant.commonName}
                </option>
              ))}
            </select>
          </>
        )}

        {type === "NEW_PLANT" && (
          <>
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
          </>
        )}

        <label>Description</label>
        <textarea
          name="description"
          value={formData.description}
          onChange={handleChange}
        />

        <label>Sunlight Requirements</label>
        <input
          name="sunlight"
          value={formData.sunlight}
          onChange={handleChange}
        />

        <label>Watering Requirements</label>
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

        <button type="submit">Submit Contribution</button>
      </form>

      {message && <p>{message}</p>}
      {error && <p>{error}</p>}
    </div>
  );
}

export default NewContribution;