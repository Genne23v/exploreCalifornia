INSERT INTO security_user (id, username, password, first_name, last_name) VALUES
(1,  'admin', '$2a$12$ZhGS.zcWt1gnZ9xRNp7inOvo5hIT0ngN7N.pN939cShxKvaQYHnnu', 'Administrator', 'Adminstrator'),
(2,  'csr_jane', '$2a$12$ZhGS.zcWt1gnZ9xRNp7inOvo5hIT0ngN7N.pN939cShxKvaQYHnnu', 'Jane', 'Doe'),
(3,  'csr_mark', '$2a$12$ZhGS.zcWt1gnZ9xRNp7inOvo5hIT0ngN7N.pN939cShxKvaQYHnnu', 'Mark', 'Smith'),
(4,  'wally', '$2a$12$ZhGS.zcWt1gnZ9xRNp7inOvo5hIT0ngN7N.pN939cShxKvaQYHnnu', 'Walter', 'Adams');

INSERT INTO user_role(user_id, role_id) VALUES
 (1, 1),
 (2, 2),
 (3, 2),
 (4, 1),
 (4, 2);
