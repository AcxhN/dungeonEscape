# Meeting 02 – More Architecture Discussion & Feature Delegation

**Date:** February 16, 2026  
**Platform:** Discord (Weekly Monday 2-3PM Meeting)  
**Objective:** Finalize technical architecture and assign Phase 2, milestone 1 implementation responsibilities  
(Phase 2 will be the coding portion of this project. It will be split into two milestones. Milestone 1 will include the implementation of the core aspects of the game, we will be able to run an ugly barebones version of the game perse. Milestone 2 will flesh everything out)

---

## 1. Engineering Decisions

### Build System
- A team member will initialize **Maven** for dependency and build management.
- All future development will follow Maven project structure.

---

### Game Loop Clarification

**Definition of a Tick:**
- One tick = one valid player movement.
- After each tick:
  - All enemies update their position.
  - Collision checks are performed.
  - Win/loss conditions are evaluated.

This formalizes the tick-based movement system discussed in Phase 1.

---

### Map System Design

- Maps will likely be represented using ASCII characters.
- Each character represents a tile type (wall, floor, reward, enemy, etc.).
- Board loads the map file during initialization.
- Grid stored as a 2D array (`Cell[][]`).

---

### Interaction & Collision Strategy

- Movement validated through walkability checks.
- Collision detection evaluated per tick.
- Object interactions handled through:
  - Grid lookup
  - Type-based behavior (Reward, Punishment, Enemy)

UML updated to reflect:
- Array-based grid storage
- Clear separation of responsibilities
- Tick-based interaction model

---

### Communication Protocol

If any implementation deviates from:
- The agreed UML
- Or team discussion decisions  

→ Post a message in Discord explaining the reasoning before merging.

Transparency required for architectural consistency.

---

## 2. Feature Ownership & Branch Strategy

All work will be completed in **feature branches**.  
All merges must go through **Pull Requests**.

---

### Member 1 – Core Game Engine  
**Xavier**  
Branch: `feature/gameEngine`

Responsibilities:
- `Game` class implementation
- Game loop integration
- `updateTick()` logic
- `handleInput()` system
- Win/loss detection
- Respawn behavior after death

Primary Focus:
- State management
- Tick execution flow
- System coordination

---

### Member 2 – Board & Map System  
**Auston**  
Branch: `feature/boardMap`

Responsibilities:
- `Board` class
- `loadMap()` implementation
- `Cell` hierarchy
- Walkability logic
- `Position` handling

Primary Focus:
- Grid architecture
- Map parsing
- Static world structure

---

### Member 3 – Player, Rewards & Punishments  
**Rammy**  
Branch: `feature/playerReward`

Responsibilities:
- Player movement logic
- `collectReward()` method
- `applyPunishment()` method
- Regular reward behavior
- Bonus reward behavior
- Chest duration ticking system

Primary Focus:
- Score system
- Reward lifecycle management
- Player interactions

---

### Member 4 – Enemy System  
**James**  
Branch: `feature/enemies`

Responsibilities:
- Abstract `Enemy` class
- `updateMovement()` implementation
- Goblin chase logic
- Ogre patrol logic

Milestones:
- **Milestone 1:** Ogre (patrol-based logic)
- **Milestone 2:** Goblin (chase-based logic)

Primary Focus:
- Movement AI
- Enemy-player interaction
- Collision outcomes

---

### Member 5 – Rendering & UI  
**Yui**  
Branch: `feature/renderingUI`

Responsibilities:
- `GamePanel`
- `DrawQueue`
- `RenderItem`
- Score and time display
- Start screen UI
- End screen UI (win & game over)

Primary Focus:
- Layered rendering pipeline
- Visual feedback system
- UI state transitions

---

## 3. Git Workflow Rules

- All features implemented in dedicated branches.
- No direct commits to `main`.
- All merges require:
  - Pull Request
  - At least one review
  - No merge conflicts
  - Confirmed alignment with UML

---

## 4. Next Steps

- Maven setup completion
- Begin feature branch development
- First milestone integration review at next weekly meeting

---

## Architectural Direction Moving Forward

The system is now formally structured into modular subsystems:

- Game Engine
- World & Map System
- Player & Interaction Layer
- Enemy AI
- Rendering Pipeline

Each subsystem is independently developed but integrated through the centralized tick-based game loop.

This concludes Meeting 02.
