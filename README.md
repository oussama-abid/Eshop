# Eshop

This project is developed in Java version "1.8.0_411".

## Project Description

Eshop is a Java application that enables users to browse and purchase products. It offers extensive functionalities for both customers and administrators, featuring user-friendly interfaces for authentication, product management, order management, user management, and more.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 22.0.1
- JavaFX SDK 22.0.1

### Installation

1. Clone the repository.
2. download JavaFx https://gluonhq.com/products/javafx/ 22.0.1
3. Import JavaFX libraries.


## Running the Project

### Using CLI and GUI

Navigate to `ui/Main.java` and select the preferred user interface to run.

### Using Server-Client version (CuI or GUI)

1. Start the server: `Server/EshopServer.java`.
2. Run the CUI or GUI client using `ui/Main.java`.
   
    Note: Uncomment `//listenForServerMessages();` inside `requestArticleList()` in `client/ServerRequest.java` to test real-time updates.

## Scenarios

### Customer Section

1. **Login**: 
   - Username: `customer`
   - Password: `customer`
   - Or register as a new customer.

2. **Explore Eshop**:
   - Add products to cart.
   - View cart items.
   - Complete purchase process.

### Admin Section

1. **Login**: 
   - Username: `admin2`
   - Password: `admin2`

2. **Explore Sections**:
   - Manage articles.
   - View article history and shop operations.
   - Manage employees.
   - Manage customers.

### Server-Client Version

1. **Running Server**. `Server/EshopServer.java`
2. **Running Clients**: ui/Main.java
   - Use the same login credentials as above , explore articles list , add new articles
     
   Note: Uncomment `//listenForServerMessages();` inside `requestArticleList()` in `client/ServerRequest.java` to test real-time updates.


### Server-Client Communication protocols :

The transport layer protocol used is TCP, which ensures reliable and ordered delivery of data between the server and clients. By default, The Java ServerSocket and Socket classes use TCP to establish connections and communicate between the server and client.
The application layer protocol is a custom text-based protocol where commands and responses are exchanged as plain text strings following predefined formats.










