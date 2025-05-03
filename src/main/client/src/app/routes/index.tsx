import { lazy } from "react";
import { createBrowserRouter } from "react-router-dom";
import { FC } from "react";

// Explicitly define the type of the default export as FC (FunctionComponent)
const Home = lazy(() => import("../../routes/home/Home") as Promise<{ default: FC }>);
const Admin = lazy(() => import("../../routes/admin/AdminPage") as Promise<{ default: FC }>);
const Login = lazy(() => import("../../routes/login/LoginPage") as Promise<{ default: FC }>);
const Signup = lazy(() => import("../../routes/signup/SignupPage") as Promise<{ default: FC }>);

export const router = createBrowserRouter([
  { path: "/", element: <Home /> },
  { path: "/admin", element: <Admin/> },
  { path: "/signup", element: <Signup /> },
  { path: "/login", element: <Login /> }
]);