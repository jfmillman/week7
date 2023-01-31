DROP TABLE IF EXISTS material;
DROP TABLE IF EXISTS step;
DROP TABLE IF EXISTS project_category;
DROP TABLE IF EXISTS project;
DROP TABLE IF EXISTS category;

CREATE TABLE category (
category_id INT AUTO_INCREMENT NOT NULL,
category_name VARCHAR(128) NOT NULL,
PRIMARY KEY (category_id)
);

CREATE TABLE project (
project_id INT AUTO_INCREMENT NOT NULL,
project_name VARCHAR(128) NOT NULL,
estimated_hours DECIMAL(7,2) NULL,
actual_hours DECIMAL(7,2) NULL,
difficulty INT NULL,
notes TEXT NULL,
PRIMARY KEY (project_id)
);

CREATE TABLE project_category (
project_id INT  NOT NULL,
category_id INT NOT NULL,
FOREIGN KEY (project_id) REFERENCES project (project_id) ON DELETE CASCADE,
FOREIGN KEY (category_id) REFERENCES category (category_id) ON DELETE CASCADE,
UNIQUE KEY (project_id, category_id)
);

CREATE TABLE step (
step_id INT AUTO_INCREMENT NOT NULL,
project_id INT NOT NULL,
step_text TEXT NOT NULL,
step_order INT NOT NULL,
PRIMARY KEY (step_id),
FOREIGN KEY (project_id) REFERENCES project (project_id) ON DELETE CASCADE
);

CREATE TABLE material (
material_id INT AUTO_INCREMENT NOT NULL,
project_id INT NOT NULL,
material_name VARCHAR(128) NOT NULL,
num_required INT NULL,
cost DECIMAL(7,2) NULL,
PRIMARY KEY (material_id),
FOREIGN KEY (project_id) REFERENCES project (project_id)
);

INSERT INTO project (project_name, estimated_hours, actual_hours, difficulty, notes) VALUES ('Sewing', 4, 5, 2, 'Dont poke yourself!');
INSERT INTO material (project_id, material_name, num_required, cost) VALUES (1, 'needle', 1, 1.50);
INSERT INTO material (project_id, material_name, num_required, cost) VALUES (1, 'thread', 1, 2.99);
INSERT INTO material (project_id, material_name, num_required, cost) VALUES (1, 'garment', 1, 10);
INSERT INTO step (project_id, step_text, step_order) VALUES (1, 'thread needle', 1);
INSERT INTO step (project_id, step_text, step_order) VALUES (1, 'sew garment', 2);
INSERT INTO category (category_id, category_name) VALUES (1, 'fashion');
INSERT INTO category (category_id, category_name) VALUES (2, 'hobbies');
INSERT INTO category (category_id, category_name) VALUES (3, 'fixing');
INSERT INTO project_category (project_id, category_id) VALUES (1, 1);
INSERT INTO project_category (project_id, category_id) VALUES (1, 2);
INSERT INTO project_category (project_id, category_id) VALUES (1, 3);

INSERT INTO project (project_name, estimated_hours, actual_hours, difficulty, notes) VALUES ('Chocolate making', 10, 15, 3, 'Let the chocolate set!');
INSERT INTO material (project_id, material_name, num_required, cost) VALUES (2, 'bag of chocolate', 1, 3.50);
INSERT INTO material (project_id, material_name, num_required, cost) VALUES (2, 'chocolate mold', 1, 12.99);
INSERT INTO material (project_id, material_name, num_required, cost) VALUES (2, 'bowl', 1, 4.00);
INSERT INTO material (project_id, material_name, num_required, cost) VALUES (2, 'microwave', 1, 100.00);
INSERT INTO material (project_id, material_name, num_required, cost) VALUES (2, 'spoon', 1, 1.10);
INSERT INTO step (project_id, step_text, step_order) VALUES (2, 'pour chocolate into bowl', 1);
INSERT INTO step (project_id, step_text, step_order) VALUES (2, 'melt chocolate in microwave', 2);
INSERT INTO step (project_id, step_text, step_order) VALUES (2, 'mix chocolate with spoon until smooth', 3);
INSERT INTO step (project_id, step_text, step_order) VALUES (2, 'pour chocolate into mold', 4);
INSERT INTO step (project_id, step_text, step_order) VALUES (2, 'let chocolate set in mold', 5);
INSERT INTO step (project_id, step_text, step_order) VALUES (2, 'remove chocolate from mold', 6);
INSERT INTO category (category_id, category_name) VALUES (4, 'food');
INSERT INTO category (category_id, category_name) VALUES (5, 'crafts');
INSERT INTO category (category_id, category_name) VALUES (6, 'fun');
INSERT INTO project_category (project_id, category_id) VALUES (2, 1);
INSERT INTO project_category (project_id, category_id) VALUES (2, 2);
INSERT INTO project_category (project_id, category_id) VALUES (2, 3);