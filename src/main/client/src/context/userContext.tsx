import { createContext, ReactNode, useCallback, useMemo, useState, useEffect } from "react";
import { accountType } from "../types/user";

// Define the context type
export type UserContextType = {
    currentUser: accountType;
    setUserData: (response: accountType) => void;
};

export const UserContext = createContext<UserContextType | null>(null);

export default function UserProvider({ children }: { children: ReactNode }) {
    const [currentUser, setCurrentUser] = useState<accountType>({
        username: "",
        role: "ROLE_ANONYMOUS"
    });

    // Function to update user data in the context
    const setUserData = useCallback((newData: accountType) => {
        setCurrentUser(newData);
    }, []);

    // UseEffect to rehydrate the user context on app startup
    useEffect(() => {
        const rehydrateUser = async () => {
            try {
                const res = await fetch("http://localhost:8080/auth/ping", {
                    credentials: "include", // To send cookies with the request
                });

                if (!res.ok) {
                    setCurrentUser({
                        username: "",
                        role: "ROLE_ANONYMOUS"
                    });
                    return;
                }

                const data = await res.json();
                if (data.authenticated) {
                    // Set the user context if authenticated
                    setUserData({
                        username: data.username,
                        role: data.role,
                    });
                } else {
                    setCurrentUser({
                        username: "",
                        role: "ROLE_ANONYMOUS"
                    }); // No authentication, clear the context
                }
            } catch (error) {
                console.error("Error fetching user data:", error);
                setCurrentUser({
                    username: "",
                    role: "ROLE_ANONYMOUS"
                }); // Clear user data in case of error
            }
        };

        rehydrateUser();
    }, [setUserData]);

    // Memoize the context value to prevent unnecessary re-renders
    const contextValue = useMemo(() => ({
        currentUser,
        setUserData,
    }), [currentUser, setUserData]);

    return (
        <UserContext.Provider value={contextValue}>
            {children}
        </UserContext.Provider>
    );
}
