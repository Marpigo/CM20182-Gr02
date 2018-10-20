-- phpMyAdmin SQL Dump
-- version 4.7.7
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 19-10-2018 a las 15:26:53
-- Versión del servidor: 10.1.31-MariaDB
-- Versión de PHP: 7.0.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `id7451289_bd_restaurante`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `bebida`
--

CREATE TABLE `bebida` (
  `id` int(11) NOT NULL,
  `name` text,
  `preci` int(11) DEFAULT NULL,
  `ingredient` text,
  `photo` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `bebida`
--

INSERT INTO `bebida` (`id`, `name`, `preci`, `ingredient`, `photo`) VALUES
(2, 'JUGO NARANJA FRESCA', 5000, 'NARANJA + LIMON + LICO', 'https://restauranteudea.000webhostapp.com/REST/imagen/drink/drink1.png'),
(3, 'JUGO GUAYABA', 3500, 'GUYABA + BRETAÑA', 'https://restauranteudea.000webhostapp.com/REST/imagen/drink/drink2.png'),
(4, 'MICHELADO CON TOMATE', 4500, 'TOMATE + CERVEZA + LIMON', 'https://restauranteudea.000webhostapp.com/REST/imagen/drink/drink3.jpg'),
(5, 'LIMONADA', 3000, 'LIMON + AZUCAR', 'https://restauranteudea.000webhostapp.com/REST/imagen/drink/drink4.jpg'),
(6, 'PIÑA', 6000, 'TORONJA + PIÑA + LICOR', 'https://restauranteudea.000webhostapp.com/REST/imagen/drink/drink5.png');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `comida`
--

CREATE TABLE `comida` (
  `id` int(11) NOT NULL,
  `name` text,
  `schedule` text,
  `type` text,
  `time` text,
  `preci` int(11) DEFAULT NULL,
  `ingredient` text,
  `photo` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `comida`
--

INSERT INTO `comida` (`id`, `name`, `schedule`, `type`, `time`, `preci`, `ingredient`, `photo`) VALUES
(1, 'LOMO CARACHO', 'NOCHE', 'PLATO PRINCIPAL', '6:30PM', 25000, 'LOMO + PAPA + AREPA', 'https://restauranteudea.000webhostapp.com/REST/imagen/food/asado carbon.png'),
(2, 'PLATO KEBAD', 'MAÑANA', 'ENTRADA', '10 AM', 15000, 'CARNE + ENSALADA + PAPA', 'https://restauranteudea.000webhostapp.com/REST/imagen/food/asa carbon.jpg'),
(3, 'VALUD PARRILLA', 'TARDE', 'ENTRADA', '2:00', 16000, 'CARNE + CARIMAÑOLA', 'https://restauranteudea.000webhostapp.com/REST/imagen/food/asado carbon.png'),
(4, 'ASADA AL CARBON', 'NOCHE', 'PLATO PRINCIPAL', '8:PM', 25000, 'AREPA DE MAIZ + AGUACATE + CARNE', 'https://restauranteudea.000webhostapp.com/REST/imagen/food/lomo cachorro.png'),
(5, 'AMOR MIO', 'NOCHE', 'PLATO PRINCIPAL', '7PM', 23000, 'SOLOMO 3/4 + AMARIDO + AGUACATE', 'https://restauranteudea.000webhostapp.com/REST/imagen/food/plato kebad.png'),
(6, 'CARNE ASADA VERDURIANA', 'FALSE,TRUE,FALSE', 'TRUE', '18:22', 35000, 'CARNE, VERDURAS', 'https://restauranteudea.000webhostapp.com/REST/imagen/food/plato verdura.jpg');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id` int(11) NOT NULL,
  `name` text,
  `email` text,
  `password` text,
  `photo` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id`, `name`, `email`, `password`, `photo`) VALUES
(1, 'marcell', 'marpigov@gmail.com', '123', 'https://restauranteudea.000webhostapp.com/REST/imagen/user/user.png'),
(2, 'gonzalo', 'chalo1970@gmail.com', '123', 'https://restauranteudea.000webhostapp.com/REST/imagen/user/user.png'),
(3, 'edwar', 'edwar.martinez@udea.edu.co', '123', 'https://restauranteudea.000webhostapp.com/REST/imagen/user/user.png'),
(4, 'HUGO', 'humar31@hotmail.com', '124', 'http://restauranteudea.000webhostapp.com/REST/imagen/user/HUGO.jpg'),
(5, 'KELLY', 'kell@udea.edu.co', '123', 'http://restauranteudea.000webhostapp.com/REST/imagen/user/KELLY.jpg');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `bebida`
--
ALTER TABLE `bebida`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`);

--
-- Indices de la tabla `comida`
--
ALTER TABLE `comida`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `bebida`
--
ALTER TABLE `bebida`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT de la tabla `comida`
--
ALTER TABLE `comida`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
