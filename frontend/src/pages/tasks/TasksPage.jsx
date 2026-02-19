
import { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchTasks, createTask, updateTask, deleteTask } from '../../store/slices/taskSlice';
import Navbar from '../../components/layout/Navbar';
import TaskForm from '../../components/tasks/TaskForm';
import TaskList from '../../components/tasks/TaskList';
import { FaPlus } from 'react-icons/fa';
import toast from 'react-hot-toast';

const TasksPage = () => {
    const dispatch = useDispatch();
    const { tasks, isLoading } = useSelector((state) => state.tasks);
    const [showForm, setShowForm] = useState(false);
    const [editingTask, setEditingTask] = useState(null);
    const [filter, setFilter] = useState('ALL');

    useEffect(() => {
        dispatch(fetchTasks());
    }, [dispatch]);

    const handleCreateTask = async (taskData) => {
        try {
            await dispatch(createTask(taskData)).unwrap();
            toast.success('Task created successfully');
            setShowForm(false);
        } catch (error) {
            toast.error('Failed to create task');
        }
    };

    const handleUpdateTask = async (id, taskData) => {
        try {
            await dispatch(updateTask({ id, taskData })).unwrap();
            toast.success('Task updated successfully');
            setEditingTask(null);
        } catch (error) {
            toast.error('Failed to update task');
        }
    };

    const handleDeleteTask = async (id) => {
        if (window.confirm('Are you sure you want to delete this task?')) {
            try {
                await dispatch(deleteTask(id)).unwrap();
                toast.success('Task deleted successfully');
            } catch (error) {
                toast.error('Failed to delete task');
            }
        }
    };

    const filteredTasks = tasks?.filter(task => {
        if (filter === 'ALL') return true;
        return task.status === filter;
    }) || [];

    return (
        <div>
            <Navbar />
            <div className="max-w-7xl mx-auto px-4 py-8">
                <div className="flex justify-between items-center mb-8">
                    <h1 className="text-3xl font-bold">Tasks</h1>
                    <button
                        onClick={() => setShowForm(true)}
                        className="flex items-center space-x-2 bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
                    >
                        <FaPlus />
                        <span>New Task</span>
                    </button>
                </div>

                {/* Filter Tabs */}
                <div className="flex space-x-2 mb-6">
                    {['ALL', 'PENDING', 'IN_PROGRESS', 'COMPLETED'].map(status => (
                        <button
                            key={status}
                            onClick={() => setFilter(status)}
                            className={`px-4 py-2 rounded ${filter === status ? 'bg-blue-600 text-white' : 'bg-gray-200'}`}
                        >
                            {status}
                        </button>
                    ))}
                </div>

                {/* Task Form Modal */}
                {(showForm || editingTask) && (
                    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4">
                        <div className="bg-white rounded-lg p-6 max-w-md w-full">
                            <h2 className="text-xl font-bold mb-4">
                                {editingTask ? 'Edit Task' : 'Create New Task'}
                            </h2>
                            <TaskForm
                                task={editingTask}
                                onSubmit={editingTask ?
                                    (data) => handleUpdateTask(editingTask.id, data) :
                                    handleCreateTask}
                                onCancel={() => {
                                    setShowForm(false);
                                    setEditingTask(null);
                                }}
                            />
                        </div>
                    </div>
                )}

                {/* Task List */}
                {isLoading ? (
                    <div className="text-center py-8">Loading...</div>
                ) : (
                    <TaskList
                        tasks={filteredTasks}
                        onEdit={setEditingTask}
                        onDelete={handleDeleteTask}
                    />
                )}
            </div>
        </div>
    );
};

export default TasksPage;
