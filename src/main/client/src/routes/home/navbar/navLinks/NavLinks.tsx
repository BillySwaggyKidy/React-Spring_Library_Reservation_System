import { Link, useLocation } from "react-router-dom";

export default function NavLinks({role = ""}:{role:string|undefined}) {
    const location = useLocation();
    const { pathname } = location;
    const linkList: {to:string, text:string}[] = [
        {to:"/books",text:"Home"},
        {to:"/reservations",text:"Reservations"},
        {to:"/admin",text:"Admin"}
    ];

    const getRoleRank = (role:string) => {
        switch (role) {
            case "ROLE_ADMIN":
                return 3;
            case "ROLE_EMPLOYEE":
                return 2;
            case "ROLE_CUSTOMER":
                return 1;
            default:
                return 1;
        }
    };

    const userRoleRank: number = getRoleRank(role);

    return (
        <>
            {
                linkList.slice(0,userRoleRank).map((link)=>
                    <Link className={`px-4 py-2 rounded-lg font-semibold transition ${pathname === link.to ? "bg-blue-100 text-blue-700" : "text-gray-600 hover:bg-gray-100 hover:text-blue-600"}`} key={link.text} to={link.to}>{link.text}</Link>
                )
            }
        </>
    );
}