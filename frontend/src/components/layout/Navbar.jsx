
import { Link, useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { logout } from '../../store/slices/authSlice';
import { FaTasks, FaSignOutAlt, FaUser, FaBars } from 'react-icons/fa';

const Navbar = ({ toggleSidebar }) => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { user } = useSelector((state) => state.auth);

  const handleLogout = () => {
    dispatch(logout());
    navigate('/login');
  };

  return (
    <nav className="bg-white dark:bg-gray-800 shadow-lg fixed top-0 left-0 right-0 z-40">
      <div className="px-4">
        <div className="flex justify-between h-16">
          <div className="flex items-center">
            <button
              onClick={toggleSidebar}
              className="p-2 rounded-md hover:bg-gray-100 dark:hover:bg-gray-700 mr-4"
            >
              <FaBars className="text-gray-600 dark:text-gray-300" />
            </button>
            <Link to="/dashboard" className="flex items-center space-x-2">
              <FaTasks className="text-blue-600 text-xl" />
              <span className="font-bold text-xl dark:text-white">Task Manager</span>
            </Link>
          </div>
          
          <div className="flex items-center space-x-4">
            <div className="flex items-center space-x-2">
              <div className="w-8 h-8 rounded-full bg-blue-600 flex items-center justify-center text-white">
                {user?.username?.charAt(0).toUpperCase()}
              </div>
              <span className="text-gray-700 dark:text-gray-300 hidden md:block">
                {user?.username}
              </span>
            </div>
            
            <button
              onClick={handleLogout}
              className="flex items-center space-x-1 bg-red-500 text-white px-3 py-2 rounded hover:bg-red-600"
            >
              <FaSignOutAlt />
              <span className="hidden md:block">Logout</span>
            </button>
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
