<?php
 
$con=mysqli_connect('localhost', 'root', '', 'bustrackerdb');
mysqli_select_db($con, "bustrackerdb");
 
date_default_timezone_set('Asia/Kuala_Lumpur');

 
$APIKey = "AIzaSyB0LkM8R2FvUI-w09blhkk5Fsjs0aLGiIc";

$routeArray = array();
$timetableArray = array();
$topNoteArray = array();

$regIdArray = array();
$notifyRouteNoArray = array();

$routeNo = array();

//no. of starting times
$beginTime = array();

	//get regId and notifyRouteNo
	$gcmResult = mysqli_query($con, "SELECT * from gcm");
		if(!empty($gcmResult)){
			while($row = mysqli_fetch_array($gcmResult)){
				$regIdArray[] = $row['regId'];
				$notifyRouteNoArray[] = $row['notifyRouteNo'];
			}
			for($i = 0; $i < count($notifyRouteNoArray); $i++){
				if(strpos($notifyRouteNoArray[$i], '|') !== FALSE){
					$routeNo[$i] = explode('|', $notifyRouteNoArray[$i]);
				}else{
					if(strlen($notifyRouteNoArray[$i]) > 0){
						$routeNo[$i][0] = trim($notifyRouteNoArray[$i]);
					}
				}
			}
		}else{
			echo 'Empty table';
		}

	if(isset($_POST['busNotifyRouteNo'])){
		$busNotifyRouteNo = $_POST['busNotifyRouteNo'];
		for($k = 0; $k < count($regIdArray); $k++){
			if(!empty($notifyRouteNoArray[$k])){
				for($i = 0; $i < count($routeNo); $i++){
					if(!empty($routeNo[$i])){
						for($j = 0; $j < count($routeNo[$i]); $j++){
							if($routeNo[$i][$j] == $busNotifyRouteNo){
								echo sendMessageToPhone($regIdArray[$k], "lol", "Route ".$routeNo[$i][$j]." has started.", $APIKey);
							}
						}
					}
				}
			}
		}
		
	}else{
		//get all timetables of routes
		$scheduleResult = mysqli_query($con, "SELECT * from schedule");
		if(!empty($scheduleResult)){
				while($row = mysqli_fetch_array($scheduleResult)){
					$routeArray[] = $row['route'];
					$timetableArray[] = $row['timetable'];
					$topNoteArray[] = $row['topNote'];
				}
				for($n = 0; $n < count($regIdArray); $n++){
					if(!empty($notifyRouteNoArray[$n])){
						for($i = 0; $i < count($routeNo); $i++){
							if(!empty($routeNo[$i])){
								for($j = 0; $j < count($routeArray); $j++){
									//allocate notifications to correct schedule days. e.g. weekdays or saturdays.
									if(date("w") == 6 && (strpos($topNoteArray[$j], "Saturday") !== FALSE || strpos($topNoteArray[$j], "Saturdays") !== FALSE) !== FALSE){
										$temp = explode(":", $routeArray[$j]);
										$route = trim(substr($temp[0], 6));
										if($routeNo[$i][$j] == $route){
											$timetableRow = explode("/", $timetableArray[$j]);
											for($k = 0; $k < count($timetableRow); $k++){
												$timetableCol = explode("|", $timetableRow[$k]);
												for($l = 0; $l < count($timetableCol); $l++){
													if(strpos($timetableCol[$l], "AM") || strpos($timetableCol[$l], "PM")){
														$startTime = strtotime($timetableCol[$l]);
														$notifyTime = strtotime("+30 minutes");
														
														if(date('g:i A', $notifyTime) == date('g:i A', $startTime)){
															echo sendMessageToPhone($regIdArray[$n], "lol", "Route ".$routeNo[$i][$j]." will begin in 30 minutes.", $APIKey);
														}
														break;
													}
												}
											}
										}
									}else if(date("w") != 6 && date("w") != 7 && (strpos($topNoteArray[$j], "Saturday") === FALSE || strpos($topNoteArray[$j], "Saturdays") === FALSE) !== FALSE){
										$temp = explode(":", $routeArray[$j]);
										$route = trim(substr($temp[0], 6));
										if($routeNo[$i][$j] == $route){
											$timetableRow = explode("/", $timetableArray[$j]);
											for($k = 0; $k < count($timetableRow); $k++){
												$timetableCol = explode("|", $timetableRow[$k]);
												for($l = 0; $l < count($timetableCol); $l++){
													if(strpos($timetableCol[$l], "AM") || strpos($timetableCol[$l], "PM")){
														$startTime = strtotime($timetableCol[$l]);
														$notifyTime = strtotime("+30 minutes");
														
														if(date('g:i A', $notifyTime) == date('g:i A', $startTime)){
															echo sendMessageToPhone($regIdArray[$n], "lol", "Route ".$routeNo[$i][$j]." will begin in 30 minutes.", $APIKey);
														}
														break;
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
				
				
				/*for($z = 0; $z < count($routeNo); $z++){
					if(!empty($routeNo[$z])){
						for($y = 0; $y < count($routeNo[$z]); $y++){
							for($i = 0; $i < count($routeArray); $i++){
								//allocate notifications to correct schedule days. e.g. weekdays or saturday.
								if(date("w") == 0) {
									
								}else if(date("w") == 6 && strpos($topNoteArray[$i], 'Saturday') !== FALSE){
									$temp = explode(':', $routeArray[$i]);
									$route = trim(substr($temp[0], 6));
									if($routeNo[$z][$y] == $route){
										//echo $routeNo[$z][$y].' = '. $route.' ';
										$timetableRow = explode('/', $timetableArray[$i]);
										$beginTime = explode('/', $timetableArray[$i]);
											
										for($j = 0; $j < count($timetableRow); $j++){
											$timetableCol = explode('|', $timetableRow[$j]);
											for($k = 0; $k < count($timetableCol); $k++){
												if(strpos($timetableCol[$k], 'AM') || strpos($timetableCol[$k], 'PM')){
													$startTime = strtotime($timetableCol[$k]);
													$notifyTime30 = strtotime('+30 minutes');
													//$notifyTime29 = strtotime('+29 minutes');
													//$notifyTime30 = strtotime('3:00 PM');
													//$notifyTime29 = strtotime('3:00 PM');
													//echo date('g:i A', $notifyTime30).'='.date('g:i A', $startTime).' ';
													//if(date('g:i A', $notifyTime30) == date('g:i A', $startTime) || date('g:i A', $notifyTime29) == date('g:i A', $startTime)){
													if(date('g:i A', $notifyTime30) == date('g:i A', $startTime)){
														for($i = 0; $i < count($regIdArray); $i++){
															if($notifyRouteNoArray[$i] != ''){
																echo sendMessageToPhone($regIdArray[$i], "lol", "Route ".$routeNo[$z][$y]." will begin in 30 minutes.", $APIKey);
															}
														}
													}
													break;
												}
											}
										}
									}
								}else if(date("w") != 6 && strpos($topNoteArray[$i], 'Saturday') !== FALSE){
									
								}else{
									$temp = explode(':', $routeArray[$i]);
									$route = trim(substr($temp[0], 6));
									if($routeNo[$z][$y] == $route){
										$timetableRow = explode('/', $timetableArray[$i]);
										$beginTime = explode('/', $timetableArray[$i]);
											
										for($j = 0; $j < count($timetableRow); $j++){
											$timetableCol = explode('|', $timetableRow[$j]);
											for($k = 0; $k < count($timetableCol); $k++){
												if(strpos($timetableCol[$k], 'AM') || strpos($timetableCol[$k], 'PM')){
													$startTime = strtotime($timetableCol[$k]);
													$notifyTime30 = strtotime('+30 minutes');
													//$notifyTime29 = strtotime('+29 minutes');
													//$notifyTime30 = strtotime('3:00 PM');
													//$notifyTime29 = strtotime('3:00 PM');
													//echo date('g:i A', $notifyTime30).'='.date('g:i A', $startTime).' ';
													//if(date('g:i A', $notifyTime30) == date('g:i A', $startTime) || date('g:i A', $notifyTime29) == date('g:i A', $startTime)){
													if(date('g:i A', $notifyTime30) == date('g:i A', $startTime)){
														for($i = 0; $i < count($regIdArray); $i++){
															if($notifyRouteNoArray[$i] != ''){
																echo sendMessageToPhone($regIdArray[$i], "lol", "Route ".$routeNo[$z][$y]." will begin in 30 minutes.", $APIKey);
															}
														}
													}
													break;
												}
											}
										}
									}
								}
							}
						}
					}
				}*/
		}else{
			echo 'Empty table';
		}
	}

	function sendMessageToPhone($deviceToken, $collapseKey, $messageText, $yourKey) {    
		echo "DeviceToken:".$deviceToken."Key:".$collapseKey."Message:".$messageText
				."API Key:".$yourKey."Response"."<br/>";

		$headers = array('Authorization:key=' . $yourKey);    
		$data = array(    
			'registration_id' => $deviceToken,          
			'collapse_key' => $collapseKey,
			'data.message' => $messageText);  
		$ch = curl_init();    

		curl_setopt($ch, CURLOPT_URL, "https://android.googleapis.com/gcm/send");    
		if ($headers)    
			curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);    
		curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);    
		curl_setopt($ch, CURLOPT_POST, true);    
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);    
		curl_setopt($ch, CURLOPT_POSTFIELDS, $data);    

		$response = curl_exec($ch);    
		var_dump($response);
		$httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);    
		if (curl_errno($ch)) {
			return false;
		}    
		if ($httpCode != 200) {
			return false;
		}    
		curl_close($ch);    
		return $response;    
	}  
 
?>