<?php
include "common.php";  // Works.

$tag = $_REQUEST[tag];
$key = $_REQUEST[key]; //tagID에 저장 되어있던 상품의 key 값. 

$brand = $_REQUEST[brand]; 
$name = $_REQUEST[name]; 
$size = $_REQUEST[size]; 
$color = $_REQUEST[color]; 

//key 값으로 찾아서 상품정보 삭제
$sql ="UPDATE TB_PRODUCTS SET PR_TAGID='null' where PR_KEY='$key'";
mysql_query($sql, $connect);

/*
//tag 값으로 찾아서 상품 정보 삭제
$sql ="UPDATE TB_PRODUCTS SET PR_TAGID='null' where PR_TAGID='$tag'";
mysql_query($sql, $connect);
*/

$sql ="UPDATE TB_PRODUCTS SET PR_TAGID='$tag' where PR_BRAND='$brand' and PR_SIZE='$size' and PR_COLOR = '$color'";
mysql_query($sql, $connect);


/*
//NFC 테이블에서 USED 값 변경
$sql ="UPDATE TB_NFC SET NFC_USED='1' where NFC_TAGID='$tag'";
mysql_query($sql, $connect);
*/


?>