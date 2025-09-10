import { useContext } from "react";
import AuthSection from "./authSection/AuthSection";
import { UserContext } from "@/src/context/userContext";
import NavLinks from "./navLinks/NavLinks";

export default function Navbar() {
    const userContext = useContext(UserContext);

    return (
        <div className="w-full bg-white shadow-md border-b border-gray-200">
            <div className="max-w-7xl mx-auto h-16 flex flex-row items-center px-6">
                <div className="flex-1 flex items-center">
                    <h2 className="font-bold text-xl text-amber-700 flex items-center gap-2 min-w-0">
                        <span>📚</span> My Library
                    </h2>
                </div>

                <div className="flex-1 flex justify-center gap-x-8">
                    <NavLinks role={userContext?.currentUser?.role}/>
                </div>

                <div className="flex-1 flex justify-end min-w-0">
                    <AuthSection userData={userContext?.currentUser}/>
                </div>
            </div>
        </div>

    );

};