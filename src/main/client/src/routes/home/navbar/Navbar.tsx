import { useContext } from "react";
import AuthSection from "./authSection/AuthSection";
import { UserContext } from "@/src/context/userContext";
import NavLinks from "./authSection/navLinks/NavLinks";

export default function Navbar() {
    const userContext = useContext(UserContext);

    return (
        <div className="w-full h-[10%] bg-white flex flex-row items-center mb-2">
            <div className="w-1/4 flex flex-row justify-start items-center pl-2">
                <h2 className="font-bold text-xl text-center text-black">My Library</h2>
            </div>
            <div className="w-2/4 flex flex-row justify-around items-center">
                <NavLinks role={userContext?.currentUser?.role}/>
            </div>
            <div className="w-1/4 flex flex-row justify-end items-center">
                <AuthSection userData={userContext?.currentUser}/>
            </div>
        </div>
    );

};