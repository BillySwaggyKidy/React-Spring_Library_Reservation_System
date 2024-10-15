import { ChangeEvent, useState, useEffect } from "react";
import FieldFilter from "./fieldFilter/FieldFilter";
import { bookType, filterFieldType } from "@/src/constants/interfaces";
import { bookFilterFieldsData } from "@/src/constants/filters/filtersFields";

// this represent the search filter component inside the work page
const SearchFilter = ({booksList} : {booksList : bookType[]}) => {
    const [searchBookName, setSearchBookName] =  useState<string>("");
    const [filterFields, setFilterFields] = useState<filterFieldType[]>(bookFilterFieldsData);


    // handle the user input inside the main input (project name)
    const handleSearchBar = (e : ChangeEvent<HTMLInputElement>) => {
        const text = e.currentTarget.value;
        setSearchBookName(text);
    };

    // handle the activation of a badge filter for the search of a project
    const handleFilterFieldData = (id : string, value: string) => {
        const newFilterFields: filterFieldType[] = filterFields.map((filter)=>{
            if (filter.id == id) {
                filter.value = value;
            }
            return filter;
        });
        setFilterFields(newFilterFields);
    };

    const alphabeticalSort = (a : bookType, b : bookType) => {
        if (a.title < b.title) {
            return -1;
          }
          if (a.title > b.title) {
            return 1;
          }
          return 0;
    }

    // this function is used inside a filter function to filter all project depending of the name and activated badges
    const filterBooks = (book: bookType) : boolean => {
        const searchMatchName = book.title.toLowerCase().includes(searchBookName);
        return searchMatchName;
    } 

    return (
        <div className="w-full flex flex-col items-center my-4">
            <div className="w-full flex flex-col items-center justify-center">
                <input 
                    className="w-9/12 border-2 border-solid rounded-lg text-lg text-lavender placeholder:text-lavender bg-blue-400 bg-search-icon bg-no-repeat bg-[length:30px_30px] bg-[center_left_0.3rem] py-2 pl-10 pr-5"
                    type="text" name="search" placeholder="Search the book's name"
                    onChange={handleSearchBar}
                />
            </div>
            <div className='mt-16 p-1 flex flex-row items-center max-w-full overflow-x-auto gap-7 sm:flex-wrap sm:max-h-[600px] sm:max-w-none sm:overflow-y-auto'>
                {
                    booksList.sort(alphabeticalSort).filter((book)=>filterBooks(book)).map((book) => (
                        <p>{book.title}</p>
                    ))
                }
            </div>
        </div>
    );
};

export default SearchFilter;