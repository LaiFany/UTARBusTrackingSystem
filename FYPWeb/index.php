<html>
	<head>
		<title>Home</title>
		  <meta charset="utf-8">
		  <meta name="viewport" content="width=device-width, initial-scale=1">
		  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
		  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
		  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
	</head>
	<body>
		<div class="container">
<?php
	//session
	if(!isset($_SESSION)){ 
		session_start(); 
	} 
	if(!isset($_SESSION['user'])){
		header('Location:login.php');
	}
	include 'unsetSession.php';
	
	//navbar
	include 'navBar.php';

	$con=mysqli_connect('localhost', 'root', '', 'bustrackerdb');
	mysqli_select_db($con, "bustrackerdb");
	
	//get updated data in route table
	$routeResult = mysqli_query($con, "SELECT * FROM route");
	
	//get updated data in bus table
	$busResult = mysqli_query($con, "SELECT * FROM bus");
	
	//get updated data in user table
	$userResult = mysqli_query($con, "SELECT * FROM user");
	
	//get updated data in info table
	$infoResult = mysqli_query($con, "SELECT * FROM info");
	
	//get updated data in news table
	$newsResult = mysqli_query($con, "SELECT * FROM news");
	
	//get updated data in news table
	$scheduleResult = mysqli_query($con, "SELECT * FROM schedule");
	
	if(!empty($userResult)){
		//create table and populate them with data from info table in server
		?>
			<div class="panel panel-info">
				<div class="panel-heading" style="padding : 20px;">Route Table <button type="button" class="btn btn-info pull-right" id = "addRoute" >Add</button></div>
				<div class="panel-body">
					<div class = "table-responsive">
						<table class = "table table-hover">
							<thead>
								<tr>
									<th>ID</th>
									<th>Route No.</th>
									<th>Route Name</th>
									<th>Options</th>
								</tr>
							</thead>
							<tbody>
							<?php
							while($row = mysqli_fetch_array($routeResult)){
							?>
							
								<tr class = "infoClickable">
									<td class = "routeRouteId"><?php echo $row['id'];?></td>
									<td class = "routeRouteNo"><?php echo $row['routeNo'];?></td>
									<td class = "routeRouteName"><?php echo $row['routeName'];?></td>
									<td class = ""><button type="button" class="btn btn-default routeEdit">Edit</button> <button type="button" class="btn btn-default routeDelete" data-toggle="modal" data-target="#routeModal">Delete</button></td>
								</tr>
								
							<?php
							}?>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		<?php
	}else{
		echo 'route table empty';
	}
	
	if(!empty($busResult)){
		//create table and populate them with data from info table in server
		?>
			<div class="panel panel-info">
				<div class="panel-heading" style="padding : 20px;">Bus Table <button type="button" class="btn btn-info pull-right" id = "addBus" >Add</button></div>
				<div class="panel-body">
					<div class = "table-responsive">
						<table class = "table table-hover">
							<thead>
								<tr>
									<th>ID</th>
									<th>Bus No.</th>
									<th>Bus No. Plate</th>
									<th>Options</th>
								</tr>
							</thead>
							<tbody>
							<?php
							while($row = mysqli_fetch_array($busResult)){
							?>
							
								<tr class = "infoClickable">
									<td class = "busBusId"><?php echo $row['id'];?></td>
									<td class = "busBusNo"><?php echo $row['busNo'];?></td>
									<td class = "busBusNoPlate"><?php echo $row['busNoPlate'];?></td>
									<td class = ""><button type="button" class="btn btn-default busEdit">Edit</button> <button type="button" class="btn btn-default busDelete" data-toggle="modal" data-target="#busModal">Delete</button></td>
								</tr>
								
							<?php
							}?>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		<?php
	}else{
		echo 'bus table empty';
	}
	
	if(!empty($userResult)){
		//create table and populate them with data from info table in server
		?>
			<div class="panel panel-info">
				<div class="panel-heading" style="padding : 20px;">User Table <button type="button" class="btn btn-info pull-right" id = "addUser" >Add</button></div>
				<div class="panel-body">
					<div class = "table-responsive">
						<table class = "table table-hover">
							<thead>
								<tr>
									<th>ID</th>
									<th>Username</th>
									<th>Password</th>
									<th>Privilege</th>
									<th>Default Route</th>
									<th>Default Bus</th>
									<th>Options</th>
								</tr>
							</thead>
							<tbody>
							<?php
							while($row = mysqli_fetch_array($userResult)){
							?>
							
								<tr class = "infoClickable">
									<td class = "userId"><?php echo $row['id'];?></td>
									<td class = "userUsername"><?php echo $row['username'];?></td>
									<td class = "userPassword"><?php echo $row['password'];?></td>
									<td class = "userPrivilege"><?php echo $row['privilege'];?></td>
									<td class = "userDefaultRoute"><?php echo $row['defaultRoute'];?></td>
									<td class = "userDefaultBus"><?php echo $row['defaultBus'];?></td>
									<td class = ""><button type="button" class="btn btn-default userEdit">Edit</button> <button type="button" class="btn btn-default userDelete" data-toggle="modal" data-target="#userModal">Delete</button></td>
								</tr>
								
							<?php
							}?>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		<?php
	}else{
		echo 'user table empty';
	}
	
	if(!empty($infoResult)){
		
		//create table and populate them with data from info table in server
		?>
			<div class="panel panel-info">
				<div class="panel-heading" style="padding : 20px;">Info Table <button type="button" class="btn btn-info pull-right" id = "addInfo" >Add</button></div>
				<div class="panel-body">
					<div class = "table-responsive">
						<table class = "table table-hover">
							<thead>
								<tr>
									<th>ID</th>
									<th>Route No.</th>
									<th>Route Name</th>
									<th>Bus</th>
									<?php
										for($i = 0; $i < 8; $i++){
											$j = $i + 1;
											?>
												<th><?php echo 'Stop '.$j?></th>
											<?php
										}
									?>
									<th style="display : none;">Waypoint Coordinates</th>
									<th style="display : none;">Stop Names</th>
									<th>Options</th>
								</tr>
							</thead>
							<tbody>
							<?php
							while($row = mysqli_fetch_array($infoResult)){
							?>
							
								<tr class = "infoClickable">
									<td class = "infoId"><?php echo $row['id'];?></td>
									<td class = "infoRouteNo"><?php echo $row['routeNo'];?></td>
									<td class = "infoRouteName"><?php echo $row['routeName'];?></td>
									<td class = "infoBus"><?php echo $row['bus'];?></td>
									
									<?php
										$waypoint = '';
										$stopNames = '';
										
										$lat = array('', '', '', '', '', '', '', '');
										$lng = array('', '', '', '', '', '', '', '');
										
										$eachStopName = array('', '', '', '', '', '', '', '');
										
										$waypoint = $row['waypoint'];
										$stopNames = $row['stopNames'];
										
										//split waypoint coordinates
										 if($waypoint != ''){
											 $coordinates = explode('|', $waypoint);
											 $length = sizeOf($coordinates);
											 for($i = 0; $i < $length; $i++){
												 $data = explode(',', $coordinates[$i]);
												 $lat[$i] = $data[0];
												 $lng[$i] = $data[1];
											 }
										 }
										 
										 //split stopNames
										 if(!empty($stopNames)){
											 $stopName = explode('|', $stopNames);
											 $length2 = sizeOf($stopName);
											 for($i = 0; $i < $length2; $i++){
												 $eachStopName[$i] = $stopName[$i];
											 }
										 }
										 for($i = 0; $i < 8; $i++){
											if(!empty($eachStopName[$i])){
											 ?>
												<td><?php echo $eachStopName[$i].' ('.$lat[$i].', '.$lng[$i].')';?></td>
											 <?php
											}else{
											 ?>
												<td></td>
											 <?php
											}
										 }									 
										 
									?>
									<td class = "infoWaypoint" style="display : none;"><?php echo $row['waypoint'];?></td>
									<td class = "infoStopNames" style="display : none;"><?php echo $row['stopNames'];?></td>
									<td class = ""><button type="button" class="btn btn-default infoEdit">Edit</button> <button type="button" class="btn btn-default infoDelete" data-toggle="modal" data-target="#infoModal">Delete</button></td>
								</tr>
								
							<?php
							}?>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		<?php
	}else{
		echo 'info table empty';
	}
	
	if(!empty($scheduleResult)){
		
		//create table and populate them with data from info table in server
		?>
			<div class="panel panel-info">
				<div class="panel-heading" style="padding : 20px;">Schedule Table <button type="button" class="btn btn-info pull-right" id = "addSchedule" >Add</button></div>
				<div class="panel-body">
					<div class = "table-responsive">
						<table class = "table table-hover">
							<thead>
								<tr>
									<th>ID</th>
									<th>Route</th>
									<th>Bus</th>
									<th>Top Note</th>
									<th>Bottom Note</th>
									<th>Timetable</th>
									<th>Date</th>
								</tr>
							</thead>
							<tbody>
							<?php
							while($row = mysqli_fetch_array($scheduleResult)){
							?>
							
								<tr class = "scheduleClickable">
									<td class = "scheduleId"><?php echo $row['id'];?></td>
									<td class = "scheduleRoute"><?php echo $row['route'];?></td>
									<td class = "scheduleBus"><?php echo $row['bus'];?></td>
									<td class = "scheduleTopNote"><?php echo $row['topNote'];?></td>
									<td class = "scheduleBottomNote"><?php echo $row['bottomNote'];?></td>
									<td class = ""><a class = "scheduleEdit" href = "scheduleForm.php">View</a></td>
									<td class = "scheduleTimetable" style="display : none;"><?php echo $row['timetable'];?></td>
									<td class = "scheduleDate"><?php echo $row['date'];?></td>
									<td class = ""><button type="button" class="btn btn-default scheduleEdit">Edit</button> <button type="button" class="btn btn-default scheduleDelete" data-toggle="modal" data-target="#scheduleModal">Delete</button></td>
								</tr>
								
							<?php
							}?>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		<?php
	}else{
		echo 'schedule table empty';
	}
	
	if(!empty($newsResult)){
		
		//create table and populate them with data from info table in server
		?>
			<div class="panel panel-info">
				<div class="panel-heading" style="padding : 20px;">News Table <button type="button" class="btn btn-info pull-right" id = "addNews" >Add</button></div>
				<div class="panel-body">
					<div class = "table-responsive">
						<table class="table table-hover">
							<thead>
								<tr>
									<th>ID</th>
									<th>News Title</th>
									<th>News Description</th>
									<th>News Content</th>
									<th>Date</th>
									<th>Cancelled Route</th>
									<th>Cancelled Bus</th>
									<th>From Date</th>
									<th>To Date</th>
									<th>From Time</th>
									<th>To Time</th>
									<th>Options</th>
								</tr>
							</thead>
							<tbody>
							<?php
							while($row = mysqli_fetch_array($newsResult)){
							?>
							
								<tr class = "newsClickable">
									<td class = "newsId"><?php echo $row['id'];?></td>
									<td class = "newsNewsTitle"><?php echo $row['newsTitle'];?></td>
									<td class = "newsNewsDesc"><?php echo $row['newsDesc'];?></td>
									<td class = "newsNewsContent"><?php echo $row['newsContent'];?></td>
									<td class = "newsDate"><?php echo $row['date'];?></td>
									<td class = "cancelledRoute"><?php echo $row['cancelledRoute'];?></td>
									<td class = "cancelledBus"><?php echo $row['cancelledBus'];?></td>
									<td class = "fromDate"><?php echo $row['fromDate'];?></td>
									<td class = "toDate"><?php echo $row['toDate'];?></td>
									<td class = "fromTime"><?php echo $row['fromTime'];?></td>
									<td class = "toTime"><?php echo $row['toTime'];?></td>
									<td class = ""><button type="button" class="btn btn-default newsEdit">Edit</button> <button type="button" class="btn btn-default newsDelete" data-toggle="modal" data-target="#newsModal">Delete</button></td>
								</tr>
								
							<?php
							}?>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		<?php
	}else{
		echo 'news table empty';
	}
