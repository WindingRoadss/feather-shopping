<?php
include "../common.php";  // Works.

$brand = $_REQUEST[brand];

$sql ="SELECT DISTINCT PR_NAME FROM TB_PRODUCTS where PR_BRAND='$brand'";

$result = mysql_query($sql, $connect);
$total_record = mysql_num_rows($result);

if($total_record != 0)
{
	echo "{\"status\":\"OK\",\"num_results\":\"$total_record\",\"message\":\"완료\",\"results\":[";
	
	for($i=0; $i< $total_record; $i++) 
	{
		mysql_data_seek($result, $i);       
		$row = mysql_fetch_array($result);
		echo "{\"name\":\"$row[PR_NAME]\"}";
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