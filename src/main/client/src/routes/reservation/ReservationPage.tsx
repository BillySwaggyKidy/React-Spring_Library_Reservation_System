import { UserContext } from "@/src/context/userContext";
import { useContext, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import ReservationSearch from "./reservationSearch/ReservationSearch";

export default function ReservationPage() {
    const userContext = useContext(UserContext);
    const navigate = useNavigate();

    useEffect(() => {
        if (!userContext || ["ROLE_ANONYMOUS","ROLE_CUSTOMER"].includes(userContext.currentUser?.role)) {
            navigate("/"); // Redirect if not an admin
        }
    }, [userContext, navigate]);

    return (
        <div className="flex-1 flex flex-col items-center min-h-0 w-full bg-reservation bg-[length:100%_100%] p-8">
            <ReservationSearch/>
        </div>
    );
}