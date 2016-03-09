
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>

<nav class="navbar navbar-default navbar-fixed-top">
  <div class="container-fluid">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="home.php" id = "home">Home</a>
    </div>
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
	  <?php
		if(isset($_SESSION['link'])){
			if($_SESSION['link'] == 'info'){
				?>
					<li id = "infoLi" class = "active"><a href="infoForm.php" id = "infoLink">Info Form</a></li>
					<li id = "newsLi" ><a href="newsForm.php" id = "newsLink">News Form</a></li>
					<li id = "scheduleLi" ><a href="scheduleForm.php" id = "scheduleLink">Schedule Form</a></li>
					<li id = "userLi" ><a href="userForm.php" id = "userLink">User Form</a></li>
				<?php
			}else if($_SESSION['link'] == 'news'){
				?>
					<li id = "infoLi" ><a href="infoForm.php" id = "infoLink">Info Form</a></li>
					<li id = "newsLi" class = "active"><a href="newsForm.php" id = "newsLink">News Form</a></li>
					<li id = "scheduleLi" ><a href="scheduleForm.php" id = "scheduleLink">Schedule Form</a></li>
					<li id = "userLi" ><a href="userForm.php" id = "userLink">User Form</a></li>
				<?php
			}else if($_SESSION['link'] == 'schedule'){
				?>
					<li id = "infoLi" ><a href="infoForm.php" id = "infoLink">Info Form</a></li>
					<li id = "newsLi"><a href="newsForm.php" id = "newsLink">News Form</a></li>
					<li id = "scheduleLi" class = "active"><a href="scheduleForm.php" id = "scheduleLink">Schedule Form</a></li>
					<li id = "userLi" ><a href="userForm.php" id = "userLink">User Form</a></li>
				<?php
			}else if($_SESSION['link'] == 'user'){
				?>
					<li id = "infoLi" ><a href="infoForm.php" id = "infoLink">Info Form</a></li>
					<li id = "newsLi"><a href="newsForm.php" id = "newsLink">News Form</a></li>
					<li id = "scheduleLi" ><a href="scheduleForm.php" id = "scheduleLink">Schedule Form</a></li>
					<li id = "userLi" class = "active"><a href="userForm.php" id = "userLink">User Form</a></li>
				<?php
			}else{
			?>
				<li id = "infoLi" ><a href="infoForm.php" id = "infoLink">Info Form</a></li>
				<li id = "newsLi" ><a href="newsForm.php" id = "newsLink">News Form</a></li>
				<li id = "scheduleLi" ><a href="scheduleForm.php" id = "scheduleLink">Schedule Form</a></li>
				<li id = "userLi" ><a href="userForm.php" id = "userLink">User Form</a></li>
			<?php
		}
		}else{
			?>
				<li id = "infoLi" ><a href="infoForm.php" id = "infoLink">Info Form</a></li>
				<li id = "newsLi" ><a href="newsForm.php" id = "newsLink">News Form</a></li>
				<li id = "scheduleLi" ><a href="scheduleForm.php" id = "scheduleLink">Schedule Form</a></li>
				<li id = "userLi" ><a href="userForm.php" id = "userLink">User Form</a></li>
			<?php
		}
	  ?>
      </ul>
	  <ul class="nav navbar-nav navbar-right">
		  <li><a id = "logOut" href="login.php"><span class="glyphicon glyphicon-user"></span> Log Out</a></li>
    </ul>
    </div>
  </div>
</nav>

<script>

	var routeNo = '';
	var routeName = '';
	var bus = '';
	var waypoint = '';
	var stopNames = '';

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
	
	var user = '';

	$('#infoLink').click(function(){
		sessionLink = 'info';
		$.post("unsetSession.php", {});
		$.post("setSession.php", {routeNo:routeNo, routeName:routeName, bus:bus, waypoint:waypoint, stopNames:stopNames, newsTitle:newsTitle, newsDesc:newsDesc, newsContent:newsContent, date:date, sessionLink:sessionLink});
	});
	
	$('#newsLink').click(function(){
		sessionLink = 'news';
		$.post("unsetSession.php", {});
		$.post("setSession.php", {routeNo:routeNo, routeName:routeName, bus:bus, waypoint:waypoint, stopNames:stopNames, newsTitle:newsTitle, newsDesc:newsDesc, newsContent:newsContent, date:date, sessionLink:sessionLink});
	});
	
	$('#scheduleLink').click(function(){
		sessionLink = 'schedule';
		$.post("unsetSession.php", {});
		$.post("setSession.php", {scheduleId:scheduleId, scheduleRoute:scheduleRoute, scheduleBus:scheduleBus, scheduleTopNote:scheduleTopNote, scheduleBottomNote:scheduleBottomNote, scheduleTimetable:scheduleTimetable, scheduleDate:scheduleDate, sessionLink:sessionLink});
	});
	
	$('#userLink').click(function(){
		sessionLink = 'user';
		$.post("unsetSession.php", {});
		$.post("setSession.php", {userId:userId, userUsername:userUsername, userPassword:userPassword, userPrivilege:userPrivilege, sessionLink:sessionLink});
	});
	
	$('#home').click(function(){
		sessionLink = '';
		$.post("unsetSession.php", {});
		$.post("setSession.php", {sessionLink:sessionLink});
	});
	
	$('#logOut').click(function(){
		$.post("unsetSession.php", {user:user});
	});

</script>