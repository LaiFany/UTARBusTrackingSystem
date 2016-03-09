
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
				<?php
			}else if($_SESSION['link'] == 'news'){
				?>
					<li id = "infoLi" ><a href="infoForm.php" id = "infoLink">Info Form</a></li>
					<li id = "newsLi" class = "active"><a href="newsForm.php" id = "newsLink">News Form</a></li>
					<li id = "scheduleLi" ><a href="scheduleForm.php" id = "scheduleLink">Schedule Form</a></li>
				<?php
			}else if($_SESSION['link'] == 'schedule'){
				?>
					<li id = "infoLi" ><a href="infoForm.php" id = "infoLink">Info Form</a></li>
					<li id = "newsLi"><a href="newsForm.php" id = "newsLink">News Form</a></li>
					<li id = "scheduleLi" class = "active"><a href="scheduleForm.php" id = "scheduleLink">Schedule Form</a></li>
				<?php
			}else{
			?>
				<li id = "infoLi" ><a href="infoForm.php" id = "infoLink">Info Form</a></li>
				<li id = "newsLi" ><a href="newsForm.php" id = "newsLink">News Form</a></li>
				<li id = "scheduleLi" ><a href="scheduleForm.php" id = "scheduleLink">Schedule Form</a></li>
			<?php
		}
		}else{
			?>
				<li id = "infoLi" ><a href="infoForm.php" id = "infoLink">Info Form</a></li>
				<li id = "newsLi" ><a href="newsForm.php" id = "newsLink">News Form</a></li>
				<li id = "scheduleLi" ><a href="scheduleForm.php" id = "scheduleLink">Schedule Form</a></li>
			<?php
		}
	  ?>
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

	var sessionLink;

	$('#infoLink').click(function(){
		sessionLink = 'info';
		$.post("setSession.php", {routeNo:routeNo, routeName:routeName, bus:bus, waypoint:waypoint, stopNames:stopNames, newsTitle:newsTitle, newsDesc:newsDesc, newsContent:newsContent, date:date, sessionLink:sessionLink});
	});
	
	$('#newsLink').click(function(){
		sessionLink = 'news';
		$.post("setSession.php", {routeNo:routeNo, routeName:routeName, bus:bus, waypoint:waypoint, stopNames:stopNames, newsTitle:newsTitle, newsDesc:newsDesc, newsContent:newsContent, date:date, sessionLink:sessionLink});
	});
	
	$('#scheduleLink').click(function(){
		sessionLink = 'schedule';
		$.post("setSession.php", {routeNo:routeNo, routeName:routeName, bus:bus, waypoint:waypoint, stopNames:stopNames, newsTitle:newsTitle, newsDesc:newsDesc, newsContent:newsContent, date:date, sessionLink:sessionLink});
	});
	
	$('#home').click(function(){
		sessionLink = '';
		$.post("setSession.php", {sessionLink:sessionLink});
	});

</script>