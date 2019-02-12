READ ME FOR TICKET SERVICE
=================================================================
To compile:
--------------
mvn clean install 


To run:
---------
mvn clean spring-boot:run



Domain classes:
--------------------
Seat.java
SeatHold.java   
SeatReserved.java
SeatStatus.java
Venue.java

Repositoy class:
----------------
SeatHoldRepository.java

NOTE:
-----
We are using simple Map data structure to hold the SeatHolds
and SeatReserved information along with a simple nested list
to store the seat information.
The seat holds are removed based on the hold end time information
from our database.

Whenever there is a request to hold, or reserve or expire the holds,
we update the venue table accordingly.
 

Service interface:
--------------------
TicketService.java

TicketServiceImpl.java

Command line runner Client 
--------------------------
TicketServiceClient.java

Spring boot application:
-----------------------
TicketServiceApplication.class

Resources:
---------------
application.properties

Following are the properties that can be customized

venue.rows=10  (Number of rows in the seating facility)
venue.columns=10  (Number of seats in each row in the seating facility)

seat.holdtimedelay=4000 ( time delay for expire of hold)


Assumptions made:
-----------------
The service will not throw any errors if there are no seats available to hold


Future improvements:
----------------------
1. A REST or MVC controller may be used to expose the service
to external consumers
2. Exception handling can be done to provide user friendly error message
3. Hold cancellation and Reservation cancellation can be provided
4. Instead of java data structures the data structures can be replaced with 
NoSQL key value data stores or SQL tables to persist the data.

