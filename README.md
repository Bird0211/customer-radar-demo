# customer-radar-demo

## Description
  >This is a demo application that provides CRUD (create, read, update, delete) RESTful services for customer data.
  
  *Each customer has a*
  
	name
  
	address
  
	telephone number
  
  
## Getting started
### SQL

>CREATE TABLE `mee_store`.`t_customer_radar_user` (
`id` BIGINT(20) NOT NULL,
`name` VARCHAR(50) NOT NULL,
`address` VARCHAR(500) NULL,
`phone` VARCHAR(14) NOT NULL,
PRIMARY KEY (`id`),
UNIQUE INDEX `UK_PHONE` (`phone` ASC) VISIBLE);
  
 ### Technology
  *Java: 11*
  
  *MySql: 8*
  
### Function
  CreateUser:  http://localhost:8080/api/user/add  PUT
  
  GetUserById:  http://localhost:8080/api/user/{id}  GET
  
  GetUserByPhone:  http://localhost:8080/api/user/phone/{phone} GET
  
  UpdateUser:  http://localhost:8080/api/user/update POST
  
  DeleteUser:  http://localhost:8080/api/user/del/{id}  DELETE
  
  
### ErrorCode

 
| Error Code        | Description   | 
| --------   | -----:  |
| 0  | SUCCESS  |
| 1  | FAIL  |
| 11111  | Sys Error  |
| 118001  | DB Error  |
| 118002 | User Not Exist |
| 118003| Param Error|
  
