<?php
		if(!isset($_SESSION)){ 
			session_start(); 
	} 
		
		unset($_SESSION['infoId']);
		unset($_SESSION['routeNo']);
		unset($_SESSION['routeName']);
		unset($_SESSION['bus']);
		unset($_SESSION['waypoint']);
		unset($_SESSION['stopNames']);
		
		unset($_SESSION['newsId']);
		unset($_SESSION['newsTitle']);
		unset($_SESSION['newsDesc']);
		unset($_SESSION['newsContent']);
		unset($_SESSION['date']);
		unset($_SESSION['cancelledRoute']);
		unset($_SESSION['cancelledBus']);
		unset($_SESSION['fromDate']);
		unset($_SESSION['toDate']);
		unset($_SESSION['fromTime']);
		unset($_SESSION['toTime']);
		
		unset($_SESSION['scheduleId']);
		unset($_SESSION['scheduleRoute']);
		unset($_SESSION['scheduleBus']);
		unset($_SESSION['scheduleTopNote']);
		unset($_SESSION['scheduleBottomNote']);
		unset($_SESSION['scheduleTimetable']);
		unset($_SESSION['scheduleDate']);
		
		unset($_SESSION['userId']);
		unset($_SESSION['userUsername']);
		unset($_SESSION['userPassword']);
		unset($_SESSION['userPrivilege']);
		unset($_SESSION['userDefaultRoute']);
		unset($_SESSION['userDefaultBus']);
		
		unset($_SESSION['routeRouteId']);
		unset($_SESSION['routeRouteNo']);
		unset($_SESSION['routeRouteName']);
		
		unset($_SESSION['busBusId']);
		unset($_SESSION['busBusNo']);
		unset($_SESSION['busBusNoPlate']);
		
		unset($_SESSION['link']);
		
		unset($_SESSION['usernameDuplicate']);
		unset($_SESSION['loginNotFound']);
		
		if(isset($_POST['user'])){
			unset($_SESSION['user']);
		}
?>