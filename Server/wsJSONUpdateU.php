<?PHP
$hostserver_localhost="restauranteudea.000webhostapp.com";
$hostname_localhost="localhost";
$database_localhost="id7451289_bd_restaurante";
$username_localhost="id7451289_grupo02";
$password_localhost="123456";

$conexion=mysqli_connect($hostname_localhost,$username_localhost,$password_localhost,$database_localhost);


	$id=$_POST['id'];
	$name=$_POST['name'];
	$email=$_POST['email'];
	$password=$_POST['password'];
	//$photo=$_POST['photo'];
	$imagen = $_POST["imagen"];

	$path = "imagen/user/$name.jpg";
	$url = "http://$hostserver_localhost/REST/$path";

	file_put_contents($path,base64_decode($imagen));

	$sql="UPDATE usuario SET name= ? , email= ?, password=?, photo=? WHERE id=?";
	$stm=$conexion->prepare($sql);
	$stm->bind_param('ssss',$name,$email,$password,$url);
		
	if($stm->execute()){
		echo "updateJson";
	}else{
		echo "noUpdateJson";
	}
	mysqli_close($conexion);
?>

