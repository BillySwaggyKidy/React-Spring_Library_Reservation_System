import { userDataType } from "@/src/constants/interfaces";
import { createContext, ReactNode, useCallback, useMemo, useState } from "react";


export type UserContextType = {
    currentUser: userDataType | null;
    setUserData: (response: userDataType | null) => void;
};

export const UserContext = createContext<UserContextType | null>(null);

export default function UserProvider({children} : {children : ReactNode}) {
    const [currentUser, setCurrentUser] = useState<userDataType | null>(null);

    const setUserData = useCallback((newData : userDataType | null) => {
        setCurrentUser(newData);
    }, []);

    const contextValue = useMemo(() => ({
        currentUser,
        setUserData
    }), [currentUser, setUserData]);

    return (
        <UserContext.Provider value={contextValue}>
            {children}
        </UserContext.Provider>
    );
}