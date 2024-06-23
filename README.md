# __Repair Agency__

__Repair Agency__ is my implementation of EPAM course project. This project is aimed at facilitating communication between managers, customers and executors of repair work, as well as at convenient management of user profiles and requests from customers.

## Built with:
- Spring Boot
  - Data JPA
  - Security
  - Web MVC
  - Docker Compose
  - Validation
  - Devtools
  - Tests
- Thymeleaf, HTML, CSS, Bootstrap
- H2, MySQL
- Junit, Mockito
- Maven
- Lombok

## Application functionality:
Users have roles such as *client*, *manager*, *repairer*, *admin* and *super-admin*.

- **Client** can create multiple requests, edit them or delete them, pay, leave feedback and replenish the account balance.
- **Manger** can assign a repairer responsible for request, assign request cost and payment status, read feedback and delete requests.
- **Repairer** can change completion status of requests and read feedbacks.
- **Admin** can create, edit and delete users.
- **Super-admin** has the same abilities as an administrator, but cannot be deleted or edited by other users with administration roles.

Each user has the ability to edit profile information.

## Usage:

### 1. Non-authenticated user
Non-authenticated user has access to pages *About*, *Contacts* and *Login/Sign-up*

**1.1. About project page** contains brief description and link to this GitHub repository.

