# Dungeon Escape - 2D Arcade Game
## Technology Stack

- **Language:** Java  
- **GUI Framework:** Swing (planned)  
- **Modeling Tools:** PlantUML / UML diagramming tools  
- **Version Control:** Git + GitHub  
- **Development Environment:** WSL + VS Code / IntelliJ  

## Overview

**Dungeon Escape** is a desktop-based 2D arcade game built in Java.  
The player controls an adventurer navigating a dungeon maze, collecting keys and optional bonus rewards while avoiding enemies and traps.  

The game is designed around a tick-based movement system, object-oriented architecture, and modular rendering pipeline.

This project emphasizes:
- Requirements engineering
- System design using UML
- Object-oriented design principles
- Collaborative development using Git workflows
- Clean architectural separation between game logic and rendering

---

## Gameplay Concept

The player starts at a designated entry point on a grid-based dungeon board.

### Objective
- Collect all required keys (regular rewards)
- Reach the exit cell
- Maintain a non-negative score

### Lose Conditions
- Colliding with a moving enemy
- Score dropping below zero due to punishments

---

## Core Mechanics

### Tick-Based Game Loop
- Each valid player movement advances the game by one tick.
- After each tick, all enemies update their movement.
- Movement is limited to one cell per tick.

### Enemy Types
- **Goblins**: Chase the player by recalculating the shortest approach each tick.
- **Ogres**: Patrol predefined routes across the dungeon.

### Rewards
- **Keys (Required)**: Must be collected to unlock the exit.
- **Chests (Bonus)**: Appear temporarily and increase score if collected in time.

### Punishments
- **Spike traps**: Deduct score upon contact and disappear after activation.

---

## Architecture & Design

The system follows a layered object-oriented architecture:

### Core Components
- `Game` – Controls game state, tick updates, win/loss checks
- `Board` – Loads map layout and manages grid state
- `Cell` hierarchy – Represents walkable and non-walkable tiles
- `Character` abstraction – Base class for Player and Enemies
- `Reward` & `Punishment` abstractions – Encapsulate scoring logic
- `DrawQueue` – Manages layered rendering order
- `GamePanel` – Handles graphical output

Design documentation and UML diagrams can be found in `/docs`.

---

## Members:  
MacKinnon, James, jam48@sfu.ca  
Matsumoto, Yui, yma99@sfu.ca  
Ng, Auston, acn8@sfu.ca  
Pakravani, Rammy, rpa89@sfu.ca  
Sandilands, Xavier,	njs12@sfu.ca
