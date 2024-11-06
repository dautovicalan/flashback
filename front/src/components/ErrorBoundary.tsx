import React from "react";
import { Link } from "react-router-dom";

type ErrorScreenProps = {
  message?: string;
};

export const ErrorScreen = ({ message }: ErrorScreenProps) => {
  return (
    <div className="flex items-center justify-center h-screen">
      <div className="text-center">
        <div className="text-red-500 text-6xl">
          <i className="fas fa-exclamation-triangle"></i>
        </div>
        {message && (
          <>
            <h1 className="text-3xl font-bold mt-4">Error</h1>
            <p className="mt-2">{message}</p>
            <Link to="/" className="btn btn-primary">
              Go Back Home
            </Link>
          </>
        )}

        {!message && (
          <>
            <h1 className="text-3xl font-bold mt-4">Something went wrong.</h1>
            <p className="mt-2">
              Please try refreshing the page or contact support if the problem
              persists.
            </p>
            <button
              className="btn btn-primary mt-6"
              onClick={
                () => window.location.reload() // eslint-disable
              }
            >
              Refresh
            </button>
          </>
        )}
      </div>
    </div>
  );
};

type ErrorBoundaryProps = {
  children: React.ReactNode;
};

class ErrorBoundary extends React.Component<ErrorBoundaryProps> {
  state = { hasError: false, error: null };

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  static getDerivedStateFromError(error: any) {
    return { hasError: true, error };
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  componentDidCatch(error: any, errorInfo: any) {
    console.error("ErrorBoundary caught an error:", error, errorInfo);
  }

  render() {
    if (this.state.hasError) {
      return <ErrorScreen />;
    }

    return this.props.children;
  }
}

export default ErrorBoundary;
