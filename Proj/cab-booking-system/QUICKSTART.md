# 🚀 Quick Start Guide

## 5-Minute Setup

### Step 1: Prerequisites Check
```bash
java -version          # Should be 24
mvn -version           # Should be 3.6+
mysql --version        # Should be 8.0+
```

### Step 2: Create Database
```sql
CREATE DATABASE cab_booking_db;
CREATE USER 'cab_user'@'localhost' IDENTIFIED BY 'cab_password';
GRANT ALL PRIVILEGES ON cab_booking_db.* TO 'cab_user'@'localhost';
FLUSH PRIVILEGES;
```

### Step 3: Update Configuration
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cab_booking_db
spring.datasource.username=cab_user
spring.datasource.password=cab_password
```

### Step 4: Run Application
```bash
# From project root directory
mvn clean install
mvn spring-boot:run
```

### Step 5: Access Application
```
Home: http://localhost:8080
```

## 🔐 Test Accounts

### Create User Account
1. Go to: http://localhost:8080/auth/register
2. Fill in the form
3. Click "Create Account"

### Admin Access
- First create a user, then add `ADMIN` role in database:
```sql
UPDATE users SET role='ADMIN' WHERE email='your_email@example.com';
```
- Access admin panel: http://localhost:8080/admin/dashboard

## 📱 Key Pages

| Page | URL | Purpose |
|------|-----|---------|
| Home | `/` | Landing page |
| Login | `/auth/login` | User login |
| Register | `/auth/register` | New user registration |
| Book Ride | `/bookings/book` | Book a cab |
| My Bookings | `/bookings/my-bookings` | View booking history |
| Admin Dashboard | `/admin/dashboard` | Admin panel |

## 🎯 Test Workflow

1. **Register**: Create a new user account
2. **Book**: Try booking a ride from one location to another
3. **Estimate**: Check estimated cost calculation
4. **View**: See booking in "My Bookings"
5. **Admin**: Access admin dashboard to view bookings

## 🔧 Common Issues & Solutions

### "Connection refused" error
```
Solution: Make sure MySQL is running
# On Windows
net start MySQL80

# On Mac
brew services start mysql

# On Linux
sudo systemctl start mysql
```

### "Port 8080 already in use"
```
Edit application.properties:
server.port=8081
```

### Thymeleaf template not found
```
Ensure templates are at:
src/main/resources/templates/
```

## 📚 Learning Path

1. **Day 1**: Understand project structure
   - Review entity classes
   - Study repository interfaces
   - Examine service layer

2. **Day 2**: Learn business logic
   - Study BookingService implementation
   - Review LocationService calculations
   - Understand booking workflow

3. **Day 3**: Frontend & Controllers
   - Review controller methods
   - Study Thymeleaf templates
   - Understand form handling

4. **Day 4**: Security & Admin
   - Study SecurityConfig
   - Review admin dashboard
   - Understand role-based access

5. **Day 5**: Enhancements
   - Add new features
   - Improve UI/UX
   - Add more business logic

## 💾 Database Schema

```sql
-- Users table
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    role VARCHAR(50) NOT NULL,
    address VARCHAR(500) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Drivers table
CREATE TABLE drivers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT UNIQUE NOT NULL,
    license_number VARCHAR(50) NOT NULL,
    vehicle_number VARCHAR(50) NOT NULL,
    vehicle_model VARCHAR(100) NOT NULL,
    vehicle_type VARCHAR(50) NOT NULL,
    current_latitude DOUBLE NOT NULL,
    current_longitude DOUBLE NOT NULL,
    status VARCHAR(50) NOT NULL,
    rating DOUBLE DEFAULT 5.0,
    total_rides INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Bookings table
CREATE TABLE bookings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    driver_id BIGINT,
    pickup_location VARCHAR(255) NOT NULL,
    dropoff_location VARCHAR(255) NOT NULL,
    estimated_distance DOUBLE NOT NULL,
    estimated_duration INT NOT NULL,
    estimated_cost DECIMAL(10,2) NOT NULL,
    actual_cost DECIMAL(10,2),
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_rating DOUBLE DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (driver_id) REFERENCES drivers(id)
);
```

## 🎓 Tips for Learning

✅ Start by running the application and exploring
✅ Create test bookings and trace through the code
✅ Modify UI to understand Thymeleaf
✅ Add new fields to entities and databases
✅ Implement new features on top of this base

## 🚨 Debugging Tips

**Enable debug logging**:
```properties
logging.level.com.cabbooking=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

**Check database data**:
```bash
mysql -u cab_user -p cab_booking_db
SELECT * FROM users;
SELECT * FROM bookings;
```

**View Thymeleaf errors**:
Open browser console (F12) for JavaScript errors

## 📞 Need Help?

1. Check the README.md for detailed documentation
2. Review the source code comments
3. Check Spring Boot documentation: https://spring.io/projects/spring-boot
4. Study Hibernate docs: https://hibernate.org/

Happy learning! 🎉
