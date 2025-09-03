import ProfilBox from "./profil/ProfilBox";
import LoginButton from "./authButtons/LoginButton";
import SignupButton from "./authButtons/SignupButton";
import { accountType } from "@/src/types/user";
import BooksCartIcon from "./booksCartIcon/BooksCartIcon";


export default function AuthSection({userData} : {userData: accountType | null | undefined}) {
    return (
        <div className="w-[50%] mr-4">
            {
                userData && userData.role != "ROLE_ANONYMOUS" ? 
                <div className="flex flex-row justify-around items-center m-2">
                    <BooksCartIcon/>
                    <ProfilBox username={userData!.username}/>
                </div> :
                <div className="flex flex-row justify-around items-center m-2">
                    <LoginButton/>
                    <SignupButton/>
                </div>
            }
        </div>
    );
}