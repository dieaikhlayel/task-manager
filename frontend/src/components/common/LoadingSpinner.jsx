
const LoadingSpinner = ({ size = 'md', color = 'blue' }) => {
    const sizes = {
        sm: 'h-4 w-4',
        md: 'h-8 w-8',
        lg: 'h-12 w-12',
        xl: 'h-16 w-16'
    };

    const colors = {
        blue: 'border-blue-600',
        red: 'border-red-600',
        green: 'border-green-600',
        yellow: 'border-yellow-600'
    };

    return (
        <div className="flex justify-center items-center">
            <div
                className={`${sizes[size]} border-4 border-t-transparent ${colors[color]} rounded-full animate-spin`}
            ></div>
        </div>
    );
};

export default LoadingSpinner;
