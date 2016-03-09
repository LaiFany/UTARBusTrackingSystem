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
	if (session_status() == PHP_SESSION_NONE) {
		session_start();
	}
	if(!isset($_SESSION['user'])){
		header('Location:login.php');
	}
	
	//navbar
	include 'navBar.php';

	$con=mysqli_connect('localhost', 'root', '', 'bustrackerdb');
	mysqli_select_db($con, "bustrackerdb");
	
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
									<th>Waypoint Coordinates</th>
									<th>Stop Names</th>
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
									<td class = "infoWaypoint"><?php echo $row['waypoint'];?></td>
									<td class = "infoStopNames"><?php echo $row['stopNames'];?></td>
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
	
	if(!empty($newsResult)){
		
		//create table and populate them with data from info table in server
		?>
			<div class="panel panel-info">
				<div class="panel-heading" style="padding : 20px;">News Table <button type="button" class="btn btn-info pull-right" id = "addNews" >Add</button></div>
				<div class="panel-body">
					<div class = "table-responsive">
						<table class = "table table-hover">
							<thead>
								<tr>
									<th>ID</th>
									<th>News Title</th>
									<th>News Description</th>
									<th>News Content</th>
									<th>Date</th>
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
									<td class = "scheduleTimetable"><?php echo $row['timetable'];?></td>
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
?>
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
				  <h4 class="modal-title">Delete Route</h4>
				</div>
				<div class="modal-body">
				  <p id = "infoModalMessage">Some text in the modal.</p>
				</div>
				<div class="modal-footer">
				  <button type="button" class="btn btn-info" id = "deleteRoute" >Delete</button>
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
	
	var sessionLink;
	
	$('.userEdit').click(function(){
		userId = $(this).closest('tr').children('td.userId').text();
		userUsername = $(this).closest('tr').children('td.userUsername').text();
		userPassword = $(this).closest('tr').children('td.userPassword').text();
		userPrivilege = $(this).closest('tr').children('td.userPrivilege').text();
		sessionLink = 'user';
		$.post("setSession.php", {userId:userId, userUsername:userUsername, userPassword:userPassword, userPrivilege:userPrivilege, sessionLink:sessionLink});
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
		sessionLink = 'news';
		$.post("setSession.php", {newsId:newsId, newsTitle:newsTitle, newsDesc:newsDesc, newsContent:newsContent, date:date, sessionLink:sessionLink});
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
	
	$('#deleteUser').click(function(){
		$.post("deleteRow.php", {userId:userId, userUsername:userUsername, userPassword:userPassword, userPrivilege:userPrivilege});
		window.location.href = "home.php";
	});
	
	$('#deleteRoute').click(function(){
		$.post("deleteRow.php", {infoId:infoId, routeNo:routeNo, routeName:routeName, bus:bus});
		window.location.href = "home.php";
	});
	
	$('#deleteNews').click(function(){
		$.post("deleteRow.php", {newsId:newsId, newsTitle:newsTitle, newsContent:newsContent, date:date});
		window.location.href = "home.php";
	});
	
	$('#deleteSchedule').click(function(){
		$.post("deleteRow.php", {scheduleId:scheduleId, scheduleRoute:scheduleRoute, scheduleBus:scheduleBus, scheduleTopNote:scheduleTopNote, scheduleBottomNote:scheduleBottomNote, scheduleTimetable:scheduleTimetable, scheduleDate:scheduleDate});
		window.location.href = "home.php";
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
