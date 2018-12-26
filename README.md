The is a chat application built upon this [repository](https://github.com/abhirockzz/websocket-chat). 

There are two main parts in this repository:

- **The server-side**: build in _Java_, using [Tyrus](https://tyrus.java.net) implementation for the core logic and [Grizzly](https://grizzly.java.net/)
- **The client-side**: Chat client, build using _HTML_ and _JavaScript_.

## Features

Here is what you can do with the chat application

- Join the chat room and choose a username
- Send messages
- See the list of connected users
- Messages are written to a MySQL DataBase. At Chat startup, past messages are loaded.

## Try it out

### Configure the MySQL DB

Messages in the Chat are written to a MySQL DB. You should first [install MySQL server](https://dev.mysql.com/downloads/installer/).

Once done, start the server and run the following script to create the necesary tables:

```sql
CREATE DATABASE chat;
CREATE TABLE IF NOT EXISTS users (
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    PRIMARY KEY (name)
);
CREATE TABLE IF NOT EXISTS messages (
    message_id INT AUTO_INCREMENT,
    user_name VARCHAR(255),
    content TEXT,
    dt DATETIME,
    PRIMARY KEY (message_id)
);
```

### Build & run the server

The server application is a Maven project

- To build, just execute `mvn clean install` which will produce an independent (fat/uber) JAR
- Run it using `java -jar target/websocket-chat.jar`

### Launch the client

Open the file `clientSide/index.html` in your favorite browser.