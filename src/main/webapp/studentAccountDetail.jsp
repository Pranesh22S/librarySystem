<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Student Account Details</title>
</head>
<body>
<form>
    <h2>Account Information</h2>
    <p>Name: ${userName}</p>
    <p>Mail: ${mail}</p>
    <p>Number of Books Loaned: ${numberOfBooksLoaned}</p>
    <p>Number of Books Returned: ${numberOfBooksReturned}</p>
    <p>Fine Amount: ${fineAmount}</p>
    </form>
</body>
</html>