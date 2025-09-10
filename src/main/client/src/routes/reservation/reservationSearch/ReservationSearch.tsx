import FieldFilter from "@/src/components/filters/FieldFilter";
import Pagination from "@/src/components/pagination/Pagination";
import { reservationFilterFieldsData } from "@/src/constants/filters/filtersFields";
import { Env } from "@/src/Env";
import { filterDataType } from "@/src/types/filter";
import { inputFieldValueType } from "@/src/types/input";
import { PagedResponse } from "@/src/types/pageResponse";
import { reservationItemType } from "@/src/types/reservation";
import { useContext, useEffect, useRef, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import ReservationItem from "./reservationItem/ReservationItem";
import { UserContext } from "@/src/context/userContext";

export default function ReservationSearch() {
    const userContext = useContext(UserContext);
    const userData = userContext.currentUser;
    const navigate = useNavigate();
    /**
     * 1) searchParams: 
     * Synchronization with the URL (e.g., ?title=Dune&page=2).
     * Allows filters to be retained even after refreshing or navigating.
    */
    const [searchParams, setSearchParams] = useSearchParams();
    /**
     * 2) filterFields (local state):
     * Represents what the user is typing into the fields.
     * We initialize with what is already in the URL (searchParams),
     * so that the filters are pre-filled when we return to the page.
    */
    const [filterFields, setFilterFields] = useState<filterDataType[]>(() => {
        return reservationFilterFieldsData.map(filter => {
            const filterValue = searchParams.get(filter.searchParamName);
            return { ...filter, value: filterValue ?? "" };
        });
    });
    /**
     * 3) currentSearchFilters (persistent ref):
     * Contains the validated filters (those used when the “Search” button was last clicked).
     * It is used for pagination: thus, changing pages
     * does not depend on what the user types in the inputs,
     * but only on the last validated search.
    */
    const currentSearchFilters = useRef<filterDataType[]>(structuredClone(reservationFilterFieldsData));
    const [reservationDataList, setReservationDataList] = useState<reservationItemType[]>([]);
    const pageIndex = useRef<number>(0);
    const totalPages = useRef<number>(0);

    // handle the activation of a badge filter for the search of a project
    const handleFilterFieldData = (id : string, value: inputFieldValueType ) => {
        const newFilterFields: filterDataType[] = filterFields.map((filter)=>{
            if (filter.id == id) {
                filter.value = value;
            }
            return filter;
        });
        setFilterFields(newFilterFields);
    };

    // handle the change of the page
    const changePageIndex = (newPage : number) => {
        setSearchParams(prev => {
            const newParams = new URLSearchParams(prev);
            newParams.set("page", newPage.toString());
            return newParams;
        })
        getReservationsData(newPage);
    };

    // get called once at the beginning
    const initialSearchFilterData = () => {

        // sync the currentSearchFilters with the url params
        currentSearchFilters.current = currentSearchFilters.current.map(filter => {
            const filterValue = searchParams.get(filter.searchParamName);
            return {
                ...filter,
                value: filterValue ?? ""
            };
        });

        const pageIndex = Number(searchParams.get("page")) || 0;
        getReservationsData(pageIndex);
    };

    // get called when clicking on the search button
    const handleSearchUsers = () => {
        setSearchParams(prev => {
            const newParams = new URLSearchParams(prev);

            currentSearchFilters.current.forEach((filter, index) => {
            filter.value = filterFields[index].value;
                if (filter.value !== "") {
                    newParams.set(filter.searchParamName, String(filter.value));
                } else {
                    newParams.delete(filter.searchParamName);
                }
            });

            // we delete the page param to put it back at the end of the url
            newParams.delete("page");
            // reset page 0 when we are making a new api search call
            newParams.set("page", "0");

            return newParams;
        });

        getReservationsData(0);
    }

    const getReservationsData = async (pageNumber : number) => {
        const searchParams: Record<string, string> = {};
        filterFields.forEach((filterData)=>{
            searchParams[filterData.searchParamName] = String(filterData.value);
        });
        const urlSearchParams = new URLSearchParams(searchParams);
        const urlParams : string = urlSearchParams.size > 0 ? new URLSearchParams(searchParams) + "&" : "";
        const response = await fetch(`${Env.API_BASE_URL}/api/reservations?` + urlParams + `page=${pageNumber}`, {
            method: "GET",
            credentials: "include",
        });
        if (response.ok) {
            // save the totalPages, pageIndex and update the reservationDataList
            const usersResponse : PagedResponse<reservationItemType> = await response.json();
            totalPages.current = usersResponse.totalPages;
            pageIndex.current = usersResponse.page;
            setReservationDataList(usersResponse.content);
        }
    };

    useEffect(()=>{
        initialSearchFilterData();
    },[]);

    useEffect(() => {
        if (!userData || ["ROLE_ANONYMOUS","ROLE_CUSTOMER"].includes(userData.role)) {
            navigate("/");
        }
    }, [userContext, navigate]);

    return (
        <div className="w-4/5 h-full grid grid-rows-8 grid-cols-1 border-2 border-orange-900 rounded-lg bg-orange-100/50">
            <div className="w-full grid grid-rows-1 grid-cols-8 border-b-4 border-black rounded-t-lg bg-orange-300/50 px-4">
                <div className="col-span-7 col-start-1 flex flex-row items-center justify-around gap-4">
                    {
                        filterFields.map((filter)=>
                            <FieldFilter key={filter.id} {...filter} filterCallback={handleFilterFieldData}/>
                        )
                    }
                </div>
                <div className="col-span-1 flex flex-row items-center justify-end">
                    <button className="bg-amber-700 hover:bg-amber-600 active:bg-amber-800 text-lg font-bold p-2 rounded-lg cursor-pointer" onClick={handleSearchUsers}>Search</button>
                </div>
            </div>
            <div className="w-full row-start-2 row-span-6 bg-orange-200/80 flex flex-col items-center max-h-full overflow-y-auto gap-4 p-4">
            {
                reservationDataList.map((reservation)=>
                    <ReservationItem key={reservation.id + reservation.userID} {...reservation} refreshReservationTable={()=>getReservationsData(0)}/>
                )
            }
            </div>
            <div className="w-full row-span-1 border-t-4 border-black rounded-b-lg bg-orange-300/80 p-2 flex flex-row justify-center items-center">
                {
                    <Pagination key={pageIndex.current + "|" + totalPages.current} totalPages={totalPages.current} currentPage={pageIndex.current} handlePageRequest={changePageIndex}/>
                }
            </div >
        </div>
    );
}