?>
		</div>
		
		<!-- routeModal -->
		  <div class="modal fade" id="routeModal" role="dialog">
			<div class="modal-dialog">
			
			  <!-- Modal content-->
			  <div class="modal-content">
				<div class="modal-header">
				  <button type="button" class="close" data-dismiss="modal">&times;</button>
				  <h4 class="modal-title">Delete Route</h4>
				</div>
				<div class="modal-body">
				  <p id = "routeModalMessage">Some text in the modal.</p>
				</div>
				<div class="modal-footer">
				  <button type="button" class="btn btn-info" id = "deleteRoute" >Delete</button>
				  <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
				</div>
			  </div>
			  
			</div>
		  </div>
		  
		  <!-- busModal -->
		  <div class="modal fade" id="busModal" role="dialog">
			<div class="modal-dialog">
			
			  <!-- Modal content-->
			  <div class="modal-content">
				<div class="modal-header">
				  <button type="button" class="close" data-dismiss="modal">&times;</button>
				  <h4 class="modal-title">Delete Bus</h4>
				</div>
				<div class="modal-body">
				  <p id = "busModalMessage">Some text in the modal.</p>
				</div>
				<div class="modal-footer">
				  <button type="button" class="btn btn-info" id = "deleteBus" >Delete</button>
				  <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
				</div>
			  </div>
			  
			</div>
		  </div>
		
		<!-- userModal -->
		  <div class="modal fade" id="userModal" role="dialog">
			<div class="modal-dialog">
			
			  <!-- Modal content-->
			  <div class="modal-content">
				<div class="modal-header">
				  <button type="button" class="close" data-dismiss="modal">&times;</button>
				  <h4 class="modal-title">Delete User</h4>
				</div>
				<div class="modal-body">
				  <p id = "userModalMessage">Some text in the modal.</p>
				</div>
				<div class="modal-footer">
				  <button type="button" class="btn btn-info" id = "deleteUser" >Delete</button>
				  <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
				</div>
			  </div>
			  
			</div>
		  </div>
		
		<!-- infoModal -->
		  <div class="modal fade" id="infoModal" role="dialog">
			<div class="modal-dialog">
			
			  <!-- Modal content-->
			  <div class="modal-content">
				<div class="modal-header">
				  <button type="button" class="close" data-dismiss="modal">&times;</button>
				  <h4 class="modal-title">Delete Info</h4>
				</div>
				<div class="modal-body">
				  <p id = "infoModalMessage">Some text in the modal.</p>
				</div>
				<div class="modal-footer">
				  <button type="button" class="btn btn-info" id = "deleteInfo" >Delete</button>
				  <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
				</div>
			  </div>
			  
			</div>
		  </div>
		  
		<!-- newsModal -->
		  <div class="modal fade" id="newsModal" role="dialog">
			<div class="modal-dialog">
			
			  <!-- Modal content-->
			  <div class="modal-content">
				<div class="modal-header">
				  <button type="button" class="close" data-dismiss="modal">&times;</button>
				  <h4 class="modal-title">Delete News</h4>
				</div>
				<div class="modal-body">
				  <p id = "newsModalMessage">Some text in the modal.</p>
				</div>
				<div class="modal-footer">
				  <button type="button" class="btn btn-info" id = "deleteNews" >Delete</button>
				  <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
				</div>
			  </div>
			  
			</div>
		  </div>  
		  
		  <!-- scheduleModal -->
		  <div class="modal fade" id="scheduleModal" role="dialog">
			<div class="modal-dialog">
			
			  <!-- Modal content-->
			  <div class="modal-content">
				<div class="modal-header">
				  <button type="button" class="close" data-dismiss="modal">&times;</button>
				  <h4 class="modal-title">Delete Schedule</h4>
				</div>
				<div class="modal-body">
				  <p id = "scheduleModalMessage">Some text in the modal.</p>
				</div>
				<div class="modal-footer">
				  <button type="button" class="btn btn-info" id = "deleteSchedule" >Delete</button>
				  <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
				</div>
			  </div>
			  
			</div>
		  </div>
		  
	</body>
