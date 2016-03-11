<?php
	//session
	session_start();
	
	$_SESSION['link'] = 'user';
	
	$userId = '';
	$userUsername = '';
	$userPassword = '';
	$userPrivilege = '';
	$userDefaultRoute = '';
	$userDefaultBus = '';
	
	 if(isset($_SESSION['userId'])){
		 $userId = $_SESSION['userId'];
	 }
	 if(isset($_SESSION['userUsername'])){
		 $userUsername = $_SESSION['userUsername'];
	 }
	 if(isset($_SESSION['userPassword'])){
		 $userPassword = $_SESSION['userPassword'];
	 }
	 if(isset($_SESSION['userPrivilege'])){
		 $userPrivilege = $_SESSION['userPrivilege'];
	 }
	 if(isset($_SESSION['userDefaultRoute'])){
		 $userDefaultRoute = $_SESSION['userDefaultRoute'];
	 }
	 if(isset($_SESSION['userDefaultBus'])){
		 $userDefaultBus = $_SESSION['userDefaultBus'];
	 }
?>
<?php include 'navBar.php'; ?>

<html>
<head>
	<meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
  <script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.11.1/jquery.validate.js"></script>
</head>
<body>
<div class="container">

	<div class="panel panel-primary">
      <div class="panel-heading">Adding User</div>
      <div class="panel-body">
		<form id = "userForm" action="postWebOperation.php" method="post"
		enctype="multipart/form-data">
		<div class = "row form-group has-feedback">
			<label for="file" class="col-sm-2">ID No.</label>
			<div class="col-sm-4 controls">
				<input type="text" name="userId" id="userId" class="form-control" value = "<?php echo $userId; ?>" readonly>
			</div>
		</div>
		<div class = "row form-group has-feedback">
			<label for="file" class="col-sm-2">Username</label>
			<div class="col-sm-4 controls">
				<input type="text" name="username" id="username" class="form-control" value = "<?php echo $userUsername; ?>">
			</div>
		</div>
		<div class = "row form-group has-feedback">
			<label for="file" class="col-sm-2">Password</label>
			<div class="col-sm-4 controls">
				<input type="text" name="password" id="password" class="form-control" value = "<?php echo $userPassword; ?>">
			</div>
		</div>
		<div class = "row form-group has-feedback">
			<label for="file" class="col-sm-2">Privilege</label>
			<div class="col-sm-4 controls">
				<label for="route">Select Privilege (select one):</label>
				<select class="form-control" name = "privilege" id="privilege" required>
				<?php
					if($userPrivilege != ''){
						?>			
							<option value = "admin" <?php if($userPrivilege == 'admin') echo 'selected';?>>System Administrator</option>
							<option value = "busdriver" <?php if($userPrivilege == 'busdriver') echo 'selected';?>>Bus Driver</option>
						<?php
					}else{
						?>
							<option value = "admin">System Administrator</option>
							<option value = "busdriver">Bus Driver</option>
						<?php
					}
				?>
			</div>
		</div>
		
		<div class = "row form-group has-feedback" style = "display:none;">
			<label for="file" class="col-sm-2">Dummy</label>
			<div class="col-sm-4 controls">
				<label for="route">Dummy</label>
				<select class="form-control" name = "Dummy" id="Dummy">
					<option value = ""></option>
				</select>
			</div>
		</div>
		
		<div class = "row form-group has-feedback">
			<label for="file" class="col-sm-2">Default Route</label>
			<div class="col-sm-4 controls">
				<label for="route">Select Default Route (select one):</label>
				<select class="form-control" name = "defaultRoute" id="defaultRoute" required>
					<option value = "none" <?php if($userDefaultRoute == "none") echo 'selected';?>>None</option>
			<?php
				$con=mysqli_connect('localhost', 'root', '', 'bustrackerdb');
				mysqli_select_db($con, "bustrackerdb");
				
				//get updated data in info table
				$routeResult = mysqli_query($con, "SELECT routeNo, routeName FROM info");
				$busResult = mysqli_query($con, "SELECT bus FROM info");
				
				if(!empty($routeResult)){
					while($row = mysqli_fetch_array($routeResult)){
				?>
						<option value = "<?php echo 'Route '.$row['routeNo'].' : '.$row['routeName'];?>" <?php if($userDefaultRoute == ('Route '.$row['routeNo'].' : '.$row['routeName'])) echo 'selected';?>><?php echo 'Route '.$row['routeNo'].' : '.$row['routeName'];?></option>
				<?php
					}
				}
				?>
				</select>
			</div>
		</div>
				
		<div class = "row form-group has-feedback">
			<label for="file" class="col-sm-2">Default Bus</label>
			<div class="col-sm-4 controls">
				<label for="route">Select Default Bus (select one):</label>
				<select class="form-control" name = "defaultBus" id="defaultBus" required>
					<option value = "none" <?php if($userDefaultBus == "none") echo 'selected';?>>None</option>
				<?php
				if(!empty($busResult)){
					while($row2 = mysqli_fetch_array($busResult)){
				?>
						<option value = "<?php echo 'Bus '.$row2['bus'];?>" <?php if($userDefaultBus == ('Bus '.$row2['bus'])) echo 'selected';?>><?php echo 'Bus '.$row2['bus'];?></option>
				<?php
					}
				?>
				</select>
				<?php
				}
				?>
			</div>
		</div>
		<input type="submit" name="submit" value="Submit" class="btn btn-default pull-right">
		<?php
			if(isset($_SESSION['usernameDuplicate'])){
				?>
					<div class = "col-sm-4"><p style="color : red;">Username already exist</p></div>
				<?php
			}
		?>
		</form>
	  </div>
    </div>
</div>
</body>
</html>

<script>

	$('form').validate({
        rules: {
            username: {
                required: true
            },
            password: {
                required: true
            }
        },
        highlight: function(element) {
            $(element).closest('.form-group').addClass('has-error');
        },
        unhighlight: function(element) {
            $(element).closest('.form-group').removeClass('has-error');
        },
        errorElement: 'span',
        errorClass: 'help-block',
        errorPlacement: function(error, element) {
            error.insertAfter(element);
        }
    });
	
	/*$(function(){
		$("#newsContent").keypress(function (e) {
			if (e.keyCode == 13) {
				$('#newsContent').val($('#newsContent').val() + '&#013;&#010;');
			}
		});
	});*/

</script>

<style>

	body { padding-top: 70px; }

</style>