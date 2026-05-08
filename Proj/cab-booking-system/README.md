# 🚖 Online Cab Booking System

An intermediate-level Java Spring Boot project for building a complete online cab management system with user, driver, admin, map, AI optimization, safety, and IoT/circuit integration modules.

The implementation now follows a structured **MBB (Module-Block-Bridge)** architecture. See [MBB_ARCHITECTURE.md](MBB_ARCHITECTURE.md) for module boundaries, blocks, event bridges, and scaling notes.

## 📋 Features

### User Module
- ✅ User registration and authentication
- ✅ Book cabs with pickup/drop-off locations
- ✅ Pokhara, Nepal map booking with pickup/drop-off pins
- ✅ Real-time distance and cost estimation at Rs. 50/km
- ✅ Vehicle type, ride schedule, passenger count, payment mode, promo code, and ride notes
- ✅ Automatic nearest-driver assignment
- ✅ AI driver-passenger matching based on proximity, rating, and vehicle fit
- ✅ Smart route suggestions based on user habits
- ✅ Ride-sharing opt-in and compatible shared ride detection
- ✅ Offline fallback navigation and route instruction decoding
- ✅ Voice-assisted booking flag
- ✅ View booking history
- ✅ Track booking status
- ✅ Emergency/SOS alert from active booking details
- ✅ Rate and review rides
- ✅ Cancel bookings

### Admin Module
- ✅ Manage drivers
- ✅ View all bookings
- ✅ Assign drivers to bookings
- ✅ Auto-assign closest available driver
- ✅ Track ride status
- ✅ Monitor system statistics
- ✅ View user profiles
- ✅ AI & IoT operations dashboard
- ✅ Predictive demand heatmaps
- ✅ SOS alert acknowledgement and resolution
- ✅ Traffic signal bridge that invalidates route cache through events

### Location Services
- ✅ Haversine formula for distance calculation
- ✅ A* graph pathfinding across Pokhara landmarks
- ✅ Turn-by-turn, landmark-based route instructions
- ✅ Route caching with traffic revision invalidation
- ✅ Dynamic pricing based on demand, vehicle type, and promo codes

### IoT & Safety
- ✅ Token-protected telemetry bridge at `POST /api/iot/telemetry`
- ✅ GPS, fuel, engine temperature, braking, acceleration, and speed capture
- ✅ Driver behavior monitoring and risk scoring
- ✅ Event-driven driver location updates after meaningful movement
- ✅ Fraud and anomaly scoring for new bookings

## 🛠️ Technology Stack

- **Backend Framework:** Spring Boot 3.2.0
- **Security:** Spring Security with BCrypt password encoding
- **ORM:** Hibernate with JPA
- **Database:** H2 by default, MySQL 8.0 optional
- **Frontend:** Thymeleaf, Bootstrap 5, HTML5/CSS3
- **Build Tool:** Maven
- **Java Version:** 24

## 📦 Project Structure

```
cab-booking-system/
├── src/main/
│   ├── java/com/cabbooking/
│   │   ├── CabBookingApplication.java       (Main app class)
│   │   ├── entity/                          (JPA entities)
│   │   │   ├── User.java
│   │   │   ├── Driver.java
│   │   │   ├── Booking.java
│   │   │   ├── UserRole.java
│   │   │   ├── DriverStatus.java
│   │   │   └── BookingStatus.java
│   │   ├── repository/                      (Data access layer)
│   │   │   ├── UserRepository.java
│   │   │   ├── DriverRepository.java
│   │   │   └── BookingRepository.java
│   │   ├── service/                         (Business logic layer)
│   │   │   ├── UserService.java
│   │   │   ├── DriverService.java
│   │   │   ├── BookingService.java
│   │   │   └── LocationService.java
│   │   ├── mbb/                             (Module-Block-Bridge architecture)
│   │   │   ├── bridge/event                 (Spring domain events)
│   │   │   └── module/
│   │   │       ├── ai                       (Matching, demand, pricing, fraud)
│   │   │       ├── iot                      (Telemetry and behavior monitoring)
│   │   │       ├── map                      (A*, graph, Haversine, cache)
│   │   │       └── safety                   (SOS alerts)
│   │   ├── controller/                      (Web controllers)
│   │   │   ├── HomeController.java
│   │   │   ├── AuthController.java
│   │   │   ├── BookingController.java
│   │   │   └── AdminController.java
│   │   └── config/                          (Configuration)
│   │       ├── SecurityConfig.java
│   │       └── CustomUserDetailsService.java
│   └── resources/
│       ├── application.properties            (App configuration)
│       └── templates/                        (Thymeleaf templates)
│           ├── auth/
│           ├── booking/
│           └── admin/
├── pom.xml                                   (Maven dependencies)
└── README.md
```

## 🚀 Getting Started

### Prerequisites
- Java 24
- Maven 3.6+
- Optional: MySQL Server 8.0+ for the `mysql` profile
- Git

### Installation & Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd cab-booking-system
   ```

2. **Build the Project**
   ```bash
   mvn clean install
   ```

3. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the Application**
   - Open browser: `http://localhost:8080`
   - Homepage: `http://localhost:8080/`


