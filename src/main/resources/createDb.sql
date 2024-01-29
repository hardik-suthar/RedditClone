DROP SCHEMA IF EXISTS `reddit-project`;
CREATE SCHEMA `reddit-project`;

USE `reddit-project`;

CREATE TABLE `user`(
	`user_name` varchar(15) NOT NULL,
    `password` varchar(250) NOT NULL,
    `first_name` varchar(20) NOT NULL,
    `last_name` varchar(20) NOT NULL,
    `email` varchar(100) NOT NULL UNIQUE,
    `bio` varchar(200) DEFAULT NULL,
    `created_on` date DEFAULT (CURRENT_DATE),
    PRIMARY KEY (`user_name`)
);

CREATE TABLE `community`(
	`id` bigint AUTO_INCREMENT,
	`name` varchar(50) NOT NULL UNIQUE,
    `created_on` date DEFAULT (CURRENT_DATE),
    `created_by` varchar(15) not null,
	PRIMARY KEY (`id`),
	FOREIGN KEY (`created_by`) REFERENCES user(`user_name`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `post`(
	`id` bigint AUTO_INCREMENT,
    `content` varchar(200),
    `url` varchar(200),
    `user_id` varchar(15) not null,
    `community_id` bigint not null,
    `created_on` date DEFAULT (CURRENT_DATE),
    `total_votes` int DEFAULT 0,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES user(`user_name`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`community_id`) REFERENCES community(`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `comment`(
	`id` bigint AUTO_INCREMENT,
	`content` varchar(200) NOT NULL,
    `user_id` varchar(15) NOT NULL,
    `post_id` bigint NOT NULL,
    `created_on` date DEFAULT (CURRENT_DATE),
    `total_votes` int DEFAULT 0,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES user(`user_name`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`post_id`) REFERENCES post(`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `post_vote`(
	`id` bigint AUTO_INCREMENT,
	`vote_type` boolean not null,
	`post_id` bigint not null,
    `user_id` varchar(15) not null,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`post_id`,`user_id`),
    FOREIGN KEY (`user_id`) REFERENCES user(`user_name`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`post_id`) REFERENCES post(`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `comment_vote`(
	`id` bigint AUTO_INCREMENT,
	`vote_type` boolean not null,
    `user_id` varchar(15) not null,
    `comment_id` bigint not null,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`user_id`,`comment_id`),
    FOREIGN KEY (`user_id`) REFERENCES user(`user_name`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`comment_id`) REFERENCES comment(`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `saved_post` (
	`user_id` varchar(15) not null,
    `post_id` bigint not null,
	PRIMARY KEY (`user_id`,`post_id`),
    FOREIGN KEY (`user_id`) REFERENCES user(`user_name`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`post_id`) REFERENCES post(`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `user_community`(
	`user_id` varchar(15) not null,
    `community_id` bigint not null,
    PRIMARY KEY (`user_id`,`community_id`),
	FOREIGN KEY (`user_id`) REFERENCES user(`user_name`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`community_id`) REFERENCES community(`id`) ON DELETE CASCADE ON UPDATE CASCADE
);
