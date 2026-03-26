# Meeting 01 – Project Initialization

**Date:** Week 1  
**Organized by:** Auston (scheduled via When2Meet) (02.09.2026, 23PM via Discord)  
**Objective:** Establish project direction and assign Phase 1 responsibilities  

---

## 1. Game Concept Decision

After discussion and voting, the team selected:

**Dungeon-themed 2D arcade game**

Reasons:
- Clear mapping to assignment requirements
- Strong visual identity
- Flexible enemy and reward mechanics
- Easy abstraction into grid-based architecture

---

## 2. Core Design Discussions

### Enemy Movement Strategy
- Avoid simplistic "Goomba-style" predictable movement
- Investigate search algorithms for enemy AI
- Potential approaches:
  - Greedy movement toward player
  - Basic pathfinding
  - Patrol route logic for specific enemies

---

### Art Direction
- Pixel art style chosen for simplicity
- Focus on readability over visual complexity

---

### Technology Stack Considerations
- Investigate **Java Swing** for UI framework
- Evaluate UML tools:
  - PlantUML
  - SmartDraw
  - Other diagramming tools

---

### Audio Discussion (Future Phase)
- Background music (TBD)
- Sound effects (TBD)
- To be implemented after core mechanics are complete

---

## 3. UML Planning

Action items:
- Map assignment requirements directly to game concept
- Ensure diagram elements reflect:
  - Core game loop
  - Board/grid structure
  - Reward and punishment abstraction
  - Character hierarchy
  - Rendering pipeline

---

## 4. Initial Class Brainstorm

Proposed core classes:

- `Game`
- `Board`
- `Grid` (abstract)
- `Cell`
  - Floor
  - Wall
  - Punishment
- `Character`
  - Player
  - Enemy
- `DrawQueue`

Additional supporting classes to be refined during UML development.

---

## 5. Phase 1 Work Division

### UI Mockups
Draw gameplay, win screen, and game over screens  
**Assigned to:** James  

### UML Diagrams
Class diagrams and system architecture  
**Assigned to:** Rammy, Yui  

### One-Page Game Plan
Project description and game mechanics  
**Assigned to:** Xavier  

### Team Contract (LaTeX)
Formal collaboration agreement  
**Assigned to:** Auston  

---

## 6. Next Meeting (in person check)

- **Wednesday**
- Review progress on all Phase 1 deliverables
- Validate UML structure
- Ensure alignment with assignment rubric