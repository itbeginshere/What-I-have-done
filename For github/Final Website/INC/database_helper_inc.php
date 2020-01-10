<?php 
//Variable decalration and assignment
$serverName = "localhost"; //Name of the server being used
$dbUsername = "root"; //Name of the user name logged into the database
$dbPassword = ""; //Password which is associated with the username
$dbName = "web_database"; //Name of the database to be used

//Opens a new connection to the MySQL server
$conn = mysqli_connect($serverName,$dbUsername,$dbPassword,$dbName);

if(!$conn){
	//If the connection is broken or incomplete the script exits and displays an error message 
	die("Connection failed: " .mysqli_connect_error());	
}