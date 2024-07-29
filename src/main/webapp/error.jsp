<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Error Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
        }
        .error-message {
            color: red;
            font-size: 24px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <h1>Error Occurred</h1>
    <p class="error-message">${param.message}</p>
    <p>Sorry, an error occurred while processing your request.</p>
    <p>Please try again or contact the administrator if the issue persists.</p>
    <a href="loginregister.jsp">Go back to login page</a>
</body>
</html>