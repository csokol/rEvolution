create table metrics (id integer primary key autoincrement, name varchar(200), value double, target varchar(200), level varchar(200), tool varchar(200), changeset_id integer);
create table changesets (id integer primary key autoincrement, project varchar(200), name varchar(200), date datetime);