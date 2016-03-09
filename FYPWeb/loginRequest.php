<?php

	if (session_status() == PHP_SESSION_NONE) {
		session_start();
	}

	function testInput($data) {
		 $data = trim($data);
		 $data = stripslashes($data);
	     $data = htmlspecialchars($data);
		 return $data;
	}
	
	$con=mysqli_connect('localhost', 'root', '', 'bustrackerdb');
	mysqli_select_db($con, "bustrackerdb");
	
	if(isset($_POST['username']) && isset($_POST['password'])){
		$username = testInput($_POST['username']);
		$password = testInput($_POST['password']);
		
		$result = mysqli_query($con, "SELECT * FROM user WHERE username='".$username."' AND password='".$password."' AND privilege='admin'");
		if(mysqli_fetch_array($result) != false){
			$_SESSION['user'] = $username;
			header('Location:home.php');
		}else{
			$_SESSION['loginNotFound'] = 'true';
			header('Location:login.php');
		}
	}
?>