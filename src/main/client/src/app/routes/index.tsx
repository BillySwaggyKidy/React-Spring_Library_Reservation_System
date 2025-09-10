import { lazy } from "react";
import { createBrowserRouter, Navigate } from "react-router-dom";
import { FC } from "react";
import Dashboard from "@/src/routes/home/dashboard/Dashboard";
import BookDetails from "@/src/routes/home/dashboard/bookDetails/BookDetails";
import BookCart from "@/src/routes/home/bookCart/BookCart";
import ReservationPage from "@/src/routes/reservation/ReservationPage";
import ReservationDetails from "@/src/routes/reservation/reservationDetails/ReservationDetails";
import AccountSettings from "@/src/routes/settings/accountSettings/AccountSettings";
import { bookLoader, reservationLoader, userLoader } from "@/src/loaders";


// Explicitly define the type of the default export as FC (FunctionComponent)
const Home = lazy(() => import("../../routes/home/Home") as Promise<{ default: FC }>);
const UserSettings = lazy(() => import("../../routes/settings/UserSettingsPage") as Promise<{default: FC}>);
const Admin = lazy(() => import("../../routes/admin/AdminPage") as Promise<{ default: FC }>);
const Login = lazy(() => import("../../routes/login/LoginPage") as Promise<{ default: FC }>);
const Signup = lazy(() => import("../../routes/signup/SignupPage") as Promise<{ default: FC }>);

export const router = createBrowserRouter([
  { path: "/", element: <Home />, children: [
    {index: true, element: <Navigate to="/books" replace/>},
    {path: "/books", element: <Dashboard/>},
    {path: "/books/:bookId", element: <BookDetails/>, loader: bookLoader},
    {path: "/cart", element: <BookCart/>},
    {path: "/reservations", element: <ReservationPage/>},
    {path: "/reservations/:reservationId", element: <ReservationDetails/>, loader: reservationLoader},
    { path: "/admin", element: <Admin/> },
  ]},
  { path: "/account/:userId", element: <UserSettings/>, loader: userLoader, children:[
    {index: true, element: <Navigate to="settings"/>},
    {path: "settings", element: <AccountSettings/> },
  ]},
  { path: "/signup", element: <Signup /> },
  { path: "/login", element: <Login /> }
]);