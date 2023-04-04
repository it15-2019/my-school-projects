--________________________TEST PODACI________________________
insert into fakultet 
	values (-100, 'Test naziv', 'Test sediste');

insert into status 
	values (-100, 'Test naziv', 'Test');
	
insert into departman 
	values (-100, 'Test naziv', 'Test', -100);

insert into student 
	values (-100, 'Test ime', 'Test prezime', 'Test br indeksa', -100, -100);

--________________________FAKULTET________________________

insert into fakultet (id, naziv, sediste) values
	(nextval('seq_fakultet'), 'Fakultet tehnickih nauka', 'Novi Sad'),
	(nextval('seq_fakultet'), 'Prirodno matematicki fakultet', 'Novi Sad'),
	(nextval('seq_fakultet'), 'Filozofski fakultet', 'Novi Sad'),
	(nextval('seq_fakultet'), 'Poljoprivredni fakultet', 'Novi Sad'),
	(nextval('seq_fakultet'), 'Pravni fakultet', 'Novi Sad');
	
--________________________STATUS________________________

insert into status (id, naziv, oznaka) values
	(nextval('seq_status'), 'status1', '111111'),
	(nextval('seq_status'), 'status2', '222222'),
	(nextval('seq_status'), 'status3', '333333'),
	(nextval('seq_status'), 'status4', '444444'),
	(nextval('seq_status'), 'status5', '555555');
	
--________________________DEPARTMAN________________________

insert into departman(id, naziv, oznaka, fakultet) values
	(nextval('seq_departman'), 'Industrijsko inzenjerstvo i menadzment', '1.1', 1),
	(nextval('seq_departman'), 'Turizam i hotelijerstvo', '2.1', 2),
	(nextval('seq_departman'), 'Strani jezici', '3.1', 3),
	(nextval('seq_departman'), 'Fitomedicina', '4.1', 4),
	(nextval('seq_departman'), 'Pravo', '5.1', 5);
		
--________________________STUDENT________________________

insert into student(id, ime, prezime, broj_indeksa, status, departman) values
	(nextval('seq_student'), 'Sofija', 'Dangubic', 'IT15-2019', 1, 1),
	(nextval('seq_student'), 'Dragana', 'Kokot', 'HI107-2019', 2, 2),
	(nextval('seq_student'), 'Olja', 'Rasic', 'EJ55-2017', 3, 3),
	(nextval('seq_student'), 'Miljan', 'Miljanovic', 'FT32-2018', 4, 4),
	(nextval('seq_student'), 'Mladen', 'Margetic', 'PR40-2018', 5, 5),
	(nextval('seq_student'), 'S', 'S', 'PR40-S', 5, 5);;
