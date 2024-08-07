# Software Modelling Final Assignment

## The Concept
This is the repo containing my group project submission for the final assignment of the subject 'Software Modelling and Design' at the University of Melbourne.
The task was to take an existing code base written in Java and update it such that new functionality is added and the code is refactored to include an number of software patterns.
The codebase is that of a card game named Game of Thrones.
This repo contains the updated codebase with all improvements.

## The Implementation
Some of the main improvements made to the design of the code were the increases in the 'GRASP' principles of cohesion and protected variation. This was achieved by decreasing the coupling and increasing the use of polymorphism.
Specifically, an observer pattern was used to communicate between 'GameOfThrones' and the player objects.
Additionally a strategy pattern was implemented as was a concrete factory pattern, to abstract the details of instantiating player objects.
We also implemented the concept of pure fabrication via the 'PileCalculator' object, and given that there could only ever be one of these objects we built it using the singleton pattern.
