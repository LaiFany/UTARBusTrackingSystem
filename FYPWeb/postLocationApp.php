<?php

	$con=mysqli_connect('localhost', 'root', '', 'bustrackerdb');
	mysqli_select_db($con, "bustrackerdb");
	
	$routeNo = $_POST['routeNo'];
	$routeName = $_POST['routeName'];
	$bus = $_POST['bus'];
	$lat = $_POST['lat'];
	$lng = $_POST['lng'];
	//$time = $_POST['time'];
	$passengers = $_POST['passengers'];
	
	mysqli_query($con, "insert into location(routeNo, routeName, bus, lat, lng, passengers) values('{$routeNo}', '{$routeName}', '{$bus}', '{$lat}', '{$lng}', '{$passengers}')");

?>