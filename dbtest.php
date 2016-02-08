
<?php

//phpinfo();
//ini_set('display_errors',1);

echo("dbtest<br />\n");

$hostname = "mysql.czrx0p0th7zk.us-west-2.rds.amazonaws.com:3306";
$username = "dbmaster";
$password = "Rltxjf11";
$dbname = "FS_MySQL";

$connect = mysql_connect($hostname, $username, $password) or die("MySQL Server 연결에 실패했습니다");
$result = mysql_select_db($dbname,$connect);

if($result) {
    echo("MySQL Server Connect Success");
}
else {
    echo("MySQL Server Connect Fail");
}

mysql_close($connect);

?>

