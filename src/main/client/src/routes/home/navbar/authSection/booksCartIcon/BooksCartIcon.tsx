import ReservedBooks from '@/src/assets/icons/books.svg?react';
import { UserContext } from '@/src/context/userContext';
import { useContext } from 'react';
import { useNavigate } from 'react-router-dom';

// represent the orange badge in the navbar, it is the cart button that display with a red bubble the number of book inside the cart
export default function BooksCartIcon() {

    const userContext = useContext(UserContext);
    // number of reserved book inside the cart
    const cartSize : number = userContext?.cartContent.length || 0;
    const navigate = useNavigate();

    const goToCartPage = () => {
        navigate("/cart");
    }

    return (
        <div className="h-full w-full flex-1 flex flex-row justify-center items-center cursor-pointer">
            <div className="relative w-9 h-9 bg-yellow-600 hover:bg-yellow-400 rounded-full flex flex-row justify-center items-center transition duration-200 ease-in-out hover:scale-110 p-2" onClick={goToCartPage}>
                {
                    cartSize > 0 && 
                    <div className="absolute -top-1 -right-1 w-5 h-5 flex flex-row justify-center items-center rounded-full bg-red-700">
                        <p className="text-white text-xs">{cartSize}</p>
                    </div>
                }
                <ReservedBooks className="w-8 h-8 fill-white" />
            </div>
        </div>
    );
}