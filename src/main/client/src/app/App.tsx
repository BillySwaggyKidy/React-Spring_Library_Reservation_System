import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Home from "./routes/home/Home.tsx";
import Login from "./routes/login/LoginPage.tsx";
import UserProvider from "./context/userContext.tsx";
import Signup from "./routes/signup/SignupPage.tsx";


export default function App() {

  const router = createBrowserRouter([
    { path: "/", element: <Home/> },
    {
      path: '/signup', element: <Signup/>,
    },
    {
      path: '/login', element: <Login/>,
    },
  ]);

  return (
    <UserProvider>
      <RouterProvider router={router} />
    </UserProvider>
  );
};