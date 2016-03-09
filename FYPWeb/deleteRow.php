<?php
	$con=mysqli_connect('localhost', 'root', '', 'bustrackerdb');
	mysqli_select_db($con, "bustrackerdb");
	
	if(isset($_POST['infoId']) && isset($_POST['routeNo']) && isset($_POST['routeName']) && isset($_POST['bus'])){
		$infoId = $_POST['infoId'];
		$routeNo = $_POST['routeNo'];
		$routeName = $_POST['routeName'];
		$bus = $_POST['bus'];
		
		mysqli_query($con, "DELETE FROM info WHERE id='".$infoId."'");
		
	}else if(isset($_POST['newsId']) && isset($_POST['newsTitle']) && isset($_POST['newsContent']) && isset($_POST['date'])){
		$newsId = $_POST['newsId'];
		$newsTitle = $_POST['newsTitle'];
		$newsContent = $_POST['newsContent'];
		$date = $_POST['date'];
		
		mysqli_query($con, "DELETE FROM news WHERE id='".$newsId."'");
		
	}else if(isset($_POST['scheduleId']) && isset($_POST['scheduleRoute']) && isset($_POST['scheduleBus']) && isset($_POST['scheduleDate'])){
		$scheduleId = $_POST['scheduleId'];
		$scheduleRoute = $_POST['scheduleRoute'];
		$scheduleBus = $_POST['scheduleBus'];
		$scheduleDate = $_POST['scheduleDate'];
		
		mysqli_query($con, "DELETE FROM schedule WHERE id='".$scheduleId."'");
		
	}else if(isset($_POST['userId'])){
		$userId = $_POST['userId'];
		
		mysqli_query($con, "DELETE FROM user WHERE id='".$userId."'");
	}
?>