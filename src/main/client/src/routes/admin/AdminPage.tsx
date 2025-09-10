import UserSearch from "./userSearch/UserSearch";
import { useContext, useEffect } from "react";
import { UserContext } from "@/src/context/userContext";
import { useNavigate } from "react-router-dom";


export default function AdminPage() {
    const userContext = useContext(UserContext);
    const navigate = useNavigate();

    useEffect(() => {
        if (!userContext || userContext.currentUser?.role !== "ROLE_ADMIN") {
            navigate("/"); // Redirect if not an admin
        }
    }, [userContext, navigate]);

    return (
        <div className="flex-1 flex flex-col items-center min-h-0 w-full bg-admin bg-[length:100%_100%] p-8">
            <UserSearch/>
        </div>
    );
}