interface confirmDialogType {
    open: boolean,
    close: ()=>void,
    title: string,
    text: string,
    okButtonText: string,
    Icon: React.ReactNode,
    callback: ()=>void | Promise<void>
}

export default function ConfirmDialog({open, close, title, text, okButtonText, Icon, callback} : confirmDialogType) {

    const callbackAndClose = () => {
        callback();
        close();
    }

    return (
        <>
        {
            open &&
            <dialog open className="w-full h-full fixed inset-0 flex items-center justify-center bg-gray-500/50">
                <div className="bg-white rounded-xl p-6 w-full max-w-lg shadow-lg animate-fade-in-scale">
                    <div className="flex flex-row justify-start items-center mb-4">
                        <p className="text-2xl font-bold text-black">{title}</p>
                    </div>

                    <div className="flex flex-row items-center mb-6">
                        <div className="flex justify-center items-center mr-4">
                            {Icon}
                        </div>
                        <div className="flex-1">
                            <p className="text-black">{text}</p>
                        </div>
                    </div>
                    
                    <div className="flex flex-row justify-end gap-4">
                        <button className="px-4 py-2 rounded-xl bg-red-500 hover:bg-red-600 text-white cursor-pointer" onClick={callbackAndClose}>{okButtonText}</button>
                        <button className="px-4 py-2 rounded-xl bg-gray-500 hover:bg-gray-600 text-white cursor-pointer" onClick={close}>Cancel</button>
                    </div>
                </div>
            </dialog>
        }
        </>
    );
}