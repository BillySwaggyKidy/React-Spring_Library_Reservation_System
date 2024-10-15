import { useEffect, useRef } from "react";

// this represent the modal of a project inside the work page
export default function BookModa({open, title, author, genres, description, closeModal} : {open:boolean, title:string, author:string, genres:string[], description: string, closeModal: () => void}) {
    const ref = useRef<HTMLDialogElement>(null);

    useEffect(() => {
        if (open) {
        ref.current?.showModal();
        } else {
        ref.current?.close();
        }
    }, [open]);

    return (
        <dialog
            className="m-auto rounded-xl bg-white border-4 border-purple-600 w-[800px] h-[600px] shadow-3xl bg-gradient-to-t from-black/80 to-black/90 shadow-inner shadow-purple-500 backdrop:backdrop-blur-lg backdrop:animate-fadeIn animate-popIn"
            ref={ref}
            onClose={closeModal}
        >
            <div className="h-full w-full flex flex-col justify-start items-center">
                <div className="w-full h-8 flex flex-row justify-end items-center">
                    <button className="w-8 h-8 rounded bg-transparent text-3xl text-lavender font-bold flex flex-row justify-center items-center" onClick={closeModal}>X</button>
                </div>
                <div className="w-full flex flex-col items-center px-1 py-2 overflow-y-auto">
                    <div className="w-full flex flex-col items-center">
                        <p className="text-2xl font-bold underline">{title}</p>
                        <div className="w-full py-1 flex flex-row justify-center items-center gap-x-2 overflow-y-auto">
                            {
                                genres.map((genre)=>
                                    <div key={genre} className="px-2 w-fit h-6 border-2 border-white rounded-full bg-slate-600 text-center">
                                        <p className="font-bold">{genre}</p>
                                    </div>
                                )
                            }
                        </div>
                        <p className="text-2xl font-bold underline">{author}</p>
                        <p>{description}</p>
                    </div>
                </div>
            </div>
        </dialog>
    );
};