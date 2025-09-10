import { ChangeEvent, useEffect, useRef, useState } from "react";
import FieldFilter from "../../../../components/filters/FieldFilter";
import { bookFilterFieldsData } from "@/src/constants/filters/filtersFields";
import { filterDataType } from "@/src/types/filter";
import { inputFieldValueType } from "@/src/types/input";
import BookList from "./bookList/BookList";
import { Env } from "@/src/Env";
import { bookSummaryType } from "@/src/types/book";
import { PagedResponse } from "@/src/types/pageResponse";
import Pagination from "@/src/components/pagination/Pagination";
import { useSearchParams } from "react-router-dom";

// this represent the search filter component inside the work page
export default function BookSearch() {
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
        return bookFilterFieldsData.map(filter => {
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
    const currentSearchFilters = useRef<filterDataType[]>(structuredClone(bookFilterFieldsData));
    const [booksList, setBooksList] = useState<bookSummaryType[]>([]);
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
        setSearchParams(prev => {
            const newParams = new URLSearchParams(prev);
            newParams.set("page", newPage.toString());
            return newParams;
        })
        getBooksData(newPage);
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
        getBooksData(pageIndex);
    };


    // get called when clicking on the search button
    const handleSearchBooks = () => {
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

        getBooksData(0);
    }

    const getBooksData = async (pageNumber : number) => {
        const searchParams: Record<string, string> = {};
        currentSearchFilters.current.forEach((filterData)=>{
            if (filterData.value != "") searchParams[filterData.searchParamName] = String(filterData.value);
        });
        const urlSearchParams = new URLSearchParams(searchParams);
        const urlParams : string = urlSearchParams.size > 0 ? new URLSearchParams(searchParams) + "&" : "";
        const response = await fetch(`${Env.API_BASE_URL}/api/books?` + urlParams + `page=${pageNumber}`, {
            method: "GET",
            credentials: "include",
        });
        if (response.ok) {
            // save the totalPages, pageIndex and update the bookList
            const booksResponse : PagedResponse<bookSummaryType> = await response.json();
            totalPages.current = booksResponse.totalPages;
            pageIndex.current = booksResponse.page;
            setBooksList(booksResponse.content);
        }
    };

    useEffect(()=>{
        initialSearchFilterData();
    },[]);

    return (
        <div className="w-full flex flex-col items-center">
            <div className="w-full flex flex-col items-center justify-center">
                <div className="w-full flex flex-row justify-center items-center">
                    <input 
                        className="w-2/5 border-2 border-solid rounded-lg text-lg text-lavender placeholder:text-lavender border-blue-400 bg-blue-300/80 bg-search-icon bg-no-repeat bg-[length:30px_30px] bg-[center_left_0.3rem] py-1 pl-10 pr-5"
                        type="text" name="search" placeholder="Search the book's name"
                        value={filterFields[0].value.toString()}
                        onChange={handleSearchBar}
                    />
                    <div className="w-1/5 flex flex-row items-center justify-center">
                        <button className="bg-blue-500 hover:bg-blue-400 active:bg-blue-600 text-lg font-bold p-2 rounded-lg cursor-pointer" onClick={handleSearchBooks}>Search</button>
                    </div>
                </div>  
                <div className="w-2/5 flex flex-row justify-around items-end p-2 gap-4">
                    {
                        filterFields.slice(1).map((filter)=>
                            <FieldFilter key={filter.id} {...filter} filterCallback={handleFilterFieldData}/>
                        )
                    }
                </div>
            </div>
            <div className='mx-2 mt-2 p-1 flex flex-wrap items-start max-w-full overflow-y-auto gap-7 max-h-[400px]'>
                <BookList booksList={booksList}/>
            </div>
            {
               booksList.length > 0 && <Pagination key={pageIndex.current + "|" + totalPages.current} totalPages={totalPages.current} currentPage={pageIndex.current} handlePageRequest={changePageIndex}/>
            }
        </div>
    );
};