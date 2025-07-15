import FieldFilter from "@/src/components/filters/FieldFilter";
import { userFilterFieldsData } from "@/src/constants/filters/filtersFields";
import { Env } from "@/src/Env";
import { filterDataType } from "@/src/types/filter";
import { inputFieldValueType } from "@/src/types/input";
import { userDataType } from "@/src/types/user";
import { useEffect, useState } from "react";
import UserItem from "./items/UserItem";
import CreateUserModal from "./modal/CreateUserModal";
import { PagedResponse } from "@/src/types/pageResponse";

export default function UserTable() {
    const [filterFields, setFilterFields] = useState<filterDataType[]>(userFilterFieldsData);
    const [usersDataList, setUsersDataList] = useState<userDataType[]>([]);
    const [displayCreateUserModal, setDisplayCreateUserModal] = useState<boolean>(false);

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

    const getUsersData = async () => {
        const searchParams: Record<string, string> = {};
        filterFields.forEach((filterData)=>{
            searchParams[filterData.searchParamName] = String(filterData.value);
        });
        const response = await fetch(`${Env.API_BASE_URL}/api/users?` + new URLSearchParams(searchParams), {
            method: "GET",
            credentials: "include", // IMPORTANT for sending/receiving cookies
        });
        if (response.ok) {
            // Save state, redirect, show badge, etc
            const usersResponse : PagedResponse<userDataType> = await response.json();
            setUsersDataList(usersResponse.content);
        }
    };

    const handleUserAdded = async (newUser?: userDataType) => {
        setDisplayCreateUserModal(false);
        if (newUser) {
            await getUsersData(); // re-fetch filtered data
        }
    };

    useEffect(()=>{
        getUsersData();
    },[]);

    return (
        <div className="w-4/5 h-4/5 border borde-2 border-gray-500 rounded-lg bg-slate-100/50 grid grid-rows-7 grid-cols-1">
            <div className="w-full row-span-1 row-start-1 border-b-4 border-black rounded-t-lg bg-slate-300/50 flex flex-row items-center px-4">
                <div className="w-4/5 flex flex-row items-center justify-around gap-4">
                {
                    userFilterFieldsData.map((filter)=>
                        <FieldFilter key={filter.id} {...filter} filterCallback={handleFilterFieldData}/>
                    )
                }
                </div>
                <div className="w-1/5 flex flex-row items-center justify-end">
                    <button className="bg-blue-500 hover:bg-blue-400 active:bg-blue-600 text-lg font-bold p-2 rounded-lg cursor-pointer" onClick={getUsersData}>Search</button>
                </div>
            </div>
            <div className="w-full row-start-2 row-span-5 bg-slate-300/80 flex flex-col items-center max-h-full overflow-y-auto gap-4 p-4">
            {
                usersDataList.map((user)=>
                    <UserItem key={user.email} {...user}/>
                )
            }
            </div>
            <div className="w-full row-span-1 row-start-7 border-t-4 border-black rounded-b-lg bg-slate-300/80 p-2 flex flex-row justify-center items-center">
                <button className="w-full h-full bg-green-400 hover:bg-green-500 active:bg-green-600 cursor-pointer border-2 border-white rounded-lg" onClick={()=>setDisplayCreateUserModal(true)}>Create</button>
                {displayCreateUserModal && <CreateUserModal open={displayCreateUserModal} close={handleUserAdded}/>}
            </div >
        </div>
    );
}