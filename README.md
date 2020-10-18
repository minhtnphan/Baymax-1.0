# Baymax1.0
This is a ReadMe for Baymax 1.0 - a heathcare recommender application.
---
### Table of Contents
- [Description](#description)
- [How To Use](#how-to-use)
- [References](#references)
- [Contributors](#contributors)
- [License & copyright](#license&copyright)
---
## Description
Baymax 1.0 is a health recommender system that recommend users the list of hospitals that have the best-ranking 
department for their sickness. The recommendation list is based on admin's department rankings and user's 
symptom input. The list can be filtered by price, location and whether the recommended hospital is applied insurance 
or not.
[Back To The Top](#Baymax1.0)
---
## How To Use
#### For admin
###### Database (using Rest API)
- Insert users data and their roles (admin/user)
- Insert hospital-department data and rankings
- Admin has permissions to use rest api to insert, update, delete, get all information from users data and 
hospital-department data
###### For admin and user (using Rest API)
- Both admin and user have permission to change their password
- Both admin and user have permission to use Baymax recommender system. They are recommended a list of best-ranking 
hospital-department based on their symptom input. This result can be filtered by location, price, and whether it's applied
insurance or not.
#### Using GUI
GUI is build for recommender system only. To use GUI, you need to put JDBC 4.x driver in the project classpath and set
up database using MySQL Workbench. The database is exactly the same as the database using Rest API.
[Back To The Top](#Baymax1.0)
---
## References
[Back To The Top](#Baymax1.0)
---
## Contributors
- Nguyen Thu Huyen <huyen.nguyen.190033@student.fulbright.edu.vn>
- Phan Nguyen Tuong Minh <minh.phan.190048@student.fulbright.edu.vn>
[Back To The Top](#Baymax1.0)
---
## License & copyright
Copyright (c) [2020] [Nguyen Thu Huyen] [Phan Nguyen Tuong Minh]
[Back To The Top](#Baymax1.0)