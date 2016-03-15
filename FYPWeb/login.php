<html>
	<head>
		<title>Login</title>
		  <meta charset="utf-8">
		  <meta name="viewport" content="width=device-width, initial-scale=1">
		  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
		  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
		  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
	</head>
	<?php
		//session
		if(!isset($_SESSION)){ 
			session_start(); 
		} 
	?>
	<body>
		<div class = "container">
			<div class="panel panel-primary">
			  <div class="panel-heading">Login</div>
			  <div class="panel-body">
				<form id = "newsForm" action="loginRequest.php" method="post"
				enctype="multipart/form-data">
				<div class = "row form-group has-feedback">
					<label for="file" class="col-sm-2">Username</label>
					<div class="col-sm-4 controls">
						<input type="text" name="username" id="username" class="form-control" required/>
					</div>
				</div>
				<div class = "row form-group has-feedback">
					<label for="file" class="col-sm-2">Password</label>
					<div class="col-sm-4 controls">
						<input type="password" name="password" id="password" class="form-control" required/>
					</div>
				</div>
				<input type="submit" name="submit" value="Login" class="btn btn-default pull-right">
				</form>
				<?php 
					if(isset($_SESSION['loginNotFound']) && $_SESSION['loginNotFound'] == 'true'){
						?>
							<div class = "col-sm-4"><p style = "color : red;">Invalid Username or Password</p></div>
						<?php
					}
				?>
			  </div>
			</div>
		</div>
	</body>
</html>

<?php
	include 'unsetSession.php';
?>