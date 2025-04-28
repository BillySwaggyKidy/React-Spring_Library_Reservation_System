import { UserContext } from "@/src/app/context/userContext";
import { useContext } from "react";
import ProfilBox from "./profil/ProfilBox";
import LoginButton from "./authButtons/LoginButton";
import SignupButton from "./authButtons/SignupButton";


export default function AuthSection() {
    const userContext = useContext(UserContext);

    return (
        <div className="flex flex-row justify-center items-center w-[15%]">
            {
                userContext?.currentUser ? 
                <ProfilBox username={userContext?.currentUser.username}/> :
                <div className="flex flex-row justify-between items-center m-2">
                    <LoginButton/>
                    <SignupButton/>
                </div>
            }
        </div>
    );
}