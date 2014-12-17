CREATE DATABASE IF NOT EXISTS PROJETRI;

CREATE TABLE IF NOT EXISTS PROJETRI.TAB_METADATA (
  ID int(11) NOT NULL AUTO_INCREMENT,
  FILENAME varchar(255) NOT NULL,
  TITRE varchar(255) NOT NULL,
  AUTEUR varchar(255) NOT NULL,
  DATE date NOT NULL,
  SUJET varchar(255) NOT NULL,
  PRIMARY KEY (ID)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

INSERT INTO PROJETRI.TAB_METADATA (ID, FILENAME, TITRE, AUTEUR, DATE, SUJET) VALUES
(1, 'vu1.html', 'Bob au pays des Bobs', 'Bobby', '2014-12-01', 'Les aventures de Bob'),
(2, 'vu10.html', 'Les péripéties de Bob', 'Bobby', '2014-12-02', 'Epreuve de Bob'),
(3, 'vu11.html', 'Bob le grand', 'Bab le baobab', '2014-12-03', 'Les aventures d''un arbre'),
(4, 'vu12.html', 'Bab le grand', 'Teh', '2014-12-15', 'Sciences'),
(5, 'vu195.html', 'Bob le docteur', 'Dr DAO', '2014-12-13', 'Médecine');
