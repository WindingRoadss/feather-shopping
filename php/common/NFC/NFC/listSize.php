<?php
include "../common.php";  // Works.


$brand = $_REQUEST[brand]; 
$snum = $_REQUEST[serial];

$sql ="SELECT DISTINCT PR_SIZE FROM TB_PRODUCTS where PR_BRAND='$brand' and PR_SNUM='$snum'";

$result = mysql_query($sql, $connect);

$total_record = mysql_num_rows($result);
if($total_record != 0)
{
	echo "{\"status\":\"OK\",\"num_results\":\"$total_record\",\"message\":\"완료\",\"results\":[";
	
	for($i=0; $i< $total_record; $i++) 
	{
		  mysql_data_seek($result, $i);       
		  $row = mysql_fetch_array($result);
		  
		  echo "{\"size\":\"$row[PR_SIZE]\"}";
		  if($i<$total_record-1){
			echo ",";
		}
	}

	
	echo "]}";
}
else
{
		
	echo "{\"status\":\"NO\",\"num_results\":\"0\",\"message\":\"상품 없음\"}";
}

?>