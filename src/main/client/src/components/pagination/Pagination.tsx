import { useState } from "react";

export default function Pagination({totalPages, currentPage, handlePageRequest} : {totalPages : number, currentPage : number, handlePageRequest: (page : number) => void}) {
    const [currentPageNav, setCurrentPageNav] = useState<number>(currentPage);

    const changePage = (pageNumber : number) => {
        if (pageNumber != currentPageNav && (pageNumber >= 0 && pageNumber < totalPages)) {
            setCurrentPageNav(pageNumber);
            handlePageRequest(pageNumber);
        }
    };

    const PageBox = ({pageIndex, text} : {pageIndex:number, text:string}) => {

        return (
            <div className={`w-6 h-6 flex flex-row justify-center items-center ${pageIndex == currentPageNav ? "cursor-none bg-blue-400" : "cursor-pointer hover:bg-gray-400/50"} rounded-md`} onClick={() => changePage(pageIndex)}>
                <p className="text-lg font-bold text-gray-600">{text}</p>
            </div>
        );
    };

    const IncrPageBox = ({text, incrValue} : {text : string, incrValue: number}) => {
        const newValue = currentPageNav + incrValue;
        const isInRange = newValue >= 0 && newValue < totalPages;
        return (
            isInRange &&
            <div className="h-6 flex flex-row justify-center items-center cursor-pointer border-2 bg-gray-300/50 p-0.5 rounded-sm" onClick={() => changePage(newValue)}>
                <p className="text-lg font-bold hover:text-black text-gray-500">{text}</p>
            </div>
        );
    };

    const PageNavigation = () => {
        const totalOfNavBox = 3;

        return (
            <div className="flex flex-row justify-center items-center mx-2">
                {
                    Array.from({ length: totalOfNavBox }, (_, i) => currentPageNav + i).filter((n : number) => n < totalPages - 1).map((n : number)=>
                        <PageBox key={"nav" + n} pageIndex={n} text={(n+1).toString()}/>
                    )
                }
                {
                    currentPageNav + 3 < totalPages && 
                    <div className="w-6 h-6 flex flex-row justify-center items-center">
                        <p className="font-bold text-gray-600">...</p>
                    </div>
                }
                <PageBox key={"navLast"} pageIndex={totalPages - 1} text={(totalPages).toString()}/>
            </div>

        );
    }

    return (
        <div className="w-full flex flex-row justify-center items-center">
            <div className="flex flex-row items-center bg-gray-50 rounded-md shadow-lg p-2">
                <IncrPageBox text="previous" incrValue={-1}/>
                <PageNavigation/>
                <IncrPageBox text="next" incrValue={1}/>
            </div>
        </div>
    );
}