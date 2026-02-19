
import { FaInfoCircle, FaCheckCircle, FaExclamationTriangle, FaTimesCircle } from 'react-icons/fa';

const Alert = ({ type = 'info', message, onClose }) => {
    const types = {
        info: {
            icon: <FaInfoCircle className="text-blue-500" />,
            bg: 'bg-blue-100',
            text: 'text-blue-700',
            border: 'border-blue-400'
        },
        success: {
            icon: <FaCheckCircle className="text-green-500" />,
            bg: 'bg-green-100',
            text: 'text-green-700',
            border: 'border-green-400'
        },
        warning: {
            icon: <FaExclamationTriangle className="text-yellow-500" />,
            bg: 'bg-yellow-100',
            text: 'text-yellow-700',
            border: 'border-yellow-400'
        },
        error: {
            icon: <FaTimesCircle className="text-red-500" />,
            bg: 'bg-red-100',
            text: 'text-red-700',
            border: 'border-red-400'
        }
    };

    const { icon, bg, text, border } = types[type];

    return (
        <div className={`${bg} ${text} ${border} border px-4 py-3 rounded relative mb-4`} role="alert">
            <div className="flex items-center">
                <span className="mr-2">{icon}</span>
                <span className="block sm:inline">{message}</span>
                {onClose && (
                    <button
                        onClick={onClose}
                        className="absolute top-0 bottom-0 right-0 px-4 py-3"
                    >
                        <FaTimesCircle className="h-4 w-4" />
                    </button>
                )}
            </div>
        </div>
    );
};

export default Alert;
