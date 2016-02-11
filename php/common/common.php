<?php

include "dbcon.php";  // Works.

//echo("common");

$connect = mysql_connect($db_host, $db_user, $db_pass) or die("DB 접속시 에러가 발생했습니다.");
mysql_query("set session character_set_connection=utf8;");
mysql_query("set session character_set_results=utf8;");
mysql_query("set session character_set_client=utf8;");
mysql_select_db($db_name, $connect) or die("DB Select 에러가 발생했습니다");

?>	