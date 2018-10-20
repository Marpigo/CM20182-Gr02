<?PHP
$hostserver_localhost="restauranteudea.000webhostapp.com";
$hostname_localhost="localhost";
$database_localhost="id7451289_bd_restaurante";
$username_localhost="id7451289_grupo02";
$password_localhost="123456";

$conexion=mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);

	$id=$_POST['id'];
	$name=$_POST['name'];
	$schedule=$_POST['schedule'];
	$type=$_POST['type'];
	$time=$_POST['time'];
	$preci=$_POST['preci'];
	$ingredient=$_POST['ingredient'];
	//$photo=$_POST['photo'];
	$imagen = $_POST["imagen"];

	$path = "imagen/food/$name.jpg";
	$url = "http://$hostserver_localhost/REST/$path";


	file_put_contents($path,base64_decode($imagen));
	$bytesArchivo=file_get_contents($path);

	$sql="INSERT INTO comida VALUES (?,?,?,?,?,?,?,?)";
	$stm=$conexion->prepare($sql);
	$stm->bind_param('issssiss',$id,$name,$schedule,$type,$time,$preci,$ingredient,$url);
		
	if($stm->execute()){
		echo "registraJson";
	}else{
		echo "noRegistraJson";
}
	
	mysqli_close($conexion);
?>

