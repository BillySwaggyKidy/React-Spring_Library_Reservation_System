import ProfilBox from "./profil/ProfilBox";
import LoginButton from "./authButtons/LoginButton";
import SignupButton from "./authButtons/SignupButton";
import { accountType } from "@/src/types/user";


export default function AuthSection({userData} : {userData: accountType | null | undefined}) {

    return (
        <div className="w-[50%] mr-4">
            {
                userData?.role != "ROLE_ANONYMOUS" ? 
                <ProfilBox username={userData!.username}/> :
                <div className="flex flex-row justify-around items-center m-2">
                    <LoginButton/>
                    <SignupButton/>
                </div>
            }
        </div>
    );
}