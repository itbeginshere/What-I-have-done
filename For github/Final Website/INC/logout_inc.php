<?php

session_start();
//unsets all the session global variables
session_unset();
//kills the current session
session_destroy();
header("location: ../PHP/HomePage.php");


