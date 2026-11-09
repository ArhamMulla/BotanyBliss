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
  const formData = new FormData();

  Object.entries(plantData).forEach(([key, value]) => {
    if (value !== null && value !== undefined && value !== "") {
      formData.append(key, value);
    }
  });

  const response = await axiosInstance.post("/plants", formData);
  return response.data;
};

export const updatePlant = async (id, plantData) => {
  const formData = new FormData();

  Object.entries(plantData).forEach(([key, value]) => {
    if (value !== null && value !== undefined && value !== "") {
      formData.append(key, value);
    }
  });

  const response = await axiosInstance.patch(
    `/plants/${id}`,
    formData
  );

  return response.data;
};

export const deletePlant = async (id) => {
  await axiosInstance.delete(`/plants/${id}`);
};