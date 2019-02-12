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
SeatHold.java   
SeatReserved.java

Repositoy class:
----------------
SeatHoldRepository.java

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

seat.capacity=100  (capacity of the seating facility)

seat.holdtimedelay=4000 ( time delay for expire of hold)

seat.schedulefrequency=1000 ( scheduler frequency)


