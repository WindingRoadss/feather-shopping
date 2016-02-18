<?php
include "../common.php";  // Works.


$brand = $_REQUEST[brand]; 
$snum = $_REQUEST[serial];
$size = $_REQUEST[size]; 
$color = $_REQUEST[color];
$now_key = $snum.$color.$size;


$sql ="SELECT * FROM TB_PRODUCTS where PR_KEY='$now_key' and PR_BRAND='$brand'";

$result = mysql_query($sql, $connect);	
$total_record = mysql_num_rows($result);


//tagID를 가지고 있는 row 가 두개 이상인 경우 -> 에러 처리 필요 ***
if($total_record!=1) {
	echo "{\"status\":\"OK\",\"num_results\":\"0\",\"message\":\"완료\"}";
}
else {
	
	$row = mysql_fetch_array($result);
	
	$price = $row[PR_PRICE];
	$stock = $row[PR_STOCK];
	
	echo "{\"status\":\"OK\",\"num_results\":\"1\",\"message\":\"완료\",\"results\":[";
	echo "{\"price\":\"$price\",\"stock\":\"$stock\"}]}"; 
}


?>