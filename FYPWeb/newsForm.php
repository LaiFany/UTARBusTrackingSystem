<?php
	//session
	session_start();
	
	$_SESSION['link'] = 'news';
	
	$newsId = '';
	$newsTitle = '';
	$newsDesc = '';
	$newsContent = '';
	
	 if(isset($_SESSION['newsId'])){
		 $newsId = $_SESSION['newsId'];
	 }
	 if(isset($_SESSION['newsTitle'])){
		 $newsTitle = $_SESSION['newsTitle'];
	 }
	 if(isset($_SESSION['newsDesc'])){
		 $newsDesc = $_SESSION['newsDesc'];
	 }
	 if(isset($_SESSION['newsContent'])){
		 $newsContent = $_SESSION['newsContent'];
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
      <div class="panel-heading">Adding News</div>
      <div class="panel-body">
		<form id = "newsForm" action="postNewsWeb.php" method="post"
		enctype="multipart/form-data">
		<div class = "row form-group has-feedback">
			<label for="file" class="col-sm-2">ID No.</label>
			<div class="col-sm-4 controls">
				<input type="text" name="newsId" id="newsId" class="form-control" value = "<?php echo $newsId; ?>" readonly>
			</div>
		</div>
		<div class = "row form-group has-feedback">
			<label for="file" class="col-sm-2">News Title</label>
			<div class="col-sm-4 controls">
				<input type="text" name="newsTitle" id="newsTitle" class="form-control" value = "<?php echo $newsTitle; ?>">
			</div>
		</div>
		<div class = "row form-group has-feedback">
			<label for="file" class="col-sm-2">News Description</label>
			<div class="col-sm-4 controls">
				<input type="text" name="newsDesc" id="newsDesc" class="form-control" value = "<?php echo $newsDesc; ?>">
			</div>
		</div>
		<div class = "row form-group has-feedback">
			<label for="file" class="col-sm-2">News Content</label>
			<div class="col-sm-4 controls">
				<textarea rows="4" cols="50" name="newsContent" id="newsContent" class="form-control"><?php echo $newsContent; ?></textarea>
			</div>
		</div>
		<input type="submit" name="submit" value="Submit" class="btn btn-default pull-right">
		</form>
	  </div>
    </div>
</div>
</body>
</html>

<script>

	$('form').validate({
        rules: {
            newsTitle: {
                required: true
            },
            newsContent: {
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

<?php
	session_destroy();
?>