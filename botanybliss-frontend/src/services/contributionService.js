import axiosInstance from "./axiosInstance";

export const submitContribution = async (contributionData) => {
  const response = await axiosInstance.post(
    "/contributions",
    contributionData
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
    { remarks }
  );

  return response.data;
};