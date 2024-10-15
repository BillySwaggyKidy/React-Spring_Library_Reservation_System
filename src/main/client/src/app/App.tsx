import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Home from "./routes/home/Home.tsx";
import Login from "./routes/login/Login.tsx";

export default function App() {

  const router = createBrowserRouter([
    { path: "/", element: <Home/> },
    {
      path: '/login', element: <Login/>,
    },
  ]);

  return <RouterProvider router={router} />;
};