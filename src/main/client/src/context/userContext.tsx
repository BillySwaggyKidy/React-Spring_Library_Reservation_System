import { createContext, ReactNode, useCallback, useMemo, useState, useEffect } from "react";
import { accountType, userPingResponse } from "../types/user";
import { Env } from "../Env";
import { bookSummaryType } from "../types/book";

// Define the context type
export type UserContextType = {
    currentUser: accountType;
    setUserData: (response: accountType) => void;
    cartContent: bookSummaryType[];
    setUserBookCart: (cart: bookSummaryType[]) => void;
};

export const UserContext = createContext<UserContextType>({} as UserContextType);

export default function UserProvider({ children }: { children: ReactNode }) {
    const [currentUser, setCurrentUser] = useState<accountType>({
        id: -1,
        username: "",
        role: "ROLE_ANONYMOUS",
    });
    const [cartContent, setCartContent] = useState<bookSummaryType[]>([]);

    // Function to update user data in the context
    const setUserData = useCallback((newData: accountType) => {
        setCurrentUser(newData);
    }, []);

    // Function to update user cart book in the context
    const setUserBookCart = useCallback((newCart: bookSummaryType[]) => {
        setCartContent(newCart);
    }, []);

    const resetUserData = () => {
        setCurrentUser({
            id: -1,
            username: "",
            role: "ROLE_ANONYMOUS"
        });
        setCartContent([]);
    }

    // UseEffect to rehydrate the user context on app startup
    useEffect(() => {
        const rehydrateUser = async () => {
            try {
                const res = await fetch(`${Env.API_BASE_URL}/auth/ping`, {
                    credentials: "include", // To send cookies with the request
                });

                if (!res.ok) {
                    resetUserData();
                    return;
                }

                const data: userPingResponse = await res.json();
                if (data.authenticated) {
                    // Set the user context if authenticated
                    setUserData({
                        id: data.id,
                        username: data.username,
                        role: data.role,
                    });
                } else {
                    resetUserData();
                }
            } catch (error) {
                console.error("Error fetching user data:", error);
                resetUserData();
            }
        };

        rehydrateUser();
    }, [setUserData]);

    // Memoize the context value to prevent unnecessary re-renders
    const contextValue = useMemo(() => ({
        currentUser,
        setUserData,
        cartContent,
        setUserBookCart
    }), [currentUser, setUserData, cartContent, setUserBookCart]);

    return (
        <UserContext.Provider value={contextValue}>
            {children}
        </UserContext.Provider>
    );
}
