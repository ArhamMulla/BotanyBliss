import { BrowserRouter, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import Home from "./pages/Home";
import Plants from "./pages/Plants";
import PlantDetails from "./pages/PlantDetails";
import Login from "./pages/Login";
import Register from "./pages/Register";
import NewContribution from "./pages/NewContribution";
import MyContributions from "./pages/MyContributions";
import AdminDashboard from "./pages/AdminDashboard";
import ManagePlants from "./pages/ManagePlants";
import ProtectedRoute from "./components/ProtectedRoutes";

function App() {
  return (
    <BrowserRouter>
      <Navbar />

      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/plants" element={<Plants />} />
        <Route path="/plants/:id" element={<PlantDetails />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route
  path="/contributions/new"
  element={
    <ProtectedRoute allowedRoles={["USER"]}>
      <NewContribution />
    </ProtectedRoute>
  }
/>

<Route
  path="/contributions/my"
  element={
    <ProtectedRoute allowedRoles={["USER"]}>
      <MyContributions />
    </ProtectedRoute>
  }
/>
        <Route
  path="/admin"
  element={
    <ProtectedRoute allowedRoles={["ADMIN"]}>
      <AdminDashboard />
    </ProtectedRoute>
  }
/>

<Route
  path="/admin/plants"
  element={
    <ProtectedRoute allowedRoles={["ADMIN"]}>
      <ManagePlants />
    </ProtectedRoute>
  }
/>
      </Routes>
    </BrowserRouter>
  );
}

export default App;