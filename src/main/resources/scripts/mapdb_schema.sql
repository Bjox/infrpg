/**
 * Author:  Bjørnar W. Alvestad
 * Created: 07.apr.2018
 */

DROP TABLE IF EXISTS `Regions`;
CREATE TABLE IF NOT EXISTS `Regions` (
	`x`	INTEGER NOT NULL,
	`y`	INTEGER NOT NULL,
	`data`	BLOB NOT NULL,
	PRIMARY KEY(`y`,`x`)
);
