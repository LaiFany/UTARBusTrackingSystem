<?php

	//header('Location:userForm.php');
	
	if(!isset($_SESSION)){ 
			session_start(); 
	} 
	
	if(isset($_POST['infoId']) && isset($_POST['routeNo']) && isset($_POST['routeName']) && isset($_POST['bus']) && isset($_POST['waypoint']) && isset($_POST['stopNames'])){
		$_SESSION['infoId'] = $_POST['infoId'];
		$_SESSION['routeNo'] = $_POST['routeNo'];
		$_SESSION['routeName'] = $_POST['routeName'];
		$_SESSION['bus'] = $_POST['bus'];
		$_SESSION['waypoint'] = $_POST['waypoint'];
		$_SESSION['stopNames'] = $_POST['stopNames'];
	}
	
	if(isset($_POST['newsId']) && isset($_POST['newsTitle']) && isset($_POST['newsDesc']) && isset($_POST['newsContent']) && isset($_POST['date']) && isset($_POST['cancelledRoute']) && isset($_POST['cancelledBus']) && isset($_POST['fromDate']) && isset($_POST['toDate']) && isset($_POST['fromTime']) && isset($_POST['toTime'])){
		$_SESSION['newsId'] = $_POST['newsId'];
		$_SESSION['newsTitle'] = $_POST['newsTitle'];
		$_SESSION['newsDesc'] = $_POST['newsDesc'];
		$_SESSION['newsContent'] = $_POST['newsContent'];
		$_SESSION['date'] = $_POST['date'];
		$_SESSION['cancelledRoute'] = $_POST['cancelledRoute'];
		$_SESSION['cancelledBus'] = $_POST['cancelledBus'];
		$_SESSION['fromDate'] = $_POST['fromDate'];
		$_SESSION['toDate'] = $_POST['toDate'];
		$_SESSION['fromTime'] = $_POST['fromTime'];
		$_SESSION['toTime'] = $_POST['toTime'];
	}
	
	if(isset($_POST['scheduleId']) && isset($_POST['scheduleRoute']) && isset($_POST['scheduleBus']) && isset($_POST['scheduleTopNote']) && isset($_POST['scheduleBottomNote']) && isset($_POST['scheduleTimetable']) && isset($_POST['scheduleDate'])){
		$_SESSION['scheduleId'] = $_POST['scheduleId'];
		$_SESSION['scheduleRoute'] = $_POST['scheduleRoute'];
		$_SESSION['scheduleBus'] = $_POST['scheduleBus'];
		$_SESSION['scheduleTopNote'] = $_POST['scheduleTopNote'];
		$_SESSION['scheduleBottomNote'] = $_POST['scheduleBottomNote'];
		$_SESSION['scheduleTimetable'] = $_POST['scheduleTimetable'];
		$_SESSION['scheduleDate'] = $_POST['scheduleDate'];
	}
	
	if(isset($_POST['userId']) && isset($_POST['userUsername']) && isset($_POST['userPassword']) && isset($_POST['userPrivilege']) && isset($_POST['userDefaultRoute']) && isset($_POST['userDefaultBus'])){
		$_SESSION['userId'] = $_POST['userId'];
		$_SESSION['userUsername'] = $_POST['userUsername'];
		$_SESSION['userPassword'] = $_POST['userPassword'];
		$_SESSION['userPrivilege'] = $_POST['userPrivilege'];
		$_SESSION['userDefaultRoute'] = $_POST['userDefaultRoute'];
		$_SESSION['userDefaultBus'] = $_POST['userDefaultBus'];
	}
	
	if(isset($_POST['routeRouteId']) && isset($_POST['routeRouteNo']) && isset($_POST['routeRouteName'])){
		$_SESSION['routeRouteId'] = $_POST['routeRouteId'];
		$_SESSION['routeRouteNo'] = $_POST['routeRouteNo'];
		$_SESSION['routeRouteName'] = $_POST['routeRouteName'];
	}
	
	if(isset($_POST['busBusId']) && isset($_POST['busBusNo']) && isset($_POST['busBusNoPlate'])){
		$_SESSION['busBusId'] = $_POST['busBusId'];
		$_SESSION['busBusNo'] = $_POST['busBusNo'];
		$_SESSION['busBusNoPlate'] = $_POST['busBusNoPlate'];
	}
	
	if(isset($_POST['sessionLink'])){
		$_SESSION['link'] = $_POST['sessionLink'];
	}
?>