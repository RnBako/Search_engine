CREATE TABLE `field` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `selector` varchar(255) NOT NULL,
  `weight` float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `site` (
  `id` int NOT NULL AUTO_INCREMENT,
  `last_error` text,
  `name` varchar(255) NOT NULL,
  `status` enum('INDEXING','INDEXED','FAILED') NOT NULL,
  `status_time` datetime NOT NULL,
  `url` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `lemma` (
  `id` int NOT NULL AUTO_INCREMENT,
  `frequency` int NOT NULL,
  `lemma` varchar(255) NOT NULL,
  `site_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5hcty8fjeaxphmttfkqtoxeyw` (`site_id`),
  CONSTRAINT `FK5hcty8fjeaxphmttfkqtoxeyw` FOREIGN KEY (`site_id`) REFERENCES `site` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=296015 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `page` (
  `id` int NOT NULL AUTO_INCREMENT,
  `code` int NOT NULL,
  `content` mediumtext NOT NULL,
  `path` text NOT NULL,
  `site_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKm6i94ts2a5h1symo57t4yjdra` (`site_id`),
  CONSTRAINT `FKm6i94ts2a5h1symo57t4yjdra` FOREIGN KEY (`site_id`) REFERENCES `site` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10211 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `index` (
  `id` int NOT NULL AUTO_INCREMENT,
  `rank` float NOT NULL,
  `lemma_id` int NOT NULL,
  `page_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKiqgm34dkvjdt7kobg71xlbr33` (`lemma_id`),
  KEY `FKsq3363uoow6fmurlfheackwgc` (`page_id`),
  CONSTRAINT `FKiqgm34dkvjdt7kobg71xlbr33` FOREIGN KEY (`lemma_id`) REFERENCES `lemma` (`id`),
  CONSTRAINT `FKsq3363uoow6fmurlfheackwgc` FOREIGN KEY (`page_id`) REFERENCES `page` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=322216 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `field` (`name`, `selector`, `weight`) VALUES ('title', 'title', '1');

INSERT INTO `field` (`name`, `selector`, `weight`) VALUES ('body', 'body', '0.8');