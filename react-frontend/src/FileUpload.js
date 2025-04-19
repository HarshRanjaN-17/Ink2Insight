import React, { useState } from 'react';
import './FileUpload.css';

const FileUpload = () => {
    const [file, setFile] = useState(null);
    const [response, setResponse] = useState("");

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
        setResponse("");
    };

    const handleUpload = async () => {
        if (!file) {
            alert("Please select a file first!");
            return;
        }

        const formData = new FormData();
        formData.append("file", file);

        try {
            const res = await fetch("http://localhost:8080/api/upload", {
                method: "POST",
                body: formData,
            });

            const data = await res.text();
            setResponse(data);
        } catch (err) {
            console.error(err);
            setResponse("Upload failed! Please try again.");
        }
    };

    return (
        <div className="upload-container">
            <h2 className="upload-heading">📑 Insurance Claim File Upload</h2>
            <p className="upload-subtext">
                Upload a claim form (PDF or JPG) to extract key information using AI.
            </p>

            <input
                type="file"
                accept=".pdf, .jpg, .jpeg, .png"
                onChange={handleFileChange}
                className="upload-input"
            />

            <br />
            <button onClick={handleUpload} className="upload-button">🚀 Upload</button>

            {response && (
                <div className="upload-response">
                    <strong>📝 Server Response:</strong>
                    <p>{response}</p>
                </div>
            )}
        </div>
    );
};

export default FileUpload;
