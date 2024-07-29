<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login and Register</title>
    <style>
        body {
            background-color: #f0f8ff;
            font-family: Arial, sans-serif;
        }
        #id1 {
            text-align: center;
            margin-bottom: 50px;
        }
        .form-container {
            max-width: 400px;
            margin: auto;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .form-container h2 {
            text-align: center;
            margin-bottom: 20px;
        }
        .form-container input[type="text"],
        .form-container input[type="email"],
        .form-container input[type="password"],
        .form-container select {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ddd;
            border-radius: 5px;
            box-sizing: border-box;
        }
        .form-container button {
            width: 100%;
            padding: 10px;
            background-color: #28a745;
            border: none;
            color: white;
            font-size: 16px;
            border-radius: 5px;
            cursor: pointer;
        }
        .form-container button:hover {
            background-color: #218838;
        }
        .toggle-links {
            text-align: center;
            margin-top: 10px;
        }
    </style>
</head>
<body>
    <h1 id="id1">Welcome to Library Management System</h1>
    <div class="form-container">
        <div id="loginForm">
            <h2>Login</h2>
            <form action="user" method="post">
                <input type="hidden" name="action" value="login">
                <input type="email" name="email" placeholder="Email" required><br>
                <input type="password" name="password" placeholder="Password" required><br>
                <select name="userType" required>
                    <option value="" disabled selected>Select User Type</option>
                    <option value="Student">Student</option>
                    <option value="Librarian">Librarian</option>
                </select><br>
                <button type="submit">Login</button>
            </form>
            <div class="toggle-links">
                <a href="#" onclick="showRegisterForm()">Don't have an account? Register here</a>
            </div>
        </div>
        <div id="registerForm" style="display: none;">
            <h2>Register</h2>
            <form action="user" method="post">
                <input type="hidden" name="action" value="register">
                <input type="text" name="name" placeholder="Name" required><br>
                <input type="email" name="email" placeholder="Email" required><br>
                <input type="password" name="password" placeholder="Password" required><br>
                <select name="userType" required>
                    <option value="" disabled selected>Select User Type</option>
                    <option value="Student">Student</option>
                    <option value="Librarian">Librarian</option>
                </select><br>
                <button type="submit">Register</button>
            </form>
            <div class="toggle-links">
                <a href="#" onclick="showLoginForm()">Already have an account? Login here</a>
            </div>
        </div>
    </div>
    <script>
        function showRegisterForm() {
            document.getElementById('loginForm').style.display = 'none';
            document.getElementById('registerForm').style.display = 'block';
        }
        function showLoginForm() {
            document.getElementById('loginForm').style.display = 'block';
            document.getElementById('registerForm').style.display = 'none';
        }
    </script>
</body>
</html>
