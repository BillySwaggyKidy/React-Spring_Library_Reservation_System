import { LoaderFunctionArgs } from "react-router-dom";
import { Env } from "../Env";
import { userDataType } from "../types/user";

// load the details of a user
export const userLoader = async ({ params } : LoaderFunctionArgs) => {

    const res = await fetch(`${Env.API_BASE_URL}/api/users/details/${params.userId}`, {
        method: "GET",
        credentials: "include", // IMPORTANT for sending/receiving cookies
    });
    const userDetails : userDataType = await res.json();

    return userDetails;
};