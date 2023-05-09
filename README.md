# Order Management System

## Overview
This is an application for managing client orders for a warehouse. It is developed using Java and follows the layered architecture pattern. The application utilizes a PostgreSQL relational database to store products, clients, and orders.

## Features
- Add, edit, delete, and view clients.
- Add, edit, delete, and view products.
- Create product orders.
- Automatic decrement of product stock after order finalization.
- Generation of bills stored in a Log table for each order.

## Structure
The application is divided into several packages:
- `client`: Contains classes for client model, repository, service, controller, and view.
- `order`: Contains classes for order model, repository, service, controller, and view.
- `product`: Contains classes for product model, repository, service, controller, and view.
- `databaseconnection`: Contains classes for establishing and managing the database connection.
- `log`: Contains classes for the Bill model and repository.

## Usage
To run the application, clone the repository and execute the main class in your Java IDE.

## Documentation
Detailed documentation for each class is available in the form of JavaDoc. 

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.


