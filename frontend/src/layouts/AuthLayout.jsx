
import { Outlet } from 'react-router-dom';
import { FaTasks } from 'react-icons/fa';

const AuthLayout = () => {
  return (
    <div className="min-h-screen flex">
      {/* Left side - Auth forms */}
      <div className="flex-1 flex items-center justify-center bg-gray-50 dark:bg-gray-900">
        <div className="w-full max-w-md">
          <Outlet />
        </div>
      </div>
      
      {/* Right side - Hero section */}
      <div className="hidden lg:flex flex-1 bg-gradient-to-br from-blue-600 to-purple-700 text-white items-center justify-center p-12">
        <div className="max-w-lg text-center">
          <FaTasks className="text-8xl mx-auto mb-6" />
          <h1 className="text-4xl font-bold mb-4">Task Manager</h1>
          <p className="text-xl opacity-90">
            Organize your tasks, boost your productivity, and achieve your goals with our intuitive task management system.
          </p>
          <div className="mt-8 grid grid-cols-2 gap-4 text-left">
            <div className="bg-white bg-opacity-10 rounded-lg p-4">
              <h3 className="font-semibold mb-2">✅ Create Tasks</h3>
              <p className="text-sm opacity-75">Easily create and organize your tasks</p>
            </div>
            <div className="bg-white bg-opacity-10 rounded-lg p-4">
              <h3 className="font-semibold mb-2">🎯 Set Priorities</h3>
              <p className="text-sm opacity-75">Define priority levels for better focus</p>
            </div>
            <div className="bg-white bg-opacity-10 rounded-lg p-4">
              <h3 className="font-semibold mb-2">📊 Track Progress</h3>
              <p className="text-sm opacity-75">Monitor your task completion rate</p>
            </div>
            <div className="bg-white bg-opacity-10 rounded-lg p-4">
              <h3 className="font-semibold mb-2">📅 Due Dates</h3>
              <p className="text-sm opacity-75">Never miss a deadline</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AuthLayout;
