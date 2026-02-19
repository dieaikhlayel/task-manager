
import { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchTasks, createTask, updateTask, deleteTask } from '../store/slices/taskSlice';

export const useTasks = (initialFilters = {}) => {
    const dispatch = useDispatch();
    const { tasks, isLoading, error } = useSelector((state) => state.tasks);
    const [filters, setFilters] = useState(initialFilters);
    const [filteredTasks, setFilteredTasks] = useState([]);

    useEffect(() => {
        dispatch(fetchTasks(filters));
    }, [dispatch, filters]);

    useEffect(() => {
        let filtered = [...(tasks || [])];

        if (filters.status) {
            filtered = filtered.filter(t => t.status === filters.status);
        }

        if (filters.priority) {
            filtered = filtered.filter(t => t.priority === filters.priority);
        }

        if (filters.search) {
            const searchLower = filters.search.toLowerCase();
            filtered = filtered.filter(t =>
                t.title.toLowerCase().includes(searchLower) ||
                t.description?.toLowerCase().includes(searchLower)
            );
        }

        if (filters.category) {
            filtered = filtered.filter(t => t.category === filters.category);
        }

        setFilteredTasks(filtered);
    }, [tasks, filters]);

    const addTask = async (taskData) => {
        return await dispatch(createTask(taskData)).unwrap();
    };

    const editTask = async (id, taskData) => {
        return await dispatch(updateTask({ id, taskData })).unwrap();
    };

    const removeTask = async (id) => {
        return await dispatch(deleteTask(id)).unwrap();
    };

    const updateFilters = (newFilters) => {
        setFilters(prev => ({ ...prev, ...newFilters }));
    };

    const resetFilters = () => {
        setFilters(initialFilters);
    };

    return {
        tasks: filteredTasks,
        allTasks: tasks,
        isLoading,
        error,
        filters,
        updateFilters,
        resetFilters,
        addTask,
        editTask,
        removeTask
    };
};