### Optional MySQL Setup

The default configuration uses an in-memory H2 database so the app runs immediately on a local machine. To use MySQL instead:

1. Create the database:
   ```sql
   CREATE DATABASE cab_booking_db;
   ```
2. Update `src/main/resources/application-mysql.properties` with your MySQL username and password.
3. Run the app with the MySQL profile:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=mysql
   ```

## 👤 Default Credentials

### User Registration
- Register a new user through `/auth/register`

### Admin Access
- Email: `admin@example.com`
- Password: `admin123`
- (Add this user manually or create registration endpoint for admin)

## 📖 Usage Guide

### For Users

1. **Registration**: Click "Register" and create an account
2. **Book a Ride**: 
   - Login to your account
   - Click "Book Now"
   - Enter pickup and dropoff locations
   - View estimated fare and duration
   - Confirm booking
3. **Track Ride**: View booking status in real-time
4. **Rate Driver**: After ride completion, rate and review

### For Admins

1. **Access Dashboard**: `http://localhost:8080/admin/dashboard`
2. **Manage Drivers**:
   - View all registered drivers
   - Check driver ratings and ride history
3. **Manage Bookings**:
   - View pending bookings
   - Assign drivers to bookings
   - Track ride status
4. **View Analytics**: Dashboard shows total users, drivers, and pending bookings

## 🎓 Learning Outcomes

This project teaches you:

✅ **Spring Boot Development**
- Creating REST controllers
- Service-oriented architecture
- Dependency injection

✅ **Spring Security**
- User authentication and authorization
- Password encoding with BCrypt
- Role-based access control

✅ **Hibernate & JPA**
- Entity mapping and relationships
- Query methods with JpaRepository
- Lifecycle management

✅ **Database Design**
- Relational database schema
- Foreign key relationships
- Data persistence

✅ **Frontend Development**
- Thymeleaf template engine
- Bootstrap framework integration
- Form handling and validation

✅ **Business Logic Implementation**
- Location-based services
- Cost calculation algorithms
- Booking workflow management

## 🔧 API Endpoints

### Authentication
- `POST /auth/register` - Register new user
- `POST /auth/login` - Login user
- `POST /auth/logout` - Logout user

### Bookings
- `GET /bookings/book` - Show booking form
- `POST /bookings/create` - Create new booking
- `GET /bookings/{id}` - View booking details
- `GET /bookings/my-bookings` - View user's bookings
- `POST /bookings/{id}/cancel` - Cancel booking
- `GET /bookings/{id}/rate` - Show rating form
- `POST /bookings/{id}/rate` - Submit rating

### Admin
- `GET /admin/dashboard` - Admin dashboard
- `GET /admin/drivers` - List drivers
- `GET /admin/bookings` - List bookings
- `POST /admin/bookings/{id}/assign/{driverId}` - Assign driver
- `POST /admin/bookings/{id}/start` - Start ride
- `POST /admin/bookings/{id}/complete` - Complete ride
- `GET /admin/intelligence` - AI, IoT, route cache, heatmap, and SOS dashboard
- `POST /admin/traffic/signal` - Publish traffic signal and invalidate route cache

### MBB Bridge APIs
- `GET /api/routes/preview` - Route, fare, demand, driver match, and instruction preview
- `POST /api/iot/telemetry` - Token-protected vehicle telemetry ingestion

## 💡 Key Classes & Methods

### LocationService
- `calculateDistance()` - Uses Haversine formula for GPS distance
- `estimateDuration()` - Estimates travel time based on distance
- `calculateCost()` - Calculates fare based on distance and time

### BookingService
- `createBooking()` - Creates new booking with estimates
- `acceptBooking()` - Assigns driver to booking
- `completeBooking()` - Marks ride as complete
- `rateBooking()` - Records user rating and updates driver rating

### SecurityConfig
- JWT-free authentication using Spring Security
- Role-based access control (USER, ADMIN, DRIVER)
- Password encoding with BCrypt

## 🐛 Troubleshooting

### Database Connection Error
```
Solution: The default H2 setup needs no external database. If using the `mysql` profile, ensure MySQL is running and credentials in application-mysql.properties are correct
```

### Port Already in Use
```
Solution: Change port in application.properties
server.port=8081
```

### Thymeleaf Template Not Found
```
Solution: Ensure templates are in src/main/resources/templates/
```

## 📝 Future Enhancements

- [ ] Real-time map integration with Google Maps API
- [ ] SMS/Email notifications
- [ ] Payment gateway integration (Stripe, PayPal)
- [ ] Driver mobile app
- [ ] Real-time chat between user and driver
- [ ] Promo codes and referral system
- [ ] Advanced analytics and reporting
- [ ] Push notifications

## 📄 License

This project is open source and available under the MIT License.

## 👨‍💻 Author

Created as an intermediate-level Spring Boot project for learning purposes.

## 🤝 Contributing

Feel free to fork, modify, and use this project for your learning!

## 📞 Support

For questions or issues, please open an issue on the repository.

---

**Happy Coding! 🚀**
