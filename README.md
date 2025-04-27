# Emergency STOMP Messaging System 🚨

A full-stack emergency services platform built with Java and C++.  
Supports real-time event reporting, subscription to emergency channels, and client-server communication over the **STOMP 1.2 protocol**.

## 📚 Project Overview

This project implements a complete emergency services system:
- **Server** (Java): Handles client connections, manages topics and subscriptions, supports both **Thread-Per-Client** and **Reactor** concurrency models.
- **Client** (C++): Allows users to log in, subscribe to channels (e.g., fire department, police), report emergencies, and receive real-time updates.

All communication follows the **STOMP 1.2** protocol for reliable, standardized messaging.

## 🚀 Technologies Used
- Java 17
- C++ 14
- STOMP 1.2 Protocol
- Networking (TCP Sockets)
- Multithreading (C++ `std::thread`, Java Threads)
- Reactor Design Pattern
- Maven (server build tool)
- Makefile (client build tool)

## 🛠️ How to Build and Run

### Server (Java)

1. Navigate to the `server/` directory.
2. Compile using Maven:
   ```bash
   mvn compile
   ```
3. Run the server (choose port and mode):
   ```bash
   mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.stomp.StompServer" -Dexec.args="<port> <tpc/reactor>"
   ```
  Example:
  ```bash
  mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.stomp.StompServer" -Dexec.args="7777 tpc"
  ```

### Client (C++)

1. Navigate to the `client/` directory.
2. Build using the Makefile:
   ```bash
   make
   ```
3. Run the server (choose port and mode):
   ```bash
   ./bin/StompESClient
   ```

## 🧠 Key Features
- Full STOMP 1.2 frame parsing and handling
- Client-side multithreading (socket reading and keyboard input simultaneously)
- Subscription to multiple emergency channels
- Reporting emergency events from JSON files
- Summary report generation for events
- Graceful connection closing (DISCONNECT + RECEIPT)

## ✨ Skills Demonstrated
- Full-Stack Network Programming
- Java and C++ Interoperability
- Asynchronous I/O handling
- Event-driven programming (Reactor pattern)
- Advanced concurrency (multi-threaded C++ clients)
- Real-world protocol implementation (STOMP)
