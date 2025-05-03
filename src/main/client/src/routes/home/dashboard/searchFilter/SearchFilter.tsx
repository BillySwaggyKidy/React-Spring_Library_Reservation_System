import { ChangeEvent, useState } from "react";
import FieldFilter from "../../../../components/filters/FieldFilter";
import { bookFilterFieldsData } from "@/src/constants/filters/filtersFields";
import { bookType } from "@/src/types/book";
import { filterDataType } from "@/src/types/filter";
import { inputFieldValueType } from "@/src/types/input";

// this represent the search filter component inside the work page
const SearchFilter = ({booksList} : {booksList : bookType[]}) => {
    const [searchBookName, setSearchBookName] =  useState<string>("");
    const [filterFields, setFilterFields] = useState<filterDataType[]>(bookFilterFieldsData);


    // handle the user input inside the main input (project name)
    const handleSearchBar = (e : ChangeEvent<HTMLInputElement>) => {
        const text = e.currentTarget.value;
        setSearchBookName(text);
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

    const dummieFunction = () => {
        console.log(booksList);
        console.log(searchBookName);
    }

    return (
        <div className="w-full flex flex-col items-center my-4">
            <div className="w-full flex flex-col items-center justify-center" onChange={dummieFunction}>
                <input 
                    className="w-2/5 border-2 border-solid rounded-lg text-lg text-lavender placeholder:text-lavender border-blue-400 bg-blue-300/70 bg-search-icon bg-no-repeat bg-[length:30px_30px] bg-[center_left_0.3rem] py-2 pl-10 pr-5"
                    type="text" name="search" placeholder="Search the book's name"
                    onChange={handleSearchBar}
                />
                <div className="w-2/5 flex flex-row justify-around items-center p-2 gap-4">
                    {
                        bookFilterFieldsData.map((filter)=>
                            <FieldFilter key={filter.id} {...filter} filterCallback={handleFilterFieldData}/>
                        )
                    }
                </div>
            </div>
            <div className='mt-16 p-1 flex flex-row items-center max-w-full overflow-x-auto gap-7 sm:flex-wrap sm:max-h-[600px] sm:max-w-none sm:overflow-y-auto'>

            </div>
        </div>
    );
};

export default SearchFilter;