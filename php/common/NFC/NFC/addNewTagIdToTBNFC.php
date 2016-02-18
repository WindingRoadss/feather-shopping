<?php
include "../common.php";  

/* NFC가 새 제품인 경우 해당 tagId를 TB_NFC 테이블에 삽입한다. */

$tag = $_REQUEST[tag]; 


$sql ="INSERT INTO TB_NFC(NFC_TAGID, NFC_USED) VALUES('$tag', '0')";
//		ON DUPLICATE KEY UPDATE NFC_USED='1'
mysql_query($sql, $connect);
echo "{\"status\":\"OK\",\"num_results\":\"0\",\"message\":\"업데이트 완료\"}";

?>