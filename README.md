
# Goal

This project, which reserves seats on a hypothetical baseball stadium (although
the sport it refers to doesn't really affect the reservation process), is split
up into multiple classes to make the convoluted process of reserving seats
simpler.

# Hierarchy

The project hierarchy goes as follows:
 - `StadiumSeats` (Main Class): Calls methods from other classes and provides some
   common methods (mostly for user input). It handles the menu and its basic
   tasks, which themselves call other functions.
 - `SeatSection` (Seat Holder): Holds seats in the different specified sections.
   It has two collections, one for reserved seats, and one for unreserved ones.
   Calls methods from the `Client` class!
 - `Client`: A single client with a name, email, and phone number. It holds
   references to the seats it owns, and a stack for undoing actions. It calls
   methods from the `Seat` class to reserve and un-reserve seats.
 - `Seat`: A single seat. Calls methods on its parent section, like
   `Section.updateWaitlist()`. It also modifies the parent section's taken and
   reserved lists!

# Terminology

Any time the comments talk about a "reservation", they refer to a seat that has
been taken by a client. Conversely, however, a seat that hasn't been taken
doesn't count as a "reservation" or as "reserved", as it has no owner.

Method comments follow guidelines to dictate what functions and methods they're 
referring to:
 - `Class.method()`: refers to a specific class' method
 - `method()` or `this.method()`: refers to a method in the current class
 - `method(arguments)`: provides information about the arguments a method takes

# Team

 - Abby Gotay Almonte
 - Andres Cruz Zapata
 - Anthony Garcia Oquendo
