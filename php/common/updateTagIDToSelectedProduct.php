<?php
include "common.php";  // Works.

$tag = $_REQUEST[tag];

$brand = $_REQUEST[brand]; 
$name = $_REQUEST[name]; 
$size = $_REQUEST[size]; 
$color = $_REQUEST[color]; 

$sql ="UPDATE TB_PRODUCTS SET PR_TAGID='$tag' where PR_BRAND='$brand' and PR_NAME='$name' 
		and PR_SIZE='$size' and PR_COLOR = '$color'";
mysql_query($sql, $connect);

//NFC 테이블에서 USED 값 변경
$sql ="UPDATE TB_NFC SET NFC_USED='1' where NFC_TAGID='$tag'";
mysql_query($sql, $connect);

?>