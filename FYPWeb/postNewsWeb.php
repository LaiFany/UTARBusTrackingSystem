<?php

	$con=mysqli_connect('localhost', 'root', '', 'bustrackerdb');
	mysqli_select_db($con, "bustrackerdb");
	
	if(isset($_POST["newsId"]) && isset($_POST["newsTitle"]) && isset($_POST["newsContent"])){
		$newsId = trim($_POST['newsId']);
		$newsTitle = trim($_POST['newsTitle']);
		$newsContent = trim($_POST['newsContent']);
		$newsDesc = trim($_POST['newsDesc']);
		
		//get current date
		// Return date/time info of a timestamp; then format the output
		$mydate=getdate(date("U"));
		$date = $mydate[month]." ".$mydate[mday].", ". $mydate[year];
		
		$result = mysqli_query($con, "SELECT * FROM news WHERE id='".$newsId."'");
		if(mysqli_fetch_array($result) != false){
			mysqli_query($con, "UPDATE news SET newsTitle='".$newsTitle."', newsDesc='".$newsDesc."', newsContent='".$newsContent."', date='".$date."' WHERE id='".$newsId."'");
		}else{
			mysqli_query($con, "insert into news(newsTitle, newsDesc, newsContent, date) values('{$newsTitle}', '{$newsDesc}', '{$newsContent}', '{$date}')");
		}
		
		header("Location:home.php");
	}
	else{
		echo "Missing required fields";
	}

?>