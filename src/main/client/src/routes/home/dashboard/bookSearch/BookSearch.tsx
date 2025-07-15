import { ChangeEvent, useEffect, useRef, useState } from "react";
import FieldFilter from "../../../../components/filters/FieldFilter";
import { bookFilterFieldsData } from "@/src/constants/filters/filtersFields";
import { filterDataType } from "@/src/types/filter";
import { inputFieldValueType } from "@/src/types/input";
import BookList from "./bookList/BookList";
import { Env } from "@/src/Env";
import { bookType } from "@/src/types/book";
import { PagedResponse } from "@/src/types/pageResponse";
import Pagination from "@/src/components/pagination/Pagination";

// this represent the search filter component inside the work page
export default function BookSearch() {
    const [filterFields, setFilterFields] = useState<filterDataType[]>(bookFilterFieldsData);
    const [booksList, setBooksList] = useState<bookType[]>([]);
    const pageIndex = useRef<number>(0);
    const totalPages = useRef<number>(0);

    // handle the user input inside the main input (project name)
    const handleSearchBar = (e : ChangeEvent<HTMLInputElement>) => {
        const text = e.currentTarget.value;
        handleFilterFieldData(filterFields[0].id, text);
    };

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
        pageIndex.current = newPage;
        getBooksData();
    };

    const getBooksData = async () => {
        const searchParams: Record<string, string> = {};
        filterFields.forEach((filterData)=>{
            searchParams[filterData.searchParamName] = String(filterData.value);
        });
        const response = await fetch(`${Env.API_BASE_URL}/api/books?` + new URLSearchParams(searchParams) + `&page=${pageIndex.current}`, {
            method: "GET",
            credentials: "include",
        });
        if (response.ok) {
            // Save state, redirect, show badge, etc
            const booksResponse : PagedResponse<bookType> = await response.json();
            totalPages.current = booksResponse.totalPages;
            setBooksList(booksResponse.content);
        }
    };

    useEffect(()=>{
        getBooksData();
    },[]);

    return (
        <div className="w-full flex flex-col items-center">
            <div className="w-full flex flex-col items-center justify-center">
                <div className="w-full flex flex-row justify-center items-center">
                    <input 
                        className="w-2/5 border-2 border-solid rounded-lg text-lg text-lavender placeholder:text-lavender border-blue-400 bg-blue-300/70 bg-search-icon bg-no-repeat bg-[length:30px_30px] bg-[center_left_0.3rem] py-2 pl-10 pr-5"
                        type="text" name="search" placeholder="Search the book's name"
                        onChange={handleSearchBar}
                    />
                    <div className="w-1/5 flex flex-row items-center justify-center">
                        <button className="bg-blue-500 hover:bg-blue-400 active:bg-blue-600 text-lg font-bold p-2 rounded-lg cursor-pointer" onClick={getBooksData}>Search</button>
                    </div>
                </div>  
                <div className="w-2/5 flex flex-row justify-around items-center p-2 gap-4">
                    {
                        bookFilterFieldsData.slice(1).map((filter)=>
                            <FieldFilter key={filter.id} {...filter} filterCallback={handleFilterFieldData}/>
                        )
                    }
                </div>
            </div>
            <div className='mx-2 mt-2 p-1 flex flex-wrap items-center max-w-full overflow-y-auto gap-7 max-h-[400px]'>
                <BookList booksList={booksList}/>
            </div>
            {
               booksList.length > 0 && <Pagination totalPages={totalPages.current} handlePageRequest={changePageIndex}/>
            }
        </div>
    );
};