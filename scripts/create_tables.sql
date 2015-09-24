CREATE TABLE `activity` (
  `id` bigint(20) NOT NULL,
  `activity` varchar(255) DEFAULT NULL,
  `alltext` longtext,
  `comment` longtext,
  `date` datetime DEFAULT NULL,
  `dist_hour` bigint(20) DEFAULT NULL,
  `dist_min` bigint(20) DEFAULT NULL,
  `dist_sec` bigint(20) DEFAULT NULL,
  `distance` double DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  FULLTEXT KEY `alltext` (`alltext`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;