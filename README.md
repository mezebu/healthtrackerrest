# Health Tracker REST

This project is a RESTful API designed to manage health-related information, including user details, activities, fitness goals, and associated images. It utilizes Javalin, a lightweight web framework for Kotlin.

## Features

- **User Management**: CRUD operations for users, including retrieval by ID or email, addition, deletion, and updating user details.
- **Activity Tracking**: Functions to handle activities, such as retrieving all activities, adding, deleting, or updating individual activities.
- **Fitness Goals**: Management of fitness goals, enabling users to create, delete, update, and retrieve fitness goals.
- **Image Information**: Handling images associated with health tracking, with support for adding, deleting, updating, and retrieving image details.

## Configuration

### Database Configuration

The application connects to a PostgreSQL database. Modify the `DbConfig` class in `ie.setu.config` to set your database credentials:

```kotlin
// Modify these variables with your PostgreSQL database details
val pgHOST = "flora.db.elephantsql.com"
val pgPORT = "5432"
val pgUSER = "your_username"
val pgPASSWORD = "your_password"
val pgDATABASE = "your_database_name"
```

### Javalin Configuration

The `JavalinConfig` class in `ie.setu.config` handles the setup of the Javalin web server. The port can be configured through the environment variable `PORT` or defaults to port 7000 if not set.

## Endpoints

### Users

- `/api/users`
  - `GET`: Retrieve all users
  - `POST`: Add a new user
- `/api/users/{user-id}`
  - `GET`: Retrieve user by ID
  - `DELETE`: Delete user by ID
  - `PATCH`: Update user details
- `/api/users/email/{email}`
  - `GET`: Retrieve user by email

### Activities

- `/api/activities`
  - `GET`: Retrieve all activities
  - `POST`: Add a new activity
- `/api/activities/{activity-id}`
  - `GET`: Retrieve activity by ID
  - `DELETE`: Delete activity by ID
  - `PATCH`: Update activity details

### Fitness Goals

- `/api/goals`
  - `GET`: Retrieve all fitness goals
  - `POST`: Add a new fitness goal
- `/api/goals/{goal-id}`
  - `GET`: Retrieve fitness goal by ID
  - `DELETE`: Delete fitness goal by ID
  - `PATCH`: Update fitness goal details

### Image Information

- `/api/images`
  - `GET`: Retrieve all image information
  - `POST`: Add new image information
- `/api/images/{image-id}`
  - `GET`: Retrieve image information by ID
  - `DELETE`: Delete image information by ID
  - `PATCH`: Update image information details

### Vue Components

Additionally, the application provides Vue components for various views:

- `/`: Home page
- `/users`: User overview
- `/users/{user-id}`: User profile
- `/users/{user-id}/activities`: User activity overview
- `/users/{user-id}/goals`: User goal overview
- `/activities`: Activity overview
- `/activities/{activity-id}`: Activity profile
- `/goals`: Goal overview
- `/goals/{goal-id}`: Goal profile

### Additional Controllers

#### ActivityController

The `ActivityController` manages operations related to activities:

- `getAllActivities`: Retrieve all activities.
- `getActivitiesByUserId`: Get activities by user ID.
- `getActivitiesByActivityId`: Get activity by activity ID.
- `addActivity`: Add a new activity.
- `deleteActivityByActivityId`: Delete activity by activity ID.
- `deleteActivityByUserId`: Delete activities by user ID.
- `updateActivity`: Update activity details.

#### FitnessGoalController

The `FitnessGoalController` handles operations associated with fitness goals:

- `getAllFitnessGoals`: Retrieve all fitness goals.
- `getFitnessGoalsByUserId`: Get fitness goals by user ID.
- `getFitnessGoalsByFitnessGoalId`: Get fitness goals by fitness goal ID.
- `addFitnessGoal`: Add a new fitness goal.
- `deleteFitnessGoalByFitnessGoalId`: Delete fitness goal by fitness goal ID.
- `deleteFitnessGoalByUserId`: Delete fitness goals by user ID.
- `updateFitnessGoal`: Update fitness goal details.

### Database Structure

The application uses the following database tables:

#### Users

- `id`: User ID (Auto-incremented primary key)
- `name`: User's name
- `email`: User's email

#### Activities

- `id`: Activity ID (Auto-incremented primary key)
- `description`: Description of the activity
- `duration`: Duration of the activity
- `calories`: Calories burned
- `started`: Timestamp of when the activity started
- `userId`: References `Users.id` (On delete, cascade)

#### FitnessGoals

- `id`: Fitness goal ID (Auto-incremented primary key)
- `goalType`: Type of fitness goal
- `target`: Target for the goal
- `initialUserStatus`: Initial user status for the goal
- `duration`: Duration of the goal
- `startDate`: Start date of the goal
- `endDate`: End date of the goal
- `userId`: References `Users.id` (On delete, cascade)

### Data Access Objects (DAO)

#### ActivityDAO

The `ActivityDAO` class in `ie.setu.domain.repository` handles database operations related to activities:

```kotlin
package ie.setu.domain.repository

class ActivityDAO {
    // ... (methods for fetching, adding, updating, and deleting activities)
}
```

#### FitnessGoalDAO

The `FitnessGoalDAO` class in `ie.setu.domain.repository` manages database interactions concerning fitness goals:

```kotlin
package ie.setu.domain.repository

class FitnessGoalDAO {
    // ... (methods for fetching, adding, updating, and deleting fitness goals)
}
```

#### UserDAO

The `UserDAO` class in `ie.setu.domain.repository` is responsible for handling user-related database operations:

```kotlin
package ie.setu.domain.repository

class UserDAO {
    // ... (methods for fetching, adding, updating, and deleting users)
}
```

## Conclusion

The Health Tracker REST project offers a robust RESTful API for managing health-related data, including user details, activities, fitness goals, and image information. Leveraging Javalin, a lightweight web framework for Kotlin, this API provides a streamlined approach to interact with and manage health-related information.

### Key Features

- **User Management**: Comprehensive functionality to manage user profiles, including retrieval, creation, updating, and deletion.
- **Activity Tracking**: Efficient tracking and management of various activities, such as duration, calories burned, and activity descriptions.
- **Fitness Goals**: Capability to set, update, and track fitness goals, including target types, durations, and progress tracking.
- **Image Information**: Ability to manage image-related details, such as descriptions, paths, and upload timestamps.

### Data Access Objects (DAO)

The project incorporates dedicated Data Access Objects (DAO) for distinct entities—Activities, Fitness Goals, and Users. These DAO classes centralize database interactions, offering methods for seamless operations like fetching, adding, updating, and deleting records for each entity.

### Get Started

To explore and utilize this Health Tracker REST API, follow the setup instructions detailed in the project's documentation. The provided DAO classes streamline database interactions and serve as a foundation for managing health-related data effectively.

This API stands as a robust solution for health management applications, providing a structured and accessible backend for diverse health-oriented services and applications.

