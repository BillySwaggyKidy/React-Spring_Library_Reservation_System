import UserTable from "./userTable/UserTable";
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
        <div className="h-full w-full">
            <button className="border-2 border-gray-400 bg-white text-black font-bold text-lg rounded-sm absolute top-0 right-0 m-2 p-2 cursor-pointer" onClick={() => navigate("/")}>Go back</button>
            <div className="w-full h-full bg-admin-bg bg-cover flex flex-col items-center justify-center">
                <h2 className="font-bold text-3xl mb-2">Admin Page</h2>
                <UserTable/>
            </div>
        </div>
    );
}