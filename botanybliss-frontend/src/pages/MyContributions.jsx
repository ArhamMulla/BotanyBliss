import { useEffect, useState } from "react";
import { getMyContributions } from "../services/contributionService";
import "./MyContributions.css";
import { getAllPlants } from "../services/plantService";

function MyContributions() {
  const [contributions, setContributions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [plants, setPlants] = useState([]);

  useEffect(() => {
  Promise.all([getMyContributions(), getAllPlants()])
    .then(([contributionData, plantData]) => {
      setContributions(contributionData);
      setPlants(plantData);
    })
    .catch(() => setError("Unable to load your contributions."))
    .finally(() => setLoading(false));
}, []);

  if (loading) return <p>Loading contributions...</p>;
  if (error) return <p>{error}</p>;

  return (
    <div className="my-contributions">
      <h2>My Contributions</h2>

      {contributions.length === 0 ? (
        <p>You haven't submitted any contributions yet.</p>
      ) : (
        <div className="contribution-list">
          {contributions.map((item) => (
            <div className="contribution-card" key={item.id}>
              <h3>
  {item.commonName ||
    plants.find((plant) => Number(plant.id) === Number(item.plantId))
      ?.commonName ||
    `Plant ID: ${item.plantId}`}
</h3>

              <p>
                <strong>Type:</strong>{" "}
                {item.contributionType === "NEW_PLANT"
                  ? "New Plant"
                  : "Plant Update"}
              </p>

              <p>
                <strong>Status:</strong>{" "}
                <span className={`status ${item.status.toLowerCase()}`}>
                  {item.status}
                </span>
              </p>

              
<div className="admin-remarks">
  <strong>Admin Remarks:</strong>
  {item.adminRemarks?.trim() ? (
    <p>{item.adminRemarks}</p>
  ) : item.status === "PENDING" ? (
    <p>Not reviewed yet.</p>
  ) : (
    <p>No remarks provided.</p>
  )}
</div>


              <p>
                <strong>Submitted:</strong>{" "}
                {item.createdAt
                  ? new Date(item.createdAt).toLocaleDateString()
                  : "N/A"}
              </p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default MyContributions;