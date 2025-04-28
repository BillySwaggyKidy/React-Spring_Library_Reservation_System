import { useNavigate } from "react-router-dom";


export default function LoginButton() {
    const navigate = useNavigate();


    return (
        <button className="m-2 p-2 bg-violet-600 hover:bg-violet-400 active:bg-purple-500 transition-colors text-white font-bold border-2 border-solid border-violet-800 rounded-md" onClick={() => navigate("/login")}>
            Login
        </button>
    );
}