<?php
include "../common.php";  

$tag = $_REQUEST[tag]; 

$sql ="SELECT NFC_USED FROM TB_NFC where NFC_TAGID = '$tag'";

$result = mysql_query($sql, $connect);
$total_record = mysql_num_rows($result);

if($total_record != 0)
{
	$row = mysql_fetch_array($result);
	echo "{\"status\":\"OK\",\"num_results\":\"$total_record\",\"message\":\"tag 사용 중\",\"results\":[";
	echo "{\"used\":\"$row[NFC_USED]\"}]}";
	//$sql 결과값으로 바로 입력하는 것 찾아보기
}
else
{		
	echo "{\"status\":\"NO\",\"num_results\":\"0\",\"message\":\"존재하지 않는 Tag ID\"}";
}

?>