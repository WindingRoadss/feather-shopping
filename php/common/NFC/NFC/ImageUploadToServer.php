<?
$data = $_POSE["data1"];
$file_path = $data."/";

echo $file_path;

if(is_dir($data)) {
echo "���� ���� O";
}
else {
echo "���� ���� X";
@mkdir($data, 0777);
@chmod($data, 0777);

$file_path = $file_path.basename($_FILES['uploaded_file']['name']);

if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path)) {
echo "file upload success";
} else {
echo "file upload fail";
}

?>