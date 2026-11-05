import axiosInstance from "./axiosInstance";

export const getAllPlants = async () => {
  const response = await axiosInstance.get("/plants");
  return response.data;
};

export const getPlantById = async (id) => {
  const response = await axiosInstance.get(`/plants/${id}`);
  return response.data;
};

export const createPlant = async (plantData) => {
  const response = await axiosInstance.post("/plants", plantData);
  return response.data;
};

export const updatePlant = async (id, plantData) => {
  const response = await axiosInstance.patch(`/plants/${id}`, plantData);
  return response.data;
};

export const deletePlant = async (id) => {
  await axiosInstance.delete(`/plants/${id}`);
};