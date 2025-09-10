import { LoaderFunctionArgs } from "react-router-dom";
import { Env } from "../Env";
import { reservationDetailsType } from "../types/reservation";

// load the details of a reservation
export const reservationLoader = async ({ params } : LoaderFunctionArgs) => {

    const res = await fetch(`${Env.API_BASE_URL}/api/reservations/view/${params.reservationId}`, {
        method: "GET",
        credentials: "include",
    });
    const reservationDetails : reservationDetailsType = await res.json();

    return reservationDetails;
};