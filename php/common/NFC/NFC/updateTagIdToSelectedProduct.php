<?php
include "../common.php";  // Works.


$tag = $_REQUEST[tag];

$brand = $_REQUEST[brand]; 
$snum = $_REQUEST[serial];
$size = $_REQUEST[size]; 
$color = $_REQUEST[color]; 
$now_key = $snum.$color.$size;


/*
$tag = 'cc146631000104e0';
$now_key = 'CC1234YELLOWF';
 * 
 *  if($_SERVER['REQUEST_METHOD']=='POST'){
 
 $image = $_POST['image'];
 
 
 $sql = "INSERT INTO images (image) VALUES (?)";
 
 $stmt = mysqli_prepare($con,$sql);
 
 mysqli_stmt_bind_param($stmt,"s",$image);
 mysqli_stmt_execute($stmt);
 
 $check = mysqli_stmt_affected_rows($stmt);
 
 if($check == 1){
 echo "Image Uploaded Successfully";
 }else{
 echo "Error Uploading Image";
 }
 mysqli_close($con);
 }else{
 echo "Error";
 }
 * 
 * 
 * , PR_IMAGE='$image'
*/

//

//이전에 저장되어 있던 tag값의 TB_NFC=>NFC_USED 값을 0으로 바꿔줌
$sql ="UPDATE TB_PRODUCTS INNER JOIN TB_NFC ON TB_PRODUCTS.PR_TAGID = TB_NFC.NFC_TAGID
SET TB_NFC.NFC_USED=0 where TB_PRODUCTS.PR_KEY='$now_key'";
mysql_query($sql, $connect);

//현재 선택된 상품 row에 tagid를 삽입
$sql ="UPDATE TB_PRODUCTS SET PR_TAGID='$tag' where PR_KEY='$now_key'";
mysql_query($sql, $connect);

//NFC 테이블에서 USED 값 변경
$sql ="UPDATE TB_NFC SET NFC_USED='1' where NFC_TAGID='$tag'";
mysql_query($sql, $connect);

echo "{\"status\":\"OK\",\"num_results\":\"0\",\"message\":\"업데이트 완료\"}";

?>