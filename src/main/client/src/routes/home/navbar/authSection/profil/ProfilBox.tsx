import { UserContext } from "@/src/context/userContext";
import { Env } from "@/src/Env";
import { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";

// represent the user profil with the settings and disconnect button
export default function ProfilBox({username = ""} : {username : string}) {
    const [checked, setChecked] = useState(false); // this state define if the profile button was opened or not
    const userContext = useContext(UserContext);
    const navigate = useNavigate();
    
    const toggleProfileMenu = () => {
        setChecked(!checked);
    };

    const goToUserSettings = () => {
        navigate("/account/" + userContext.currentUser.id);
    }

    const disconnect = async () => {
        const response = await fetch(`${Env.API_BASE_URL}/auth/logout`, {
            method: "POST",
            headers: {
              "Content-Type": "application/x-www-form-urlencoded",
            },
            credentials: "include", // IMPORTANT for sending/receiving cookies
        });
            
        if (response.ok) {
            userContext?.setUserData({
                id: -1,
                username: "",
                role: "ROLE_ANONYMOUS"
            });
            navigate("/");
        } 
        else {
            // Show error
            console.error("Couldn't disconnect you are in big trouble.");
        }
    };

    return (
        <div className="w-full mx-2 relative">
            <div id="profile" className={`flex flex-row justify-center items-center cursor-pointer mt-1 py-1 px-2 bg-slate-400 border-slate-400 border-2 border-solid hover:border-black ${checked ? "rounded-t-md border-black" : "rounded-md"} `} onClick={toggleProfileMenu}>
                <div className="h-10 w-10 rounded-full flex flex-row justify-center items-center bg-teal-300 border-gray-500 border-2">
                    <p className="font-bold text-2xl">{username.charAt(0).toUpperCase()}</p>
                </div>
                <p className="text-white ml-2 cursor-text">{username}</p>
            </div>
            {
                checked &&
                <div className="absolute w-full rounded-b-md bg-gray-700 shadow mr-8 transition-[border-color] border-2 border-black linear delay-75 animate-fade-in-down">
                    <ul className="">
                        <li className="font-medium cursor-pointer hover:bg-gray-500">
                            <a className="flex items-center transform transition-colors duration-200 border-r-4 border-transparent" onClick={goToUserSettings}>
                                Settings
                            </a>
                        </li>
                        <hr className="dark:border-gray-700"></hr>
                        <li className="font-mediu cursor-pointer rounded-b-lg hover:bg-gray-500">
                            <a className="flex items-center transform transition-colors duration-200 border-r-4 border-transparent" onClick={disconnect}>
                                Disconnect
                            </a>
                        </li>
                    </ul>
                </div>
            }
        </div>
    );
}