
import axiosInstance from "./axiosInstance";

export const submitContribution = async (contributionData) => {
  const formData = new FormData();

  Object.entries(contributionData).forEach(([key, value]) => {
    if (value !== null && value !== undefined && value !== "") {
      formData.append(key, value);
    }
  });

  const response = await axiosInstance.post(
    "/contributions",
    formData
  );

  return response.data;
};

export const getMyContributions = async () => {
  const response = await axiosInstance.get("/contributions/my");
  return response.data;
};

export const getPendingContributions = async () => {
  const response = await axiosInstance.get("/contributions/pending");
  return response.data;
};

export const reviewContribution = async (id, action, remarks) => {
  const response = await axiosInstance.post(
    `/contributions/${id}/${action}`,
    { adminRemarks: remarks }
  );
  return response.data;
};