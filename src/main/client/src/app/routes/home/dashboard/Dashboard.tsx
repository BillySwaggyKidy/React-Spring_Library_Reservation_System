import SearchFilter from "./searchFilter/SearchFilter";

export default function Dashboard() {
    return (
        <div className="h-[90%] flex flex-col justify-start items-center">
            <div className="w-48 h-20 bg-amber-800/40 border-4 border-solid border-orange-800 rounded-lg flex flex-row justify-center items-center">
                <p className="text-white text-2xl font-bold">Book Library</p>
            </div>
            <div className="m-4 p-2 w-full h-full flex flex-col justify-start items-center">
                <div className="mt-2 w-full h-full">
                    <SearchFilter booksList={[]}/>
                </div>
            </div>
        </div>
    );

};