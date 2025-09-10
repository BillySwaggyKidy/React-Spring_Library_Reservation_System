import FieldFilter from "@/src/components/filters/FieldFilter";
import { userFilterFieldsData } from "@/src/constants/filters/filtersFields";
import { Env } from "@/src/Env";
import { filterDataType } from "@/src/types/filter";
import { inputFieldValueType } from "@/src/types/input";
import { userDataType } from "@/src/types/user";
import { useEffect, useRef, useState } from "react";
import UserItem from "./items/UserItem";
import CreateUserModal from "./modal/CreateUserModal";
import { PagedResponse } from "@/src/types/pageResponse";
import Pagination from "@/src/components/pagination/Pagination";
import { useSearchParams } from "react-router-dom";

export default function UserSearch() {
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
        return userFilterFieldsData.map(filter => {
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
    const currentSearchFilters = useRef<filterDataType[]>(structuredClone(userFilterFieldsData));
    const [usersDataList, setUsersDataList] = useState<userDataType[]>([]);
    const [displayCreateUserModal, setDisplayCreateUserModal] = useState<boolean>(false);
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
        getUsersData(newPage);
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
        getUsersData(pageIndex);
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

        getUsersData(0);
    }

    const getUsersData = async (pageNumber : number) => {
        const searchParams: Record<string, string> = {};
        filterFields.forEach((filterData)=>{
            searchParams[filterData.searchParamName] = String(filterData.value);
        });
        const urlSearchParams = new URLSearchParams(searchParams);
        const urlParams : string = urlSearchParams.size > 0 ? new URLSearchParams(searchParams) + "&" : "";
        const response = await fetch(`${Env.API_BASE_URL}/api/users?` + urlParams + `page=${pageNumber}`, {
            method: "GET",
            credentials: "include", // IMPORTANT for sending/receiving cookies
        });
        if (response.ok) {
            // save the totalPages, pageIndex and update the usersDataList
            const usersResponse : PagedResponse<userDataType> = await response.json();
            totalPages.current = usersResponse.totalPages;
            pageIndex.current = usersResponse.page;
            setUsersDataList(usersResponse.content);
        }
    };

    const handleUserAdded = async (refresh: boolean) => {
        setDisplayCreateUserModal(false);
        if (refresh) {
            await getUsersData(0); // re-fetch filtered data
        }
    };

    useEffect(()=>{
        initialSearchFilterData();
    },[]);

    return (
        <div className="w-4/5 h-full grid grid-rows-8 grid-cols-1 border-2 border-gray-500 rounded-lg bg-indigo-400/50">
            <div className="w-full row-span-1 row-start-1 grid grid-cols-7 grid-rows-1 border-b-4 border-black rounded-t-lg bg-indigo-200/50">
                <div className="col-span-6 flex flex-row items-center px-4">
                    <div className="w-4/5 flex flex-row items-center justify-around gap-4">
                    {
                        userFilterFieldsData.map((filter)=>
                            <FieldFilter key={filter.id} {...filter} filterCallback={handleFilterFieldData}/>
                        )
                    }
                    </div>
                    <div className="w-1/5 flex flex-row items-center justify-end">
                        <button className="bg-blue-500 hover:bg-blue-400 active:bg-blue-600 text-lg font-bold p-2 rounded-lg cursor-pointer" onClick={handleSearchUsers}>Search</button>
                    </div>
                </div>
                <div className="col-span-1 flex flex-row justify-center items-center border-black border-l-4">
                    <button className=" bg-green-400 hover:bg-green-500 active:bg-green-600 cursor-pointer border-2 border-white rounded-lg font-bold text-lg p-2" onClick={()=>setDisplayCreateUserModal(true)}>Create</button>
                    {displayCreateUserModal && <CreateUserModal open={displayCreateUserModal} close={handleUserAdded}/>}
                </div>
            </div>
            <div className="w-full row-start-2 row-span-6 bg-indigo-200/80 flex flex-col items-center max-h-full overflow-y-auto gap-4 p-4">
            {
                usersDataList.map((user)=>
                    <UserItem key={user.email} {...user} refreshUserTable={()=>{getUsersData(0)}}/>
                )
            }
            </div>
            <div className="w-full row-span-1 border-t-4 border-black rounded-b-lg bg-indigo-200/50 p-2 flex flex-row justify-center items-center">
                {
                    <Pagination key={pageIndex.current + "|" + totalPages.current} totalPages={totalPages.current} currentPage={pageIndex.current} handlePageRequest={changePageIndex}/>
                }
            </div >
        </div>
    );
}