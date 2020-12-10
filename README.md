# Distributed excercise Programming 3 - KdG College Antwerp

This excercise is a small example of distributed systems where there is a client and server setup with communication.

In this project you fill in a document with some text and do some general modifications on it like toUpper, toLower, append,... .
This project is split up into 2 projects, a client side and a server side, as per request.

## The project

We received a base project that was not distributed and we had to build around the core logic to make the project distributed.
Therefore I split the project in a client side and a server side.
To communicate with eachother we received a communication package which we could use freely, in addition, and what this excercise is all about,
we had to create stubs and skeletons to handle the communication between server and client and to make sure the core logic works by sending and receiving requests.

## Problems I faced

The first problem was getting my head around the whole project and how to get started. I solved this by reading/listening to the given recordings and powerpoints that explain the theory behind this project. I also made some simple domain models on how everything should be connected and how the flow of the project will go.

The second problem I faced was the communication. When and where to send / receive an acknowledge message ( empty reply ) so the code runs smoothly. I solved this by discussing it with fellow students and once i had a general idea of how this should be, I started coding/ testing ( trail & error ).

The third problem I faced was the Call by Reference implmentation we needed to provide. Again i solved this by discussing it with fellow students but i mostly came to a general idea by looking up what Call by reference means and how it works in a java project, and most of all, in this project's scenario.

The fourth and last problem I faced where some mistakes on the acknowledge messages. I made the mistake of checking for and sending an ack message before actually sending the requested data which resulted in an infinite loop that checks for an ack message. This problem was solved by just analyzing my code and debugging a whole lot. I debugged the 2 projects at once to see where my mistakes were made and to eventually correct them when they were found.

PS: to make sure the 'discussing the project with fellow students' isn't mistaken for 'copying code of one another' i want to make clear that i have written this project myself.
