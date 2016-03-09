<?php

	if (session_status() == PHP_SESSION_NONE) {
		session_start();
	}

	$con=mysqli_connect('localhost', 'root', '', 'bustrackerdb');
	mysqli_select_db($con, "bustrackerdb");
	
	if(isset($_POST['username']) && isset($_POST['password']) && isset($_POST['privilege'])){
		$username = $_POST['username'];
		$password = $_POST['password'];
		$privilege = $_POST['privilege'];
		
		$result = mysqli_query($con, "SELECT * FROM user WHERE username='".$username."'");
		if(mysqli_fetch_array($result) != false){
			$_SESSION['usernameDuplicate'] = 'true';
			header('Location:userForm.php');
		}else{
			mysqli_query($con, "insert into user(username, password, privilege) values('{$username}', '{$password}', '{$privilege}')");
			header('Location:home.php');
		}
		
	
	}

?>