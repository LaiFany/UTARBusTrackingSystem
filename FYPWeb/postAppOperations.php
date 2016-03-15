<?php

	$con=mysqli_connect('localhost', 'root', '', 'bustrackerdb');
	mysqli_select_db($con, "bustrackerdb");
	
	if(isset($_POST["routeNo"]) && isset($_POST["routeName"]) && isset($_POST["bus"]) && isset($_POST["waypoint"]) && isset($_POST["stopNames"])){
		$routeNo = $_POST['routeNo'];
		$routeName = $_POST['routeName'];
		$bus = $_POST['bus'];
		$waypoint = $_POST["waypoint"];
		$stopNames = $_POST["stopNames"];
		
		$result = mysqli_query($con, "SELECT * FROM info WHERE routeNo='".$routeNo."'");
		if(mysqli_fetch_array($result) != false){
			mysqli_query($con, "UPDATE info SET waypoint='".$waypoint."', stopNames='".$stopNames."' WHERE routeNo='".$routeNo."'");
		}
	}
	
	if(isset($_POST["routeNo"]) && isset($_POST["routeName"]) && isset($_POST["bus"]) && isset($_POST["lat"]) && isset($_POST["lng"])){
		$routeNo = $_POST['routeNo'];
		$routeName = $_POST['routeName'];
		$bus = $_POST['bus'];
		$lat = $_POST['lat'];
		$lng = $_POST['lng'];
		
		mysqli_query($con, "insert into location(routeNo, routeName, bus, lat, lng) values('{$routeNo}', '{$routeName}', '{$bus}', '{$lat}', '{$lng}')");
	}
	else{
		echo "Missing required fields";
	}
	
	if(isset($_POST['regId'])){
		$regId = $_POST['regId'];
		$notifyRouteNo = '';
		if(isset($_POST['notifyRouteNo'])){
			$notifyRouteNo = $_POST['notifyRouteNo'];
		}
		
		$result = mysqli_query($con, "SELECT * FROM gcm WHERE regId='".$regId."'");
		if(mysqli_fetch_array($result) != false){
			mysqli_query($con, "UPDATE gcm SET notifyRouteNo='".$notifyRouteNo."' WHERE regId='".$regId."'");
		}else{
			mysqli_query($con, "insert into gcm(regId, notifyRouteNo) values('{$regId}', '{$notifyRouteNo}')");
		}
	}else{
		echo "Missing required fields";
	}

?>