import BookSearch from "./bookSearch/BookSearch";

export default function Dashboard() {
    return (
        <div className="flex-1 flex flex-col justify-start items-center bg-home bg-cover">
            <div className="m-4 p-2 w-full h-full flex flex-col justify-start items-center">
                <BookSearch/>
            </div>
        </div>
    );

};