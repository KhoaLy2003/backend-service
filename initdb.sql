SET NAMES utf8mb4;

-- backend_service.tbl_account definition

CREATE TABLE `tbl_account`
(
    `birthday`   datetime(6) DEFAULT NULL,
    `created_at` datetime(6) DEFAULT NULL,
    `id`         bigint NOT NULL AUTO_INCREMENT,
    `updated_at` datetime(6) DEFAULT NULL,
    `phone`      varchar(15)  DEFAULT NULL,
    `email`      varchar(50)  DEFAULT NULL,
    `first_name` varchar(50)  DEFAULT NULL,
    `last_name`  varchar(50)  DEFAULT NULL,
    `password`   varchar(255) DEFAULT NULL,
    `role`       enum('ADMIN','MANAGER','STAFF') DEFAULT NULL,
    `status`     enum('ACTIVE','INACTIVE') DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UKbbkmr2fn78s42ol38ss6fdlbh` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- backend_service.tbl_token definition

CREATE TABLE `tbl_token`
(
    `is_revoked`      bit(1)       DEFAULT NULL,
    `account_id`      bigint       DEFAULT NULL,
    `expiration_date` datetime(6) DEFAULT NULL,
    `id`              bigint NOT NULL AUTO_INCREMENT,
    `refresh_token`   varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY               `FKcek6yryfv9tyeseti645pwbhu` (`account_id`),
    CONSTRAINT `FKcek6yryfv9tyeseti645pwbhu` FOREIGN KEY (`account_id`) REFERENCES `tbl_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- backend_service.tbl_task definition

CREATE TABLE `tbl_task`
(
    `assigned_account` bigint       DEFAULT NULL,
    `created_account`  bigint       DEFAULT NULL,
    `created_at`       datetime(6) DEFAULT NULL,
    `id`               bigint NOT NULL AUTO_INCREMENT,
    `updated_at`       datetime(6) DEFAULT NULL,
    `title`            varchar(100) DEFAULT NULL,
    `description`      text,
    `note`             text,
    `status`           enum('CANCELLED','COMPLETED','CREATED','IN_PROGRESS') DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY                `FK5d8aniisi3obl5ydayx7gltap` (`assigned_account`),
    KEY                `FK288fcrbbbf8e7nkbk2lgjqlcq` (`created_account`),
    CONSTRAINT `FK288fcrbbbf8e7nkbk2lgjqlcq` FOREIGN KEY (`created_account`) REFERENCES `tbl_account` (`id`),
    CONSTRAINT `FK5d8aniisi3obl5ydayx7gltap` FOREIGN KEY (`assigned_account`) REFERENCES `tbl_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- backend_service.tbl_task_log definition

CREATE TABLE `tbl_task_log`
(
    `created_at`      datetime(6) DEFAULT NULL,
    `id`              bigint NOT NULL AUTO_INCREMENT,
    `task`            bigint DEFAULT NULL,
    `trigger_account` bigint DEFAULT NULL,
    `updated_at`      datetime(6) DEFAULT NULL,
    `action`          text,
    PRIMARY KEY (`id`),
    KEY               `FKb57f16avpw44w25bn417pdw5h` (`task`),
    KEY               `FK1q0tkeajneh2gllj3elaoeor5` (`trigger_account`),
    CONSTRAINT `FK1q0tkeajneh2gllj3elaoeor5` FOREIGN KEY (`trigger_account`) REFERENCES `tbl_account` (`id`),
    CONSTRAINT `FKb57f16avpw44w25bn417pdw5h` FOREIGN KEY (`task`) REFERENCES `tbl_task` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `tbl_account` (birthday, created_at, updated_at, phone, email, first_name, last_name,
                           password, `role`, status)
VALUES ('1990-01-15 00:00:00', '2024-12-22 10:00:00', '2024-12-22 10:00:00', '1234567890', 'john.doe@example.com',
        'John', 'Doe', '$2a$12$fmua1GP4fg3nR2SI6rwBnu./3Jyf6BDKRe6GE/oXy02Acb9LEck/m', 'STAFF', 'ACTIVE'),
       ('1985-05-25 00:00:00', '2024-12-22 10:05:00', '2024-12-22 10:05:00', '2345678901', 'jane.smith@example.com',
        'Jane', 'Smith', '$2a$12$fmua1GP4fg3nR2SI6rwBnu./3Jyf6BDKRe6GE/oXy02Acb9LEck/m', 'STAFF', 'ACTIVE'),
       ('1992-03-30 00:00:00', '2024-12-22 10:10:00', '2024-12-22 10:10:00', '3456789012', 'michael.brown@example.com',
        'Michael', 'Brown', '$2a$12$fmua1GP4fg3nR2SI6rwBnu./3Jyf6BDKRe6GE/oXy02Acb9LEck/m', 'MANAGER', 'ACTIVE'),
       ('1988-07-20 00:00:00', '2024-12-22 10:15:00', '2024-12-22 10:15:00', '4567890123', 'emily.davis@example.com',
        'Emily', 'Davis', '$2a$12$fmua1GP4fg3nR2SI6rwBnu./3Jyf6BDKRe6GE/oXy02Acb9LEck/m', 'STAFF', 'ACTIVE'),
       ('1995-11-11 00:00:00', '2024-12-22 10:20:00', '2024-12-22 10:20:00', '5678901234', 'chris.johnson@example.com',
        'Chris', 'Johnson', '$2a$12$fmua1GP4fg3nR2SI6rwBnu./3Jyf6BDKRe6GE/oXy02Acb9LEck/m', 'STAFF', 'INACTIVE'),
       ('1980-02-05 00:00:00', '2024-12-22 10:25:00', '2024-12-22 10:25:00', '6789012345',
        'patricia.wilson@example.com', 'Patricia', 'Wilson',
        '$2a$12$fmua1GP4fg3nR2SI6rwBnu./3Jyf6BDKRe6GE/oXy02Acb9LEck/m', 'STAFF', 'ACTIVE'),
       ('1979-08-15 00:00:00', '2024-12-22 10:30:00', '2024-12-22 10:30:00', '7890123456', 'richard.moore@example.com',
        'Richard', 'Moore', '$2a$12$fmua1GP4fg3nR2SI6rwBnu./3Jyf6BDKRe6GE/oXy02Acb9LEck/m', 'MANAGER', 'ACTIVE'),
       ('1998-12-31 00:00:00', '2024-12-22 10:35:00', '2024-12-22 10:35:00', '8901234567', 'linda.taylor@example.com',
        'Linda', 'Taylor', '$2a$12$fmua1GP4fg3nR2SI6rwBnu./3Jyf6BDKRe6GE/oXy02Acb9LEck/m', 'STAFF', 'INACTIVE'),
       ('2000-06-10 00:00:00', '2024-12-22 10:40:00', '2024-12-22 10:40:00', '9012345678',
        'robert.anderson@example.com', 'Robert', 'Anderson',
        '$2a$12$fmua1GP4fg3nR2SI6rwBnu./3Jyf6BDKRe6GE/oXy02Acb9LEck/m', 'STAFF', 'ACTIVE'),
       ('1987-09-23 00:00:00', '2024-12-22 10:45:00', '2024-12-22 10:45:00', '0123456789', 'barbara.thomas@example.com',
        'Barbara', 'Thomas', '$2a$12$fmua1GP4fg3nR2SI6rwBnu./3Jyf6BDKRe6GE/oXy02Acb9LEck/m', 'MANAGER', 'ACTIVE');

INSERT INTO `tbl_task` (assigned_account, created_account, created_at, updated_at, title, description,
                        note, status)
VALUES (1, 7, '2024-12-22 19:01:46.314000', '2024-12-22 19:04:49.922000', 'Fix machinery issue',
        'Investigate and resolve the issue with the hydraulic press.',
        'Priority task, customer waiting for resolution.', 'COMPLETED'),
       (4, 10, '2024-12-22 19:06:28.291000', '2024-12-22 19:28:08.978000', 'Update software version',
        'Upgrade the system software to the latest version for improved performance.',
        'Ensure compatibility with all machines and devices.', 'CANCELLED'),
       (9, 3, '2024-12-22 19:25:17.451000', '2024-12-22 19:25:17.451000', 'Clean machinery parts',
        'Clean all parts of the machinery to ensure smooth operation.', 'Routine maintenance task.', 'CREATED'),
       (1, 3, '2024-12-22 19:27:17.451000', '2024-12-22 19:29:15.445000', 'Replace worn-out tires',
        'Replace the worn-out tires on the truck.', 'Ensure safe transportation of goods.', 'IN_PROGRESS');

INSERT INTO `tbl_task_log` (created_at, task, trigger_account, updated_at, `action`)
VALUES ('2024-12-22 19:01:46.325000', 1, 7, '2024-12-22 19:01:46.325000',
        'Task created with title ''Fix machinery issue'' and assigned to ''Doe John'''),
       ('2024-12-22 19:04:25.193000', 1, 1, '2024-12-22 19:04:25.193000',
        'Task with title ''Fix machinery issue'' status changed to ''IN_PROGRESS'' by ''Doe John'''),
       ('2024-12-22 19:04:49.920000', 1, 1, '2024-12-22 19:04:49.920000',
        'Task with title ''Fix machinery issue'' status changed to ''COMPLETED'' by ''Doe John'''),
       ('2024-12-22 19:06:28.294000', 2, 10, '2024-12-22 19:06:28.294000',
        'Task created with title ''Update software version'' and assigned to ''Davis Emily'''),
       ('2024-12-22 19:07:55.662000', 2, 10, '2024-12-22 19:07:55.662000',
        'Task with title ''Update software version'' status changed to ''CANCELLED'' by ''Thomas Barbara'''),
       ('2024-12-22 19:25:20.002000', 3, 3, '2024-12-22 19:25:20.002000',
        'Task created with title ''Clean machinery parts'' and assigned to ''Anderson Robert'''),
       ('2024-12-22 19:27:17.453000', 4, 3, '2024-12-22 19:27:17.453000',
        'Task created with title ''Replace worn-out tires'' and assigned to ''Doe John'''),
       ('2024-12-22 19:29:15.443000', 4, 1, '2024-12-22 19:29:15.443000',
        'Task with title ''Replace worn-out tires'' status changed to ''IN_PROGRESS'' by ''Doe John''');
