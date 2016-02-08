<?php

$hostname = "mysql.czrx0p0th7zk.us-west-2.rds.amazonaws.com:3306";
$username = "dbmaster";
$password = "Rltxjf11";
$dbname = "FS_MySQL";

$connect = mysql_connect($hostname, $username, $password) or die("MySQL Server 연결에 실패했습니다");
mysql_select_db($dbname, $connect) or die("DB open error");

$sql = "SELECT * FROM test";
$result = mysql_query($sql);

while($row=mysql_fetch_assoc($result)){
    echo($row['id']);
    echo("<br />\n");
    echo($row['pwd']);
    echo("<br />\n");
}

mysql_free_result($result);
mysql_close($connect);

?>


