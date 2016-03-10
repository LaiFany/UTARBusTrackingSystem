<?php

	if (session_status() == PHP_SESSION_NONE) {
		session_start();
	}

	$con=mysqli_connect('localhost', 'root', '', 'bustrackerdb');
	mysqli_select_db($con, "bustrackerdb");
	
	if(isset($_POST['userId']) && isset($_POST['username']) && isset($_POST['password']) && isset($_POST['privilege'])){
		$userId = $_POST['userId'];
		$username = $_POST['username'];
		$password = $_POST['password'];
		$privilege = $_POST['privilege'];
		$defaultRoute = '';
		$defaultBus = '';
		if(isset($_POST['defaultRoute'])){
			$defaultRoute = $_POST['defaultRoute'];
		}
		if(isset($_POST['defaultBus'])){
			$defaultBus = $_POST['defaultBus'];
		}
		
		$editResult = mysqli_query($con, "SELECT * FROM user WHERE id='".$userId."'");
		$addResult = mysqli_query($con, "SELECT * FROM user WHERE username='".$username."'");
		if(mysqli_fetch_array($editResult) != false){
			mysqli_query($con, "UPDATE user SET username='".$username."', password='".$password."', privilege='".$privilege."', defaultRoute='".$defaultRoute."', defaultBus='".$defaultBus."' WHERE id='".$userId."'");
			header('Location:home.php');
		}else{
			if(mysqli_fetch_array($addResult) != false){
				$_SESSION['usernameDuplicate'] = 'true';
				header('Location:userForm.php');
			}else{
				mysqli_query($con, "insert into user(username, password, privilege, defaultRoute, defaultBus) values('{$username}', '{$password}', '{$privilege}', '{$defaultRoute}', '{$defaultBus}')");
				header('Location:home.php');
			}
		}
	}

?>