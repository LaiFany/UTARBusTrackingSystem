<?php

	$con=mysqli_connect('localhost', 'root', '', 'bustrackerdb');
	mysqli_select_db($con, "bustrackerdb");
	
	if(isset($_POST["scheduleId"]) && isset($_POST["route"]) && isset($_POST["bus"])){
		$scheduleId = trim($_POST['scheduleId']);
		$route = trim($_POST['route']);
		$bus = trim($_POST['bus']);
		$topNote = trim($_POST['topNote']);
		$bottomNote = trim($_POST['bottomNote']);
		$timetable = '';
		
		//schedule boxes
		
		for($i = 1; $i < 16; $i++){
			if($timetable != '' && substr($timetable, -1) != '|' && substr($timetable, -1) != '/'){
				$timetable .= '/';
			}
			$row = 'r'.$i;
			for($j = 1; $j < 12; $j++){
				$col = 'c'.$j;
				${$row.$col} = trim($_POST[$row.$col]);
				if(${$row.$col} != ''){
					if($timetable != '' && substr($timetable, -1) != '|' && substr($timetable, -1) != '/'){
						$timetable .= '|'.${$row.$col};
					}else{
						$timetable .= ${$row.$col};
					}
				}
			}
		}
		
		//get current date
		// Return date/time info of a timestamp; then format the output
		$mydate=getdate(date("U"));
		$date = $mydate[month]." ".$mydate[mday].", ". $mydate[year];
		
		$result = mysqli_query($con, "SELECT * FROM schedule WHERE id='".$scheduleId."'");
		if(mysqli_fetch_array($result) != false){
			mysqli_query($con, "UPDATE schedule SET route='".$route."', bus='".$bus."', date='".$date."', topNote='".$topNote."', bottomNote='".$bottomNote."', timetable='".$timetable."' WHERE id='".$scheduleId."'");
		}else{
			mysqli_query($con, "insert into schedule(route, bus, date, topNote, bottomNote, timetable) values('{$route}', '{$bus}', '{$date}', '{$topNote}', '{$bottomNote}', '{$timetable}')");
		}
		
		header("Location:home.php");
	}
	else{
		echo "Missing required fields";
	}
	
	function parseSchedule(){
		
	}

?>