</html>
<script>
	window.onload = function() {
		if(!window.location.hash) {
			window.location = window.location + '#loaded';
			window.location.reload();
		}
	}

	var infoId = '';
	var routeNo = '';
	var routeName = '';
	var bus = '';
	var waypoint = '';
	var stopNames = '';
	
	var newsId = '';
	var newsTitle = '';
	var newsDesc = '';
	var newsContent = '';
	var date = '';
	var cancelledRoute = '';
	var cancelledBus = '';
	var fromDate = '';
	var toDate = '';
	var fromTime = '';
	var toTime = '';
	
	var scheduleId = '';
	var scheduleRoute = '';
	var scheduleBus = '';
	var scheduleTopNote = '';
	var scheduleBottomNote = '';
	var scheduleTimetable = '';
	var scheduleDate = '';
	
	var userId = '';
	var userUsername = '';
	var userPassword = '';
	var userPrivilege = '';
	var userDefaultRoute = '';
	var userDefaultBus = '';
	
	var routeRouteId = '';
	var routeRouteNo = '';
	var routeRouteName = '';
	
	var busBusId = '';
	var busBusNo = '';
	var busBusNoPlate = '';
	
	var sessionLink;
	
	$('.routeEdit').click(function(){
		routeRouteId = $(this).closest('tr').children('td.routeRouteId').text();
		routeRouteNo = $(this).closest('tr').children('td.routeRouteNo').text();
		routeRouteName = $(this).closest('tr').children('td.routeRouteName').text();
		sessionLink = 'route';
		$.post("setSession.php", {routeRouteId:routeRouteId, routeRouteNo:routeRouteNo, routeRouteName:routeRouteName, sessionLink:sessionLink});
		window.location.href = "routeForm.php";
	});
	
	$('.busEdit').click(function(){
		busBusId = $(this).closest('tr').children('td.busBusId').text();
		busBusNo = $(this).closest('tr').children('td.busBusNo').text();
		busBusNoPlate = $(this).closest('tr').children('td.busBusNoPlate').text();
		sessionLink = 'bus';
		$.post("setSession.php", {busBusId:busBusId, busBusNo:busBusNo, busBusNoPlate:busBusNoPlate, sessionLink:sessionLink});
		window.location.href = "busForm.php";
	});
	
	$('.userEdit').click(function(){
		userId = $(this).closest('tr').children('td.userId').text();
		userUsername = $(this).closest('tr').children('td.userUsername').text();
		userPassword = $(this).closest('tr').children('td.userPassword').text();
		userPrivilege = $(this).closest('tr').children('td.userPrivilege').text();
		userDefaultRoute = $(this).closest('tr').children('td.userDefaultRoute').text();
		userDefaultBus = $(this).closest('tr').children('td.userDefaultBus').text();
		sessionLink = 'user';
		$.post("setSession.php", {userId:userId, userUsername:userUsername, userPassword:userPassword, userPrivilege:userPrivilege, userDefaultRoute:userDefaultRoute, userDefaultBus:userDefaultBus, sessionLink:sessionLink});
		window.location.href = "userForm.php";
	});

	$('.infoEdit').click(function(){
		infoId = $(this).closest('tr').children('td.infoId').text();
		routeNo = $(this).closest('tr').children('td.infoRouteNo').text();
		routeName = $(this).closest('tr').children('td.infoRouteName').text();
		bus = $(this).closest('tr').children('td.infoBus').text();
		waypoint = $(this).closest('tr').children('td.infoWaypoint').text();
		stopNames = $(this).closest('tr').children('td.infoStopNames').text();
		sessionLink = 'info';
		$.post("setSession.php", {infoId:infoId, routeNo:routeNo, routeName:routeName, bus:bus, waypoint:waypoint, stopNames:stopNames, sessionLink:sessionLink});
		window.location.href = "infoForm.php";
	});
	
	$('.newsEdit').click(function(){
		newsId = $(this).closest('tr').children('td.newsId').text();
		newsTitle = $(this).closest('tr').children('td.newsNewsTitle').text();
		newsDesc = $(this).closest('tr').children('td.newsNewsDesc').text();
		newsContent = $(this).closest('tr').children('td.newsNewsContent').text();
		date = $(this).closest('tr').children('td.newsDate').text();
		cancelledRoute = $(this).closest('tr').children('td.cancelledRoute').text();
		cancelledBus = $(this).closest('tr').children('td.cancelledBus').text();
		fromDate = $(this).closest('tr').children('td.fromDate').text();
		toDate = $(this).closest('tr').children('td.toDate').text();
		fromTime = $(this).closest('tr').children('td.fromTime').text();
		toTime = $(this).closest('tr').children('td.toTime').text();
		sessionLink = 'news';
		$.post("setSession.php", {newsId:newsId, newsTitle:newsTitle, newsDesc:newsDesc, newsContent:newsContent, date:date, cancelledRoute:cancelledRoute, cancelledBus:cancelledBus, fromDate:fromDate, toDate:toDate, fromTime:fromTime, toTime:toTime, sessionLink:sessionLink});
		window.location.href = "newsForm.php";
	});
	
	$('.scheduleEdit').click(function(){
		scheduleId = $(this).closest('tr').children('td.scheduleId').text();
		scheduleRoute = $(this).closest('tr').children('td.scheduleRoute').text();
		scheduleBus = $(this).closest('tr').children('td.scheduleBus').text();
		scheduleTopNote = $(this).closest('tr').children('td.scheduleTopNote').text();
		scheduleBottomNote = $(this).closest('tr').children('td.scheduleBottomNote').text();
		scheduleTimetable = $(this).closest('tr').children('td.scheduleTimetable').text();
		scheduleDate = $(this).closest('tr').children('td.scheduleDate').text();
		sessionLink = 'schedule';
		$.post("setSession.php", {scheduleId:scheduleId, scheduleRoute:scheduleRoute, scheduleBus:scheduleBus, scheduleTopNote:scheduleTopNote, scheduleBottomNote:scheduleBottomNote, scheduleTimetable:scheduleTimetable, scheduleDate:scheduleDate, sessionLink:sessionLink});
		window.location.href = "scheduleForm.php";
	});
	
	$('.routeDelete').click(function(){
		routeRouteId = $(this).closest('tr').children('td.routeRouteId').text();
		routeRouteNo = $(this).closest('tr').children('td.routeRouteNo').text();
		routeRouteName = $(this).closest('tr').children('td.routeRouteName').text();
		$('#routeModalMessage').text("Delete route no. " + routeRouteNo + " (" + routeRouteName + ") ?");
	});
	
	$('.busDelete').click(function(){
		busBusId = $(this).closest('tr').children('td.busBusId').text();
		busBusNo = $(this).closest('tr').children('td.busBusNo').text();
		busBusNoPlate = $(this).closest('tr').children('td.busBusNoPlate').text();
		$('#busModalMessage').text("Delete bus no. " + busBusNo + " (" + busBusNoPlate + ") ?");
	});
	
	$('.userDelete').click(function(){
		userId = $(this).closest('tr').children('td.userId').text();
		userUsername = $(this).closest('tr').children('td.userUsername').text();
		userPassword = $(this).closest('tr').children('td.userPassword').text();
		userPrivilege = $(this).closest('tr').children('td.userPrivilege').text();
		$('#userModalMessage').text("Delete user no. " + userId + " with Username : " + userUsername + ", Password : " + userPassword + ", and Privilege : " + userPrivilege + "?");
	});
	
	$('.infoDelete').click(function(){
		infoId = $(this).closest('tr').children('td.infoId').text();
		routeNo = $(this).closest('tr').children('td.infoRouteNo').text();
		routeName = $(this).closest('tr').children('td.infoRouteName').text();
		bus = $(this).closest('tr').children('td.infoBus').text();
		$('#infoModalMessage').text("Delete route no. " + routeNo + " (" + routeName + ") ?");
	});
	
	
	$('.newsDelete').click(function(){
		newsId = $(this).closest('tr').children('td.newsId').text();
		newsTitle = $(this).closest('tr').children('td.newsNewsTitle').text();
		newsContent = $(this).closest('tr').children('td.newsNewsContent').text();
		date = $(this).closest('tr').children('td.newsDate').text();
		$('#newsModalMessage').text("Delete news no. " + newsId + " titled " + newsTitle + " ?");
	});
	
	$('.scheduleDelete').click(function(){
		scheduleId = $(this).closest('tr').children('td.scheduleId').text();
		scheduleRoute = $(this).closest('tr').children('td.scheduleRoute').text();
		scheduleBus = $(this).closest('tr').children('td.scheduleBus').text();
		scheduleTopNote = $(this).closest('tr').children('td.scheduleTopNote').text();
		scheduleBottomNote = $(this).closest('tr').children('td.scheduleBottomNote').text();
		scheduleTimetable = $(this).closest('tr').children('td.scheduleTimetable').text();
		scheduleDate = $(this).closest('tr').children('td.scheduleDate').text();
		$('#scheduleModalMessage').text("Delete schedule no. " + scheduleId + " for " + scheduleRoute + " ?");
	});
	
	$('#deleteRoute').click(function(){
		$.post("deleteRow.php", {routeRouteId:routeRouteId});
		window.location.href = "index.php";
	});
	
	$('#deleteBus').click(function(){
		$.post("deleteRow.php", {busBusId:busBusId});
		window.location.href = "index.php";
	});
	
	$('#deleteUser').click(function(){
		$.post("deleteRow.php", {userId:userId, userUsername:userUsername, userPassword:userPassword, userPrivilege:userPrivilege});
		window.location.href = "index.php";
	});
	
	$('#deleteInfo').click(function(){
		$.post("deleteRow.php", {infoId:infoId, routeNo:routeNo, routeName:routeName, bus:bus});
		window.location.href = "index.php";
	});
	
	$('#deleteNews').click(function(){
		$.post("deleteRow.php", {newsId:newsId, newsTitle:newsTitle, newsContent:newsContent, date:date});
		window.location.href = "index.php";
	});
	
	$('#deleteSchedule').click(function(){
		$.post("deleteRow.php", {scheduleId:scheduleId, scheduleRoute:scheduleRoute, scheduleBus:scheduleBus, scheduleTopNote:scheduleTopNote, scheduleBottomNote:scheduleBottomNote, scheduleTimetable:scheduleTimetable, scheduleDate:scheduleDate});
		window.location.href = "index.php";
	});
	
	$('#addRoute').click(function(){
		sessionLink = 'route';
		$.post("setSession.php", {sessionLink:sessionLink});
		window.location.href = "routeForm.php";
	});
	
	$('#addBus').click(function(){
		sessionLink = 'bus';
		$.post("setSession.php", {sessionLink:sessionLink});
		window.location.href = "busForm.php";
	});
	
	$('#addUser').click(function(){
		sessionLink = 'user';
		$.post("setSession.php", {sessionLink:sessionLink});
		window.location.href = "userForm.php";
	});
	
	$('#addInfo').click(function(){
		sessionLink = 'info';
		$.post("setSession.php", {sessionLink:sessionLink});
		window.location.href = "infoForm.php";
	});
	
	$('#addNews').click(function(){
		sessionLink = 'news';
		$.post("setSession.php", {sessionLink:sessionLink});
		window.location.href = "newsForm.php";
	});
	
	$('#addSchedule').click(function(){
		sessionLink = 'schedule';
		$.post("setSession.php", {sessionLink:sessionLink});
		window.location.href = "scheduleForm.php";
	});

</script>

<style>

	body { padding-top: 70px; }

</style>
