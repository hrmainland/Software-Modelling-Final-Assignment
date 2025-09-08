# Game of Thrones Card Game - Software Modelling Final Assignment

[![Java](https://img.shields.io/badge/Java-ED8B00?style=flat-square&logo=java&logoColor=white)](https://www.oracle.com/java/)
[![Software Design](https://img.shields.io/badge/Software%20Design-Patterns-blue?style=flat-square)](https://en.wikipedia.org/wiki/Software_design_pattern)

## Overview

This repository contains the final assignment submission for the **Software Modelling and Design** course at the University of Melbourne. The project demonstrates advanced software engineering principles through the refactoring and enhancement of an existing Java-based Game of Thrones card game.

### Project Objectives

- **Refactor Legacy Code**: Transform an existing Java codebase to incorporate modern software design patterns
- **Enhance Maintainability**: Improve code structure through better separation of concerns and reduced coupling
- **Implement Design Patterns**: Apply industry-standard design patterns to solve common software architecture challenges
- **Demonstrate GRASP Principles**: Showcase understanding of General Responsibility Assignment Software Patterns

## Features

- **Complete Card Game Implementation**: Fully functional Game of Thrones card game with enhanced gameplay mechanics
- **Advanced Design Patterns**: Implementation of Observer, Strategy, Factory, and Singleton patterns
- **Improved Architecture**: Enhanced code organization following SOLID principles and GRASP guidelines
- **Extensible Design**: Modular structure allowing for easy addition of new features and game mechanics

## Architecture & Design Patterns

### Design Pattern Implementation

This project implements several key design patterns to improve code quality and maintainability:

#### Observer Pattern
- **Purpose**: Facilitates communication between the `GameOfThrones` core engine and player objects
- **Benefits**: Reduces coupling and enables dynamic event-driven interactions
- **Implementation**: Players observe game state changes and react accordingly

#### Strategy Pattern  
- **Purpose**: Provides flexible player behavior implementations
- **Benefits**: Allows runtime switching of player strategies without code modification
- **Implementation**: Different AI strategies can be plugged in dynamically

#### Factory Pattern
- **Purpose**: Abstracts player object instantiation details
- **Benefits**: Centralizes object creation logic and improves code maintainability
- **Implementation**: Concrete factory handles the creation of different player types

#### Singleton Pattern
- **Purpose**: Ensures single instance of `PileCalculator` object
- **Benefits**: Provides global access point while preventing multiple instances
- **Implementation**: Thread-safe singleton implementation for game calculations

### GRASP Principles Applied

- **High Cohesion**: Related functionalities grouped together within appropriate classes
- **Low Coupling**: Minimal dependencies between classes through interface-based design
- **Protected Variation**: Stable interfaces protect against implementation changes
- **Pure Fabrication**: `PileCalculator` serves as a behavioral class not representing domain concepts
- **Polymorphism**: Extensive use of inheritance and interface implementation

## Getting Started

### Prerequisites

- **Java Development Kit (JDK)** 8 or higher
- **IDE** with Java support (IntelliJ IDEA, Eclipse, or VS Code)
- **Git** for version control

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/hrmainland/Software-Modelling-Final-Assignment.git
   cd Software-Modelling-Final-Assignment
   ```

2. **Compile the project**
   ```bash
   javac -d bin src/**/*.java
   ```

3. **Run the game**
   ```bash
   java -cp bin GameOfThrones
   ```

### Usage

1. Launch the application using the run command above
2. Follow the on-screen prompts to configure game settings
3. Enjoy playing the enhanced Game of Thrones card game!

## Project Structure

```
├── src/                    # Source code directory
│   ├── game/              # Core game logic
│   ├── players/           # Player implementations
│   ├── patterns/          # Design pattern implementations
│   └── utils/             # Utility classes
├── docs/                  # Documentation files
├── tests/                 # Unit tests
└── README.md             # Project documentation
```

## Contributing

This is an academic project submission. While the main development phase is complete, feedback and suggestions are welcome for educational purposes.

### Development Guidelines

- Follow Java naming conventions
- Maintain existing design pattern implementations
- Add comprehensive unit tests for new features
- Update documentation for any architectural changes

## Academic Context

**Course**: Software Modelling and Design  
**Institution**: University of Melbourne  
**Assignment Type**: Group Project - Final Assignment  
**Focus Areas**: Design Patterns, GRASP Principles, Code Refactoring

## Project Evolution

The original Java codebase has been significantly enhanced through:

- **Structural Improvements**: Better class organization and responsibility distribution
- **Pattern Integration**: Strategic implementation of proven design patterns
- **Code Quality**: Enhanced readability, maintainability, and extensibility
- **Performance Optimization**: Efficient algorithms and reduced computational complexity

## License

This project is part of an academic assignment and is intended for educational purposes.

---

*This project demonstrates practical application of software engineering principles and serves as a comprehensive example of design pattern implementation in Java.*
