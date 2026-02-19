
export const validateEmail = (email) => {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(String(email).toLowerCase());
};

export const validatePassword = (password) => {
    return password && password.length >= 6;
};

export const validateUsername = (username) => {
    return username && username.length >= 3 && username.length <= 20;
};

export const validateTaskTitle = (title) => {
    return title && title.length >= 3 && title.length <= 200;
};

export const validateTaskDescription = (description) => {
    return !description || description.length <= 1000;
};

export const validateTaskForm = (formData) => {
    const errors = {};

    if (!validateTaskTitle(formData.title)) {
        errors.title = 'Title must be between 3 and 200 characters';
    }

    if (!validateTaskDescription(formData.description)) {
        errors.description = 'Description cannot exceed 1000 characters';
    }

    if (formData.estimatedHours && (formData.estimatedHours < 0 || formData.estimatedHours > 1000)) {
        errors.estimatedHours = 'Estimated hours must be between 0 and 1000';
    }

    return {
        isValid: Object.keys(errors).length === 0,
        errors
    };
};

export const validateLoginForm = (formData) => {
    const errors = {};

    if (!formData.username) {
        errors.username = 'Username is required';
    }

    if (!formData.password) {
        errors.password = 'Password is required';
    }

    return {
        isValid: Object.keys(errors).length === 0,
        errors
    };
};

export const validateRegisterForm = (formData) => {
    const errors = {};

    if (!validateUsername(formData.username)) {
        errors.username = 'Username must be between 3 and 20 characters';
    }

    if (!validateEmail(formData.email)) {
        errors.email = 'Invalid email address';
    }

    if (!validatePassword(formData.password)) {
        errors.password = 'Password must be at least 6 characters';
    }

    if (formData.password !== formData.confirmPassword) {
        errors.confirmPassword = 'Passwords do not match';
    }

    return {
        isValid: Object.keys(errors).length === 0,
        errors
    };
};
