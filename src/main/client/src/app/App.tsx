import { Suspense } from "react";
import { RouterProvider } from "react-router-dom";
import { router } from "./routes";
import UserProvider from "../context/userContext";

export default function App() {
  return (
    <UserProvider>
      <Suspense fallback={<div>Loading...</div>}>
        <RouterProvider router={router} />
      </Suspense>
    </UserProvider>
  );
}