
import { useEffect, useState } from "react";
import {
  getPendingContributions,
  reviewContribution,
} from "../services/contributionService";
import "./AdminDashboard.css";

function AdminDashboard() {
  const [contributions, setContributions] = useState([]);
  const [remarks, setRemarks] = useState({});
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  const loadContributions = async () => {
    try {
      const data = await getPendingContributions();
      setContributions(data);
    } catch {
      setError("Unable to load pending contributions.");
    }
  };

  useEffect(() => {
    loadContributions();
  }, []);

  const handleReview = async (id, action) => {
    setError("");
    setMessage("");

    try {
      await reviewContribution(id, action, remarks[id] || "");

      setMessage(`Contribution ${action}d successfully.`);

      setContributions((previous) =>
        previous.filter((item) => item.id !== id)
      );

      setRemarks((previous) => {
        const updated = { ...previous };
        delete updated[id];
        return updated;
      });
    } catch (err) {
      setError(
        err.response?.data?.error || `Unable to ${action} contribution.`
      );
    }
  };

  return (
    <div className="admin-dashboard">
      <h2>Admin Dashboard</h2>
      <h3>Pending Contributions</h3>

      {message && <p className="admin-success">{message}</p>}
      {error && <p className="admin-error">{error}</p>}

      {contributions.length === 0 ? (
        <p>No pending contributions.</p>
      ) : (
        contributions.map((item) => (
          <div className="admin-card" key={item.id}>
            <h3>
              {item.commonName || `Plant ID: ${item.plantId}`}
            </h3>

            <p><strong>Submitted by:</strong> {item.userName}</p>
            <p><strong>Type:</strong> {item.contributionType}</p>
            <p><strong>Scientific name:</strong> {item.scientificName || "N/A"}</p>
            <p><strong>Description:</strong> {item.description || "N/A"}</p>
            <p><strong>Sunlight:</strong> {item.sunlight || "N/A"}</p>
            <p><strong>Watering:</strong> {item.watering || "N/A"}</p>
            <p><strong>Soil:</strong> {item.soilType || "N/A"}</p>

            {item.imageUrl && (
              <div>
                <strong>Contribution Image:</strong>
                <img
                  src={item.imageUrl}
                  alt={item.commonName || "Plant contribution"}
                  className="contribution-image"
                />
              </div>
            )}

            <label>Admin Remarks</label>
            <textarea
              value={remarks[item.id] || ""}
              onChange={(e) =>
                setRemarks((previous) => ({
                  ...previous,
                  [item.id]: e.target.value,
                }))
              }
              placeholder="Enter remarks (optional for approval, required for rejection)"
            />

            <div className="admin-actions">
              <button
                className="approve-btn"
                onClick={() => handleReview(item.id, "approve")}
              >
                Approve
              </button>

              <button
                className="reject-btn"
                onClick={() => {
                  if (!remarks[item.id]?.trim()) {
                    setError("Please provide a reason for rejection.");
                    return;
                  }
                  handleReview(item.id, "reject");
                }}
              >
                Reject
              </button>
            </div>
          </div>
        ))
      )}
    </div>
  );
}

export default AdminDashboard;
