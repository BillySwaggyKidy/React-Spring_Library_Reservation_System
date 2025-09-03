import { lazy } from "react";
import { createBrowserRouter, Navigate } from "react-router-dom";
import { FC } from "react";
import Dashboard from "@/src/routes/home/dashboard/Dashboard";
import BookDetails from "@/src/routes/home/details/BookDetails";
import BookCart from "@/src/routes/home/bookCart/BookCart";
import { bookLoader } from "@/src/loaders/bookLoader";

// Explicitly define the type of the default export as FC (FunctionComponent)
const Home = lazy(() => import("../../routes/home/Home") as Promise<{ default: FC }>);
const Admin = lazy(() => import("../../routes/admin/AdminPage") as Promise<{ default: FC }>);
const Login = lazy(() => import("../../routes/login/LoginPage") as Promise<{ default: FC }>);
const Signup = lazy(() => import("../../routes/signup/SignupPage") as Promise<{ default: FC }>);

export const router = createBrowserRouter([
  { path: "/", element: <Home />, children: [
    {index: true, element: <Navigate to="/books" replace/>},
    {path: "/books", element: <Dashboard/>},
    {path: "/books/:bookId", element: <BookDetails/>, loader: bookLoader},
    {path: "/cart", element: <BookCart/>},
    {path: "/reservations", element: <p>Reservation List</p>},
    {path: "/reservations/:reservationId", element: <p>View Reservation</p>}
  ]},
  { path: "/admin", element: <Admin/> },
  { path: "/signup", element: <Signup /> },
  { path: "/login", element: <Login /> }
]);