![about project](https://drive.google.com/uc?export=view&id=1EbiA_WhjRFJvmk_gRcLEX42pMz0y3YiM)

**1.2. Contacts page** contains all contacts for convenience to get in touch.

![contacts](https://drive.google.com/uc?export=view&id=1y7N3Cd3dCk74NkAYjhzlRZ0G7xZfPLyk)

**1.3. Login page** allows a registered user to authenticate.

![login](https://drive.google.com/uc?export=view&id=16AcoD1xOgkFgCzbvHpLZUhOQg7e33Qa-)

**1.4. Registration page** asks user to enter their contact information. Each field is validated for correctness of the entered data.  First and last name must be no shorter than two Latin characters, email must be in the appropriate format and not be already registered for another user (same as phone number), phone number must contain nine digits, password must be no shorter and no longer than 100 characters.

![registration](https://drive.google.com/uc?export=view&id=1YGDDrxXRfz3q0LMtC0KVxsRSSN-NJ_rs)

**1.5. Standard logout page**

![logout](https://drive.google.com/uc?export=view&id=1WIJH-T6ItVr4R5FV7VPWUdE8keQt9VYp)

### 2. Authenticated user
All authenticated users has access to profile edit page. Other pages may only be available for a specific user role or multiple roles. If an unauthorized user tries to access a page that is inaccessible to them, they will get an error page.

**2.1. Profile edit page** can be accessed by clicking on users name on a header. If a user tries to enter an email address or phone number that is already used by another user, they will receive an error message, same as wih registration. User must enter an existing or new password to confirm changes.

![profile-update](https://drive.google.com/uc?export=view&id=1WoPut31DH6CCXmCpFdWExWSLKBvHNoSC)

**2.2. Generic error page** is displayed for each error that may occur on server or user side.

![generic 404](https://drive.google.com/uc?export=view&id=1Gto8D48DOLWa2FxIuYv8MqWWx62WwnwO)

### 3. Client
User with client role has access to balance replenishment, user info, repair request and feedback pages.

**3.1. Balance page** allows client to replenish their balance.

![balance-replenishment](https://drive.google.com/uc?export=view&id=1F94bJ3IhUXMeHnhag8hetUihrcRN54ZC)

**3.2. Requests page**  allows client to manage their requests and check their status.
- After creating a request, the client waits until a repairer is assigned to the request and the price is determined. After that they can pay for the request and the funds will be debited from their account.
- In case of insufficient funds on the account, the client will be offered to top up the balance and will be transferred to the balance page.
- As long as the request has status *"Not started"*, the client has possibility to delete or edit it.
- If the client edits an already paid request, the spent funds will be returned to the client's balance and the request price will require revaluation.
- If the client deletes a paid request, the funds are also returned to his balance.
- The client can leave and read feedback on requests with the status *"Completed"*.

![client requests](https://drive.google.com/uc?export=view&id=1KcPTSlndztY8u7MNi8HY7vQlH3_riz3f)

**3.3. Request creation page** allows user to describe problem that needs to be solved. Description of request should be not less than 10 and not more than 10000 symbols.

![request form](https://drive.google.com/uc?export=view&id=1h6s3awwVu70LHWBDKla-6_hLG6PRDQF9)

**3.4. Request edit page** contains previously written description.

![request edit](https://drive.google.com/uc?export=view&id=1vmmGUaLoc_XkQXyJ7M6jdeqh1yyQ-ORJ)

**3.5. Feedback creation page** allows client to leave their feedback for completed requests and assess quality of work done. Feedback text should be not less than 5 and not more than 10000 symbols.

![feedback form](https://drive.google.com/uc?export=view&id=1IA2--nKFh9GVV6eeeanZ57SRyUr2HVmq)

### 4. Manager
User with manager role has access to user info, feedback and request pages.

**Requests page** allows manager to view and interact with requests created by clients.
- After a clients creates a request, the manager assigns a repairman to it and determines the cost of the work.
- The request can be deleted if it has the status *"Not started"*. The spent funds will be returned to the client if the request has already been paid.
- As long as the request does not have the status *"Completed"*, its price or the responsible repairer can be changed.
- When changing the price:
  - if the request has been paid and the new price is HIGHER than the previous one, its payment status changes to "Awaiting payment" and the customer will have to pay additional funds.
  - if the request has been paid and the new price is LOWER than the previous one, the client will be refunded the difference.
- The payment status of orders is changed automatically, but can also be changed manually by the manager.
- Manager can also view feedbacks left by client.

![manager requests](https://drive.google.com/uc?export=view&id=1W8BwFL26kyrfXghKwCo-9MZ4cVef7SCI)

### 5. Repairer
User with repairer role has access to user info, feedback and request pages, same as manager.

**5.1. Requests page** allows repairer to view and interact with requests assigned to them by manager.
- Repairer is able to change completion status for paid requests.
- Request completion status cannot be changed once it set as *"Completed"*.
- Repairer can also view feedbacks left by client.

![repairer requests](https://drive.google.com/uc?export=view&id=1PiEvEqW05RNtjk33-4-ewfeZVVZIW2U9)

**5.2. Feedback info** page allows repairer to read feedback left by client. This page has same view for client and manager.

![feedback info](https://drive.google.com/uc?export=view&id=1x3jn7-XiNRYkRAEU6pW07IIQTk95t0PY)

### 6. Admin
User with admin role has access to users creation and management pages.

**6.1. Users page** allow admin to create and manage existing users.
- User with role *"Super Admin"* cannot be deleted or edited by other admins.
- Admin cannot delete or edit their profile from this page.
- Administrator can view information about users of any role, while clients can view information about repairers assigned to their requests, and manager or repairer can view information about the client who created the request.

![admin users list](https://drive.google.com/uc?export=view&id=1aW-UHi3diguh7p6r6m1cfb2Oi9cEI4wq)

**6.2. User creation page** allows admin to enter the required data for the new user and select the required role. All fields entered are validated in the same way as during user registration.

![admin create user form](https://drive.google.com/uc?export=view&id=12-rckpAAL_FrHd8w1AcbNSdgT5Xknupd)

**6.3. User editing page** allows admin to edit existing user profile, except their password.

![admin edit user form](https://drive.google.com/uc?export=view&id=1Q4dRvC7csFNZPRXyWdGBRJGR1mpi1hRT)

**6.4. Information about user** page allows:
- administrator to view profile info of any registered user.
- manager to view any client's info who created the request.
- repairer to view profile info of clients who created requests assigned to them.

**6.5. Example of client's information page:**  
This page will have same view for users with admin and manager roles, except for the change to the text title.

![info client](https://drive.google.com/uc?export=view&id=1g208Were9wRu9TqIvDsewHyXqqjB7FXs)

**6.6. Example of repairer's information page:**  
This page also contains average rating based on clients feedbacks.

![info repairer](https://drive.google.com/uc?export=view&id=1-ykr4hx2gEawSCsQQtFCDWP3RHvp8_wf)

## Application configuration

**1. Docker compose:**  
Application database is running with docker and configured with docker compose. Before running the application you should configure your own database environment.

![docker](https://drive.google.com/uc?export=view&id=15ZfzRz3MGA2B-H6xNBHOoISLnicGy00g)

**2. Application properties file** has configuration for:
- docker-compose to automatically start and stop db container with the application.
- flyway migration schema.
- jpa properties
- generic error page view
- super-admin user creation properties which you should change to your own before running the application. Application has class responsible for creation of super-admin user during every app start, if it not exists.
- possible db connection configuration without docker-compose.

![application properties](https://drive.google.com/uc?export=view&id=1SCaXovdp6wSDpP1MNdIjgUvkKyawv24X)