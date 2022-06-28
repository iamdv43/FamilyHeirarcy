create schema project;

use project;

CREATE TABLE `familyinfo` (
  `id` integer not null,
  `name` varchar(20) DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `birthlocation` varchar(20) DEFAULT NULL,
  `dod` date DEFAULT NULL,
  `deathlocation` varchar(20) DEFAULT NULL,
  `gender` varchar(20) DEFAULT NULL,
  constraint id_p primary key (id)  
);

CREATE TABLE `occupationinfo` (
  `id` int not NULL,
  `occupation` varchar(100) DEFAULT NULL,
  FOREIGN KEY (id) REFERENCES familyinfo(id)
);

CREATE TABLE `noterefinfo` (
  `id` int not NULL,
  `notes` varchar(255) DEFAULT NULL,
  `checkn` int DEFAULT NULL,
   FOREIGN KEY (id) REFERENCES familyinfo(id)
);


CREATE TABLE `childinfo` (
  `id` int not NULL,
  `child` varchar(255) DEFAULT NULL,
   FOREIGN KEY (id) REFERENCES familyinfo(id)
);

CREATE TABLE `partneringinfo` (
  `id` int not NULL,
  `partner` varchar(255) DEFAULT NULL,
  `dissolution` varchar(255) DEFAULT NULL,
  FOREIGN KEY (id) REFERENCES familyinfo(id)
);

CREATE TABLE `mediainfo` (
  `fileIdentifier` varchar(100),
  `dateOfPicture` date DEFAULT NULL,
  `Location` varchar(100) DEFAULT NULL,
  constraint fI_p primary key (fileIdentifier)
);

CREATE TABLE `peoplemediainfo` (
  `fileIdentifier` varchar(100) DEFAULT NULL,
  `peopleid` int DEFAULT NULL,
  FOREIGN KEY (fileIdentifier) REFERENCES mediainfo(fileIdentifier),
  foreign key (peopleid) references familyinfo(id)
);

CREATE TABLE `tagmediainfo` (
  `fileIdentifier` varchar(100) DEFAULT NULL,
  `tags` varchar(100) DEFAULT NULL,
  FOREIGN KEY (fileIdentifier) REFERENCES mediainfo(fileIdentifier)
);

CREATE TABLE `mediadatemethod` (
  `fileIdentifier` varchar(100) NOT NULL,
  `dateOfPicture` date DEFAULT NULL,
  FOREIGN KEY (fileIdentifier) REFERENCES mediainfo(fileIdentifier)
);





