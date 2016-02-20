<?php
include "../common.php";  // Works.

$sql ="SELECT * FROM TB_BRAND";
$result = mysql_query($sql, $connect);

$total_record = mysql_num_rows($result);
if($total_record != 0)
{
	echo "{\"status\":\"OK\",\"num_results\":\"$total_record\",\"message\":\"완료\",\"results\":[";
	
	for($i=0; $i< $total_record; $i++) 
	{
		  mysql_data_seek($result, $i);       
		  $row = mysql_fetch_array($result);
		  
		  echo "{\"brand\":\"$row[BR_NAME]\"}";
		  if($i<$total_record-1){
			echo ",";
		  }
	}
	echo "]}";
}
else
{
	echo "{\"status\":\"OK\",\"num_results\":\"0\",\"message\":\"완료\"}";
}

?>