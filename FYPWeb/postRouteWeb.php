<?php

	if (session_status() == PHP_SESSION_NONE) {
		session_start();
	}

	$con=mysqli_connect('localhost', 'root', '', 'bustrackerdb');
	mysqli_select_db($con, "bustrackerdb");
	
	if(isset($_POST['routeRouteId']) && isset($_POST['routeRouteNo']) && isset($_POST['routeRouteName'])){
		$routeRouteId = $_POST['routeRouteId'];
		$routeRouteNo = $_POST['routeRouteNo'];
		$routeRouteName = $_POST['routeRouteName'];
		
		$result = mysqli_query($con, "SELECT * FROM route WHERE id='".$routeRouteId."'");
		if(mysqli_fetch_array($result) != false){
			mysqli_query($con, "UPDATE route SET routeNo='".$routeRouteNo."', routeName='".$routeRouteName."'WHERE id='".$routeRouteId."'");
		}else{
			mysqli_query($con, "insert into route(routeNo, routeName) values('{$routeRouteNo}', '{$routeRouteName}')");
		}
		
		header('Location:home.php');
	}

?>