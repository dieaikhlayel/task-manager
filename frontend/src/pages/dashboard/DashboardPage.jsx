
import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchTasks } from '../../store/slices/taskSlice';
import Navbar from '../../components/layout/Navbar';
import { FaTasks, FaCheckCircle, FaClock, FaExclamationTriangle } from 'react-icons/fa';

const DashboardPage = () => {
    const dispatch = useDispatch();
    const { tasks, isLoading } = useSelector((state) => state.tasks);

    useEffect(() => {
        dispatch(fetchTasks());
    }, [dispatch]);

    const stats = {
        total: tasks?.length || 0,
        completed: tasks?.filter(t => t.status === 'COMPLETED').length || 0,
        pending: tasks?.filter(t => t.status === 'PENDING').length || 0,
        inProgress: tasks?.filter(t => t.status === 'IN_PROGRESS').length || 0,
        highPriority: tasks?.filter(t => t.priority === 'HIGH' || t.priority === 'CRITICAL').length || 0,
        overdue: tasks?.filter(t => t.overdue).length || 0
    };

    const recentTasks = tasks?.slice(0, 5) || [];

    return (
        <div>
            <Navbar />
            <div className="max-w-7xl mx-auto px-4 py-8">
                <h1 className="text-3xl font-bold mb-8">Dashboard</h1>

                {/* Stats Grid */}
                <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-6 gap-4 mb-8">
                    <StatCard icon={<FaTasks />} label="Total" value={stats.total} color="blue" />
                    <StatCard icon={<FaCheckCircle />} label="Completed" value={stats.completed} color="green" />
                    <StatCard icon={<FaClock />} label="Pending" value={stats.pending} color="yellow" />
                    <StatCard icon={<FaTasks />} label="In Progress" value={stats.inProgress} color="purple" />
                    <StatCard icon={<FaExclamationTriangle />} label="High Priority" value={stats.highPriority} color="red" />
                    <StatCard icon={<FaClock />} label="Overdue" value={stats.overdue} color="orange" />
                </div>

                {/* Recent Tasks */}
                <div className="bg-white rounded-lg shadow p-6">
                    <h2 className="text-xl font-bold mb-4">Recent Tasks</h2>
                    {isLoading ? (
                        <p>Loading...</p>
                    ) : recentTasks.length > 0 ? (
                        <div className="space-y-3">
                            {recentTasks.map(task => (
                                <div key={task.id} className="flex items-center justify-between p-3 border rounded">
                                    <div>
                                        <h3 className="font-semibold">{task.title}</h3>
                                        <p className="text-sm text-gray-600">{task.status}</p>
                                    </div>
                                    <span className={`px-2 py-1 rounded text-xs font-semibold
                    ${task.priority === 'HIGH' || task.priority === 'CRITICAL' ? 'bg-red-100 text-red-800' :
                                            task.priority === 'MEDIUM' ? 'bg-yellow-100 text-yellow-800' :
                                                'bg-green-100 text-green-800'}`}>
                                        {task.priority}
                                    </span>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <p className="text-gray-500">No tasks yet. Create your first task!</p>
                    )}
                </div>
            </div>
        </div>
    );
};

const StatCard = ({ icon, label, value, color }) => {
    const colors = {
        blue: 'bg-blue-100 text-blue-800',
        green: 'bg-green-100 text-green-800',
        yellow: 'bg-yellow-100 text-yellow-800',
        red: 'bg-red-100 text-red-800',
        purple: 'bg-purple-100 text-purple-800',
        orange: 'bg-orange-100 text-orange-800'
    };

    return (
        <div className={`${colors[color]} rounded-lg p-4 text-center`}>
            <div className="flex justify-center text-2xl mb-2">{icon}</div>
            <div className="text-2xl font-bold">{value}</div>
            <div className="text-sm">{label}</div>
        </div>
    );
};

export default DashboardPage;
