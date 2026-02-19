
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { logout } from '../store/slices/authSlice';

export const useAuth = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const { user, token, isLoading } = useSelector((state) => state.auth);

    const handleLogout = () => {
        dispatch(logout());
        navigate('/login');
    };

    const isAuthenticated = !!token;
    const isAdmin = user?.roles?.includes('ADMIN');
    const isUser = user?.roles?.includes('USER');

    return {
        user,
        token,
        isLoading,
        isAuthenticated,
        isAdmin,
        isUser,
        logout: handleLogout
    };
};
