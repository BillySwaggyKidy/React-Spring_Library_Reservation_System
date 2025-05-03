import { Link, useLocation } from "react-router-dom";

export default function NavLinks({role = ""}:{role:string|undefined}) {
    const location = useLocation();
    const { pathname } = location;
    const linkList: {to:string, text:string}[] = [
        {to:"/",text:"Home"},
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
                    <Link className={`font-bold ${pathname == link.to ? "text-blue-600" : "text-blue-300"} underline`} key={link.text} to={link.to}>{link.text}</Link>
                )
            }
        </>
    );
}