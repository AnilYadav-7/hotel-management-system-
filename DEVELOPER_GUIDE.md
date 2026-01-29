# Hotel Management System - Developer Quick Reference

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Node.js 16+
- MySQL 8.0+
- Maven 3.8+

### Database Setup
```sql
-- Create databases (auto-created by Hibernate)
-- Just ensure MySQL is running on localhost:3306
-- Credentials: root / Anil@123
```

### Start Services (in order)
```bash
# 1. Eureka Server (Service Discovery)
cd eureka-server
mvn spring-boot:run
# Runs on http://localhost:8761

# 2. API Gateway
cd api-gateway
mvn spring-boot:run
# Runs on http://localhost:8080

# 3. Auth Service
cd auth-service
mvn spring-boot:run
# Runs on http://localhost:8081

# 4. Rooms Service
cd rooms-service
mvn spring-boot:run
# Runs on http://localhost:8082

# 5. Guests Service
cd guests-service
mvn spring-boot:run
# Runs on http://localhost:8083

# 6. Bookings Service
cd bookings-service
mvn spring-boot:run
# Runs on http://localhost:8084

# 7. Form Service
cd form-service
mvn spring-boot:run
# Runs on http://localhost:8085

# 8. Frontend
cd frontend
npm install
npm start
# Runs on http://localhost:3000
```

---

## 👥 Test Accounts

### Manager Account
```
Username: manager
Password: manager123
Role: ROLE_MANAGER
```

### Receptionist Account
```
Username: receptionist
Password: receptionist123
Role: ROLE_RECEPTIONIST
```

### Guest Account
```
Username: guest
Password: guest123
Role: ROLE_USER
```

---

## 🔑 Key Features by Role

### Manager
- Create/Edit/Delete rooms
- Create/Edit/Delete staff
- View all bookings
- Edit/Delete bookings
- View all feedbacks
- Delete feedbacks

### Receptionist
- View/Create/Edit guests
- View all bookings
- Check-in guests
- Check-out guests
- View all feedbacks
- Edit feedbacks

### Guest
- View available rooms
- Create bookings
- View own bookings
- Cancel own bookings
- Submit feedback

---

## 📱 Frontend Pages

### Login Page
- Register new guest account
- Login with existing credentials
- Redirects to dashboard on success

### Dashboard
- Overview of system statistics
- Quick access to all modules

### Rooms Management
- View all rooms with status
- Create new rooms
- Edit room details
- Delete rooms
- Statistics: Total, Available, Booked, Maintenance

### Guests Management
- View all guests
- Create new guest profiles
- Edit guest information
- Delete guests
- Validation: Email, Phone (10 digits)

### Bookings Management
- View all bookings
- Create new bookings
- Edit booking details
- Delete bookings
- Check-in guests (Receptionist only)
- Check-out guests (Receptionist only)
- Cancel bookings (User/Manager)
- Pagination support

### Feedbacks Management
- View all feedbacks
- Submit new feedback
- Edit feedback
- Delete feedback (Manager only)
- Search by guest name
- Filter by rating
- Average rating calculation

### Staff Management
- View all staff members
- Create new staff
- Edit staff details
- Delete staff
- Role assignment (Receptionist/Manager)

---

## 🔌 API Gateway Routes

All requests go through API Gateway on port 8080:

```
/api/auth/**      → Auth Service (8081)
/api/rooms/**     → Rooms Service (8082)
/api/guests/**    → Guests Service (8083)
/api/bookings/**  → Bookings Service (8084)
/api/feedbacks/** → Form Service (8085)
```

---

## 🛠️ Common Tasks

### Create a New Room
1. Login as Manager
2. Go to Rooms Management
3. Click "Add Room"
4. Fill in: Room Number, Type, Price
5. Click "Create"

### Create a Booking
1. Login as Receptionist/Guest
2. Go to Bookings Management
3. Click "New Booking"
4. Select Guest and Room
5. Set Check-in/Check-out dates
6. Click "Create"

### Check-in a Guest
1. Login as Receptionist
2. Go to Bookings Management
3. Find booking with RESERVED status
4. Click Check-in button (green)
5. Status changes to CHECKED_IN

### Submit Feedback
1. Login as Guest
2. Go to Feedbacks
3. Click "Add Feedback"
4. Fill in all required fields
5. Set rating (1-5 stars)
6. Click "Submit"

---

## 🐛 Troubleshooting

### Services not starting
- Check if ports are already in use
- Ensure MySQL is running
- Check application.yaml for correct database credentials

### Frontend can't connect to backend
- Ensure API Gateway is running on port 8080
- Check CORS configuration in services
- Verify JWT token is stored in localStorage

### Booking creation fails
- Ensure guest exists
- Ensure room is available
- Check-out date must be after check-in date
- Verify no overlapping bookings

### Check-in fails
- Only Receptionist can check-in
- Booking must be in RESERVED status
- Check-in date must be today or later

---

## 📊 Database Schema

### Users Table (auth_db)
```
id, username, password, email, firstName, lastName, role
```

### Rooms Table (rooms_db)
```
id, roomNumber, type, price, status
```

### Guests Table (guests_db)
```
id, name, email, phone, idProofNumber, address, createdAt, updatedAt
```

### Bookings Table (bookings_db)
```
id, guestId, roomId, checkInDate, checkOutDate, numberOfAdults, 
numberOfChildren, totalAmount, status, bookingDate
```

### Guest Feedbacks Table (form_db)
```
feedbackId, guestName, guestEmail, roomNumber, rating, serviceQuality,
roomCleanliness, amenitiesFeedback, overallExperience, suggestions, createdAt
```

---

## 🔐 Security Notes

### JWT Token
- Stored in localStorage
- Sent in Authorization header: `Bearer <token>`
- Expires after 24 hours
- Secret: Configured in application.yaml

### Password Encoding
- Uses BCrypt
- Minimum 6 characters recommended

### Role-Based Access
- Enforced at controller level with @PreAuthorize
- Frontend also checks roles before showing UI elements

---

## 📈 Performance Tips

1. **Pagination** - Use pageNo and pageSize parameters
2. **Caching** - Consider Redis for frequently accessed data
3. **Indexing** - Database indexes on email, phone, roomNumber
4. **Connection Pooling** - Configured in application.yaml

---

## 🚀 Production Deployment

### Before Going Live
1. [ ] Move credentials to environment variables
2. [ ] Update CORS configuration for production domain
3. [ ] Enable HTTPS
4. [ ] Set up proper logging
5. [ ] Configure database backups
6. [ ] Set up monitoring and alerts
7. [ ] Load test the system
8. [ ] Security audit

### Environment Variables
```bash
DB_URL=jdbc:mysql://host:3306/db_name
DB_USERNAME=username
DB_PASSWORD=password
JWT_SECRET=your-secret-key
JWT_EXPIRATION=86400000
```

---

## 📞 Support

For issues or questions:
1. Check the logs in each service
2. Verify all services are running
3. Check database connectivity
4. Review API documentation in Swagger UI

Swagger UI available at:
- Auth Service: http://localhost:8081/swagger-ui.html
- Rooms Service: http://localhost:8082/swagger-ui.html
- Guests Service: http://localhost:8083/swagger-ui.html
- Bookings Service: http://localhost:8084/swagger-ui.html
- Form Service: http://localhost:8085/swagger-ui.html

---

**Happy Coding! 🎉**
