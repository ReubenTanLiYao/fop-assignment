PROJECT TITLE

WIX1002 Fundamentals of Programming - Project 2: Calendar and Schedular App


TEAM MEMBERS
1. Lee Xian Zheng (23121598)
2. Reuben Tan Li Yao (23120854)
3. Lin Shao Jie (24040026)
4. Johan Yeong (22118264)
5. Liu Yu Hang (24040140)
6. Arieth Thaqif bin Arnizzam (25076382)


PROJECT OVERVIEW

Effective time management is essential for students and working individuals to organize their daily activities and responsibilities efficiently. With multiple tasks, meetings, and deadlines to manage, having a clear and structured way to track events is important to avoid conflicts and missed commitments. Calendar and scheduling applications provide a practical solution by allowing users to view, plan, and manage their time in an organized manner.

The main goal of this project is to develop a simple Calendar and Scheduler Application for personal use using the Java programming language. The application allows users to create, update, delete, and view events in different formats such as list views and calendar views. It also supports recurring events, enabling users to schedule repeating activities within a defined period.

To reinforce fundamental programming concepts, the application stores all event data using local CSV files instead of a database. This project emphasizes logical program flow, object-oriented programming principles, and file input/output handling. Overall, the application aims to provide a lightweight yet functional scheduling tool while strengthening the team’s understanding of core Java programming concepts.


FEATURES IMPLEMENTED

• Basic Features:
Event Creation (completed)
Event Update & Delete (completed)
Recurring Events (completed)
Backup and Restore (completed)
View Calendar (completed)
Event Basic Search (completed)

• Extra Features:

Event Advanced Search & Filter (completed)

The Event Advanced Search feature allows users to search for events using more detailed criteria beyond basic date-based searching. Users can filter events by event title keywords, date ranges, event duration,event location, event type and event attendees. This makes it easier to locate specific events, especially when the number of scheduled events increases.
This feature merits extra marks because it demonstrates the ability to process and filter stored data dynamically using multiple conditions. It also shows a deeper understanding of file reading, string processing, and logical comparisons, as well as improving overall usability of the application.

Event Statistics (completed)

The Event Statistics feature provides useful insights based on the user’s scheduled events. Examples of statistics implemented include the total scheduled time, average event duration, and analysis of event durations. These statistics help users understand how their time is being allocated and identify patterns in their schedule.
This feature deserves extra marks because it goes beyond basic CRUD operations and involves data aggregation, time calculations, and analytical thinking. It demonstrates the ability to extract meaningful information from raw event data and apply programming logic to produce useful summaries.

Additional Event Fields (completed)

The Additional Event Fields feature extends the basic event information by allowing extra details such as location, category, or notes to be stored separately in another CSV file. These additional fields are linked to events using the event ID and can be searched and included during backup and restore operations.
This feature merits extra marks as it shows extensibility in system design. By separating additional fields into a different file, the system remains modular and scalable. It also demonstrates careful data organization, file handling, and integration between multiple data sources.

Conflict Detection (completed)

The Conflict Detection feature checks whether a newly created event overlaps in time with existing events. If a scheduling conflict is detected, the system displays the events, notifying the user.This allows the user to spot conflicting events and make necessary changes.
This feature deserves extra marks because it requires comparing time ranges and handling logical edge cases. It improves the reliability of the scheduler and reflects real-world calendar behavior. Implementing conflict detection shows an understanding of time-based logic and practical application design.


HOW TO COMPILE AND RUN

1.RUNNING ON Terminal
- Download the Calendar Folder
- Go to Calendar Folder
- Run the RunMenu.bat file

2.RUNNING ON NetBeans
- Download the FOP_Assignment folder
- Open the project in netbeans
- Locate the source codes in FOP_Assignment -> Source Packages -> com.test.calendar
- Run the project

3.RUNNING ON Visual Studio Code(VSC)
- Download the FOP_Assignment folder
- Open the folder in VSC
- Locate the source codes in FOP_Assignment\src\main\java\com\test\calendar
- Run the project


SCREENSHOTS



CONTRIBUTION

1. Event Creation (Lee Xian Zheng)
2. Event Update & Delete (Johan Yeong)
3. Recurring Events (Reuben Tan Li Yao)
4. Backup and Restore（Liu Yu Hang）
5. View Calendar (Lee Xian Zheng)
6. Event Basic Search（Lin Shao Jie）
7. Event Advanced Search & Filter (Reuben Tan Li Yao)
8. Event Statistics (Reuben Tan Li Yao)
9. Additional Event Fields (Arieth Thaqif bin Arnizzam)
10. Conflict Detection (Arieth Thaqif bin Arnizzam)

CHALLENGES FACED

One of the main challenges faced during the development of this project was managing recurring events using CSV files instead of a database. Since recurring events are stored separately from base events, additional logic was required to generate repeated occurrences dynamically and merge them with normal events during searching and viewing. This was resolved by generating recurring events in memory and treating them as regular events during processing, which simplified the overall design.

Another challenge faced was the portability of the program. Hardcoding the file path initially caused errors when reading and writing CSV files when used on another system. The issue was tackled by implementiong
a FolderCreation and FileCreation class that creates the directory "data" which stores the csv files needed for the program to run, thus standardizing the file paths. Furthermore, by using relative paths and File.separatorm, the program to run consistently across different systems.
 
