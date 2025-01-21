Drop TABLE IF EXISTS 'user';
DROP TABLE IF EXISTS 'record';

CREATE TABLE `user` (
                        `username` varchar(50) NOT NULL,
                        `password` varchar(45) CHARACTER SET latin1 NOT NULL,
                        `id` int(11) NOT NULL AUTO_INCREMENT,
                        PRIMARY KEY (`id`,`username`),
                        UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `record` (
                          `id` int(11) NOT NULL AUTO_INCREMENT,
                          `black_username` varchar(50) DEFAULT NULL,
                          `white_username` varchar(50) DEFAULT NULL,
                          `content` text NOT NULL,
                          `start_time` datetime NOT NULL,
                          `end_time` datetime NOT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4;