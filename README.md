# E-Commerce API

A robust RESTful API built with Spring Boot for managing e-commerce operations including categories, products, and user management.

## 🚀 Features

- **Category Management**: CRUD operations for product categories
- **Product Management**: Full product lifecycle management with image support
- **RESTful API**: Clean, well-structured REST endpoints
- **Pagination & Sorting**: Advanced data retrieval with pagination and sorting
- **Validation**: Input validation using Jakarta Validation
- **Exception Handling**: Comprehensive error handling with custom exceptions
- **Database Integration**: MySQL database with JPA/Hibernate
- **Bulk Operations**: Support for bulk category and product creation

## 🛠️ Technology Stack

- **Java 17**
- **Spring Boot 3.5.4**
- **Spring Data JPA**
- **MySQL Database**
- **Maven**
- **Lombok**
- **ModelMapper**

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd E-comm
```

### 2. Database Setup

1. Create a MySQL database named `Store`
2. Update database credentials in `src/main/resources/application.properties`:

```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Run the Application

```bash
# Using Maven
mvn spring-boot:run

# Or using Maven wrapper
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api
```

### Category Endpoints

#### Get All Categories
```http
GET /public/categories?pageNumber=0&pageSize=10&sortBy=title&sortOrder=asc
```

#### Create Category
```http
POST /public/categories
Content-Type: application/json

{
  "title": "Electronics",
  "description": "Electronic devices and accessories"
}
```

#### Create Bulk Categories
```http
POST /public/bulk
Content-Type: application/json

[
  {
    "title": "Electronics",
    "description": "Electronic devices"
  },
  {
    "title": "Clothing",
    "description": "Fashion and apparel"
  }
]
```

#### Get Category by ID
```http
GET /public/categories/{categoryId}
```

#### Update Category
```http
PUT /admin/categories/{categoryId}
Content-Type: application/json

{
  "title": "Updated Electronics",
  "description": "Updated description"
}
```

#### Delete Category
```http
DELETE /admin/categories/{categoryId}
```

#### Delete All Categories
```http
DELETE /deleteAll
```

### Product Endpoints

#### Create Product
```http
POST /admin/categories/{categoryId}/products
Content-Type: application/json

{
  "title": "iPhone 15",
  "description": "Latest iPhone model",
  "price": 999.99,
  "quantity": 50
}
```

#### Create Bulk Products
```http
POST /admin/category/{categoryId}/products
Content-Type: application/json

[
  {
    "title": "iPhone 15",
    "description": "Latest iPhone model",
    "price": 999.99,
    "quantity": 50
  }
]
```

#### Get All Products
```http
GET /admin/categories/products?pageNumber=0&pageSize=10&sortBy=title&sortOrder=asc
```

#### Get Product by ID
```http
GET /admin/categories/products/{productId}
```

#### Search Products by Keyword
```http
GET /products/{keyword}?pageNumber=0&pageSize=10&sortBy=title&sortOrder=asc
```

#### Get Products by Category
```http
GET /admin/categories/{categoryId}/products?pageNumber=0&pageSize=10&sortBy=title&sortOrder=asc
```

#### Update Product
```http
PUT /admin/products/{productId}
Content-Type: application/json

{
  "title": "Updated iPhone 15",
  "description": "Updated description",
  "price": 899.99,
  "quantity": 45
}
```

#### Update Product Image
```http
PUT /products/{productId}/image
Content-Type: multipart/form-data

image: [file]
```

#### Delete Product
```http
DELETE /admin/products/{productId}
```

## 🔧 Configuration

### Application Properties

Key configuration options in `src/main/resources/application.properties`:

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/Store
spring.datasource.username=root
spring.datasource.password=your_password

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Constants

Application constants are defined in `AppConstant.java`:
- Default page number: 0
- Default page size: 10
- Default sorting fields and directions

## 🏗️ Project Structure

```
src/main/java/com/ecommerce/project/
├── config/           # Configuration classes
├── controller/       # REST controllers
├── exception/        # Custom exceptions and handlers
├── helper/          # Helper classes and enums
├── model/           # Entity classes
├── payload/         # DTOs and response classes
├── repositery/      # Data access layer
└── service/         # Business logic layer
```

## 🧪 Testing

Run tests using Maven:

```bash
mvn test
```

## 📝 API Response Format

### Success Response
```json
{
  "content": [...],
  "pageNumber": 0,
  "pageSize": 10,
  "totalElements": 100,
  "totalPages": 10,
  "lastPage": false
}
```

### Error Response
```json
{
  "message": "Error description",
  "success": false,
  "status": "BAD_REQUEST"
}
```

## 🔒 Security Notes

- Public endpoints are accessible without authentication
- Admin endpoints require proper authorization (to be implemented)
- Input validation is enforced on all endpoints
- SQL injection protection through JPA/Hibernate

## 🚧 Future Enhancements

- [ ] User authentication and authorization
- [ ] Shopping cart functionality
- [ ] Order management
- [ ] Payment integration
- [ ] Image storage service
- [ ] API documentation with Swagger/OpenAPI
- [ ] Unit and integration tests
- [ ] Docker containerization

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 👥 Support

For support and questions, please open an issue in the repository.

---

**Note**: This is a demo project for Spring Boot. Update the database credentials and configuration before running in production.
